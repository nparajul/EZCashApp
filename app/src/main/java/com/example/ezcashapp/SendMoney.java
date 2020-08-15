package com.example.ezcashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * NAME: ProfileActivity - The controller for the Dwolla User Registration page of the application
 *
 * DESCRIPTION: This class populates the Register For Payment page screen after the user clicks "Send Money" on the SendMoney page, if User hasn't yet signed up
 *              with Dwolla. It allows user to create a registration with Dwolla which is the first step required to sending/receiving money.
 *              The page is visible when Users click on Add my Bank menu option on Send Money page's menu option.
 *
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 8/1/2020
 *
 */


public class SendMoney extends AppCompatActivity {

    private Retrofit mRetrofit;
    private RetroInterface mRetroInterface;
    private String BASE_URL = "http://172.18.105.36:3000";
    private DatabaseReference mUserDatabase;
    private DatabaseReference mPaymentDatabase;
    private FirebaseUser mCurrentUser;
    private String list_user_id;


    /**
     *
     * NAME: SendMoney::onCreate() - Initializes member variables of the class
     *
     * SYNOPSIS: protected void onCreate(Bundle savedInstanceState)
     *           savedInstanceState: State where application was left off previously
     *
     * DESCRIPTION: This method is called when SendMoney activity is first called or launched.
     *              It initializes all member variables and is responsible to create the SendMoney Activity.
     *
     *
     * RETURNS: Nothing.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 8/1/2020
     *
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        mPaymentDatabase = FirebaseDatabase.getInstance().getReference().child("payment_details").child(uid);

        mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        mRetroInterface = mRetrofit.create(RetroInterface.class);

        handleLoginDialog();

    }

    /**
     *
     * NAME: SendMoney::handleLoginDialog() -
     *
     * SYNOPSIS: protected void onCreate(Bundle savedInstanceState)
     *           savedInstanceState: State where application was left off previously
     *
     * DESCRIPTION: This method registers the user for payments with Dwolla and generates a link_token (by making a POST request to "/create_link_token"),
     *              after retrieving the customerUrl from backend server
     *
     *
     * RETURNS: Nothing.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 8/1/2020
     *
     */

    private void handleLoginDialog() {

        final TextInputLayout firstName, lastName, email;
        firstName = findViewById(R.id.firstName_dwolla);
        lastName = findViewById(R.id.lastName_dwolla);
        email = findViewById(R.id.email_dwolla);
        Button reg = findViewById(R.id.button_reg_dwolla);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog mProgress;
                mProgress = new ProgressDialog(SendMoney.this);
                mProgress.setTitle("Adding user for payment");
                mProgress.setMessage("Please wait while we save the changes");
                mProgress.show();

                HashMap<String, String> map = new HashMap<>();
                map.put("firstName", firstName.getEditText().getText().toString());
                map.put("lastName", lastName.getEditText().getText().toString());
                map.put("email", email.getEditText().getText().toString());

                Call<DwollaLogin> call = mRetroInterface.executeDwollaLogin(map);

                call.enqueue(new Callback<DwollaLogin>() {
                    @Override
                    public void onResponse(Call<DwollaLogin> call, Response<DwollaLogin> response) {

                        if(response.code() == 200)
                        {
                            final DwollaLogin result = response.body();
                            final String customerUrl = result.getCustomerUrl();
                            mPaymentDatabase.child("paymentUrl").setValue(customerUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                         if(task.isSuccessful()){

                                             HashMap<String, String> map = new HashMap<>();
                                             map.put("muserId", mCurrentUser.getUid() );

                                             Call<PlaidToken> call = mRetroInterface.executePlaidToken(map);
                                             call.enqueue(new Callback<PlaidToken>() {
                                                              @Override
                                                              public void onResponse(Call<PlaidToken> call, Response<PlaidToken> response) {
                                                                  if(response.code() == 200)
                                                                  {

                                                                      PlaidToken result = response.body();
                                                                      String retVal = result.getLink_token();


                                                                      Intent plaidIntent = new Intent(SendMoney.this, PlaidActivity.class);
                                                                      plaidIntent.putExtra("link_token", retVal);
                                                                      plaidIntent.putExtra("customerUrl", customerUrl);
                                                                      mProgress.dismiss();
                                                                      startActivity(plaidIntent);

                                                                  }

                                                              }

                                                              @Override
                                                              public void onFailure(Call<PlaidToken> call, Throwable t) {



                                                              }
                                                          }

                                             );

                                         }
                                        else{
                                             Toast.makeText(SendMoney.this,"Error in saving changes" ,Toast.LENGTH_LONG).show();
                                         }
                                }
                            });

                        }

                    }

                    @Override
                    public void onFailure(Call<DwollaLogin> call, Throwable t) {

                        Toast.makeText(SendMoney.this,t.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

            }
        });


    }

    //discarded function

//    private void handleBankInfo(String customerUrl) {
//
//        ProgressDialog mProgress;
//        mProgress = new ProgressDialog(SendMoney.this);
//        mProgress.setTitle("Loading Bank Verification Page");
//        mProgress.setMessage("Please wait while we load the bank verification page");
//        mProgress.show();
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("muserId", mCurrentUser.getUid() );
//
//        Call<PlaidToken> call = mRetroInterface.executePlaidToken(map);
//        call.enqueue(new Callback<PlaidToken>() {
//          @Override
//          public void onResponse(Call<PlaidToken> call, Response<PlaidToken> response) {
//              if(response.code() == 200)
//                {
//                    mProgress.dismiss();
//                    PlaidToken result = response.body();
//                    String retVal = result.getLink_token();
//
//                    Intent plaidIntent = new Intent(SendMoney.this, PlaidActivity.class);
//                    plaidIntent.putExtra("link_token", retVal);
//                    plaidIntent.putExtra("customerUrl", customerUrl);
//                    startActivity(plaidIntent);
//
//            }
//
//          }
//
//          @Override
//          public void onFailure(Call<PlaidToken> call, Throwable t) {
//
//
//
//          }
//        }
//
//        );
//
//    }
}