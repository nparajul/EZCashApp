package com.example.ezcashapp;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

/**
 *
 * NAME: StartActivity - The controller for the Login page of the application.
 *
 * DESCRIPTION: This class populates the login page. The login page is the first page visible to the user after installing the application, or starting the application
 *              after the user logs out of the application. It displays text fields for email and password, and takes the user to MainActivity after successful login.
 *
 *
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 8/1/2020
 *
 */


public class StartActivity extends AppCompatActivity {


    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;
    private Button mRegBtn;
    private Button mLoginBtn;
    private ProgressDialog mLoginProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private Toolbar mToolbar;


    /**
     *
     * NAME: StartActivity::onCreate() - Initializes member variables of the class
     *
     * SYNOPSIS: protected void onCreate(Bundle savedInstanceState)
     *           savedInstanceState: State where application was left off previously
     *
     * DESCRIPTION: This method is called when Login activity is first called or launched.
     *              It initializes all member variables, and is responsible to create the Login Activity.
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
        setContentView(R.layout.activity_start);
        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("EZ Cash App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mLoginProgress = new ProgressDialog(this);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        mLoginEmail = findViewById(R.id.email);
        mLoginPassword = findViewById(R.id.password);
        mRegBtn =  findViewById(R.id.register);
        mLoginBtn = findViewById(R.id.button_login);

        mRegBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                Intent reg_intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(reg_intent);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)) {
                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your login credentials!");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    loginUser(email, password);
                }
            }
        });
    }

    /**
     *
     * NAME: StartActivity::loginUser() - checks a user's login credentials and starts the Main Activity after login success
     *
     * SYNOPSIS: private void loginUser(String email, String password)
     *           email -> user's email address
     *           password -> user's password
     *
     * DESCRIPTION: This method is called when Login button is clicked.
     *              The login credentials are checked with firebase's registered users and validated before the home page loads.
     *
     *
     * RETURNS: Nothing.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 8/1/2020
     *
     */
    private void loginUser(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String token;
                if(task.isSuccessful())
                {
                    mLoginProgress.dismiss();

                    final String current_user_id = mAuth.getCurrentUser().getUid();

                    Task<InstanceIdResult> deviceToken = FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String deviceToken = instanceIdResult.getToken();
                            mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent mainIntent = new Intent(StartActivity.this,MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();

                                }
                            });
                        }
                    });

                }
                else{
                    mLoginProgress.hide();
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    Toast.makeText(StartActivity.this, "Cannot Sign In: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
}
