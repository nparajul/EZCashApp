package com.example.ezcashapp;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

/**
 *
 * NAME: MainActivity - The controller for the home page of the application
 *
 * DESCRIPTION: This class populates the home screen after successful login. It redirects the user to other pages of the application.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 7/31/2020
 *
 */
public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    private Toolbar mToolbar;
    RelativeLayout rFriends,rTransactions,rChat,rExit;

    /**
     *
     * NAME: MainActivity::onCreate() - Initializes member variables of the class
     *
     * SYNOPSIS: protected void onCreate(Bundle savedInstanceState)
     *           savedInstanceState: State where application was left off previously
     *
     * DESCRIPTION: This method is called when MainActivity is first called or launched.
     *              It initializes all member variables, including the database reference, and is responsible to create the MainActivity.
     *
     * RETURNS: Nothing
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 7/31/2020
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        }

        mToolbar= findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        rFriends = findViewById(R.id.friends);
        rTransactions = findViewById(R.id.transactions);
        rChat = findViewById(R.id.chat);
        rExit = findViewById(R.id.exit);

        /**
         * OnClickListener directs the user to the requested pages based on user's click.
         */

        rFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, AddedFriendsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        rTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, TransactionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        rChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        rExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("Are you sure you want to Exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }


    /**
     *
     * NAME: MainActivity::onStart() - makes the activity visible to the user
     *
     * SYNOPSIS: public void onStart()
     *
     * DESCRIPTION: This method is called when MainActivity enters started state.
     *              It makes the activity visible to the user, as the app prepares for the activity to enter the foreground and become interactive.
     *              Also, it sets the user's presence online and the value is stored in the FirebaseDatabase.
     *
     * RETURNS: Nothing. It's a void function.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 7/31/2020
     *
     */

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null)
        {
            sendToStart();
        }else{
            mUserRef.child("online").setValue("yes");

        }

    }

    /**
     *
     * NAME: MainActivity::onKeyDown() - controls the back button press
     *
     * SYNOPSIS:  public boolean onKeyDown(int keyCode, KeyEvent event)
     *            keycode ->  integer value indicating the key pressed
     *            event -> event to be executed
     *
     * DESCRIPTION: This method overrides the default action on the back key press to ensure the users are no longer using the application
     *
     * RETURNS: True after successfully completing the task.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 8/1/2020
     *
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            //'Back' button is set to start MainActivity
            finishAffinity();
        }
        return true;
    }

    /**
     *
     * NAME: MainActivity::sendToStart() - redirects user to login page
     *
     * SYNOPSIS: private void sendToStart()
     *
     * DESCRIPTION: This method redirects the user to the login page if the user hasn't logged in or if user login is expired.
     *
     * RETURNS: Nothing. It's a void function.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 7/31/2020
     *
     */
    private void sendToStart(){
        Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    /**
     *
     * NAME: MainActivity::onCreateOptionsMenu() - inflates the toolbar menu
     *
     * SYNOPSIS:  public boolean onCreateOptionsMenu(Menu menu)
     *            menu -> main menu displayed to the user
     *
     * DESCRIPTION: This method inflates the toolbar menu with menu items.
     *
     * RETURNS: True after successfully inflating the menu.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 7/31/2020
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    /**
     *
     * NAME: MainActivity::onOptionsItemSelected() - handles the menu options
     *
     * SYNOPSIS:    public boolean onOptionsItemSelected(MenuItem item)
     *              item  -> menu item selected by the user
     *
     *
     * DESCRIPTION: This method handles all menu options and directs the user to other pages based on what they select.
     *
     * RETURNS: True after successfully handling a menu item
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 7/31/2020
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_logout_btn){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser!=null) {
                mUserRef.child("online").setValue("no");
                final String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
                mUserRef.child("lastSeen").setValue(currentDateTime);
            }
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        if(item.getItemId() == R.id.main_account_settings){
            Intent newIntent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(newIntent);
        }
        if(item.getItemId() == R.id.main_allUsers){
            Intent newIntent = new Intent(MainActivity.this, UsersActivity.class);
            startActivity(newIntent);
        }

        return true;
    }
}
