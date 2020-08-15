package com.example.ezcashapp;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.plaid.link.Plaid;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

/**
 *
 * NAME: EZCashApp - Base class of the application.
 *
 * DESCRIPTION: This is a Base class for maintaining global application state. It describes specialized tasks that need to run before the creation of the first activity.
 *
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 7/31/2020
 *
 */


public class EZCashApp extends Application implements LifecycleObserver {

    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;

    /**
     *
     * NAME: EZCashApp::onCreate() - Initializes member variables of the class
     *
     * SYNOPSIS: protected void onCreate()
     *
     * DESCRIPTION: This method controls the user presence within all activities of the application and handles other services used within the application.
     *              It initializes users database reference and handles the online presence when the application is in background/foreground state.
     *
     * RETURNS: Nothing
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 7/31/2020
     *
     */


    @Override
    public void onCreate(){
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        Plaid.INSTANCE.initialize(this);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        mAuth = FirebaseAuth.getInstance();

    }

    /**
     *
     * NAME: EZCashApp::onAppBackgrounded() - Handles activities and services when app is in background
     *
     * SYNOPSIS: private void onAppBackgrounded()
     *
     * DESCRIPTION: This method controls the user presence when the application is in background state.
     *
     * RETURNS: Nothing
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 7/31/2020
     *
     */

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        Log.d("MyApp", "App in background");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            mUserDatabase.child("online").setValue("no");
        }

    }

    /**
     *
     * NAME: EZCashApp::onAppBackgrounded() - Handles activities and services when app is in foreground
     *
     * SYNOPSIS: private void onAppBackgrounded()
     *
     * DESCRIPTION: This method controls the user presence when the application is in foreground state.
     *
     * RETURNS: Nothing
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 7/31/2020
     *
     */

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        Log.d("MyApp", "App in foreground");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            mUserDatabase.child("online").setValue("yes");
        }
    }

}