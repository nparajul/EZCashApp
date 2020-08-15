package com.example.ezcashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * NAME: TransferActivity - The controller for the TransferMoney page of the application
 *
 * DESCRIPTION: This class populates the Transfer Money page screen after the user clicks "Send Money" option from Transactions page.
 *              It displays a numeric field for users to enter the amount of money they wish to send to a friend.
 *              This page will only be visible if a user has successfully registered for payment with Dwolla and verified their bank with Plaid.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 8/1/2020
 *
 */


public class TransferActivity extends AppCompatActivity {

    private String sender_bank_url;
    private String receiver_user_id;
    private DatabaseReference mPaymentsDatabase;
    private FirebaseUser mCurrentUser;
    private String mReceiverBankUrl;

    private Retrofit mRetrofit;
    private RetroInterface mRetroInterface;
    private String BASE_URL = "http://172.18.105.36:3000";


    /**
     *
     * NAME: TransferActivity::onCreate() - Initializes member variables of the class
     *
     * SYNOPSIS: protected void onCreate(Bundle savedInstanceState)
     *           savedInstanceState: State where application was left off previously
     *
     * DESCRIPTION: This method is called when Transfer activity is first called or launched.
     *              It initializes all member variables and is responsible to create the TransferActivity. In additon, when the user enters an amount and clicks the
     *              send button, a POST request is sent to the server and the transfer will be completed from their account to the receiver's account.
     *              Both parties must have verified their bank accounts to complete a transfer.
     *
     * RETURNS: Nothing.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 7/31/2020
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);



        receiver_user_id = getIntent().getStringExtra("receiver_user_id");



        mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        mRetroInterface = mRetrofit.create(RetroInterface.class);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();



        mPaymentsDatabase = FirebaseDatabase.getInstance().getReference().child("payment_details");

        mPaymentsDatabase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sender_bank_url = snapshot.child("bankUrl").getValue().toString();
                mPaymentsDatabase.child(receiver_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(!snapshot.hasChild("bankUrl")){

                            Toast.makeText(TransferActivity.this,"User has not signed up for payments yet!",Toast.LENGTH_LONG).show();
                            Intent transactionIntent = new Intent(TransferActivity.this, TransactionsActivity.class);
                            startActivity(transactionIntent);
                        }
                        else {

                            mReceiverBankUrl = snapshot.child("bankUrl").getValue().toString();
                            EditText amount;
                            Button sendMoney;

                            amount = findViewById(R.id.amount);
                            sendMoney = findViewById(R.id.sendMoney);

                            sendMoney.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ProgressDialog mProgress;
                                    mProgress = new ProgressDialog(TransferActivity.this);
                                    mProgress.setTitle("Sending Money");
                                    mProgress.setMessage("Please wait");
                                    mProgress.show();

                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("sender_bank_url", sender_bank_url);
                                    map.put("receiver_bank_url", mReceiverBankUrl);
                                    map.put("amount", amount.getText().toString());

                                    Call<DwollaBank> call = mRetroInterface.makeTransfer(map);

                                    call.enqueue(new Callback<DwollaBank>() {
                                        @Override
                                        public void onResponse(Call<DwollaBank> call, Response<DwollaBank> response) {

                                            if (response.code() == 200) {
                                                final DwollaBank result = response.body();
                                                final String status = result.getStatus();
                                                Toast.makeText(TransferActivity.this, "Payment Sent!", Toast.LENGTH_LONG).show();
                                                Intent transactionIntent = new Intent(TransferActivity.this, TransactionsActivity.class);
                                                startActivity(transactionIntent);

                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<DwollaBank> call, Throwable t) {

                                            Toast.makeText(TransferActivity.this, "Error Sending money. Please contact the administrator for details!", Toast.LENGTH_LONG).show();

                                        }
                                    });
                                }
                            });


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


}