package com.example.ezcashapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 *
 * NAME: RegisterActivity - The controller for the Registration page of the application
 *
 * DESCRIPTION: This class populates the registration page screen after the user clicks "Register" on the login page.
 *              It displays text fields for name, email, and password, and registers the user for the application.
 *              Upon successful registration, the user will be added to the users collection in firebase database.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 8/1/2020
 *
 */


public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateBtn;
    private Button mLoginBtn;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Toolbar mToolbar;
    private ProgressDialog mRegProgress;
    private DatabaseReference mDatabase;


    /**
     *
     * NAME: RegisterActivity::onCreate() - Initializes member variables of the class
     *
     * SYNOPSIS: protected void onCreate(Bundle savedInstanceState)
     *           savedInstanceState: State where application was left off previously
     *
     * DESCRIPTION: This method is called when Register activity is first called or launched.
     *              It initializes all member variables, such as database references, Toolbar, etc. and is responsible to create the RegisterActivity.
     *
     *
     * RETURNS: Nothing.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 8/1/2020
     *
     */
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDisplayName = findViewById(R.id.username);//ERROR
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mCreateBtn   = findViewById(R.id.button_reg);
        mLoginBtn    = findViewById(R.id.button_login);

        mToolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create an Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgress = new ProgressDialog(this);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(display_name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)) {
                    mRegProgress.setTitle("Registering a User");
                    mRegProgress.setMessage("Please wait while we create an account!");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    register_user(display_name, email, password);
                }
            }
        });
    }

    /**
     *
     * NAME: RegisterActivity::register_user() - completes user registration process and adds user to users collection.
     *
     * SYNOPSIS: private void register_user (final String display_name, String email, String password)
     *           display_name -> User's full name
     *           email -> user's email
     *           password -> user's password
     *
     * DESCRIPTION: This method is called when Register button is clicked.
     *              It adds the user to the users collection in firebase database and presents the user with home screen after successful registration.
     *
     *
     * RETURNS: Nothing.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 8/1/2020
     *
     */

    private void register_user (final String display_name, String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    final FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                    Task<InstanceIdResult> deviceToken = FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String deviceToken = instanceIdResult.getToken();
                            HashMap<String,String> usersMap = new HashMap<>();
                            usersMap.put("name",display_name);
                            usersMap.put("status","Status not set!");
                            usersMap.put("image","default");
                            usersMap.put("UID", current_user.getUid());
                            usersMap.put("device_token", deviceToken);

                            mDatabase.setValue(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mRegProgress.dismiss();
                                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            });
                        }
                    });
                }
                else{
                    mRegProgress.hide();
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    Toast.makeText(RegisterActivity.this, "Registration Failed : "+e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
}
