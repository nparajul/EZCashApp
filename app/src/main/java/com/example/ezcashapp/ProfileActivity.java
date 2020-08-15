package com.example.ezcashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * NAME: ProfileActivity - The controller for the Profile page of the application
 *
 * DESCRIPTION: This class populates the profile page screen after the user clicks "View Profile" on the Added Friends/All Users page.
 *              It displays the user's profile image, name, and status. In addition, it also allows sending a friend request, declining a friend request,
 *              accepting a friend request, canceling a friend request, and unfriend functionality.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 8/1/2020
 *
 */



public class ProfileActivity extends AppCompatActivity {

    private TextView mProfileName, mProfileStatus;
    private ImageView mProfileImage;
    private Button mProfileSendReqBtn, mProfileDeclineReqBtn;

    private DatabaseReference mUsersDatabase;
    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;

    private FirebaseUser mCurrentUser;

    private String mCurrent_state;

    /**
     *
     * NAME: ProfileActivity::onCreate() - Initializes member variables of the class
     *
     * SYNOPSIS: protected void onCreate(Bundle savedInstanceState)
     *           savedInstanceState: State where application was left off previously
     *
     * DESCRIPTION: This method is called when Profile activity is first called or launched.
     *              It initializes all member variables, such as database references, ImageView, and all friend/unfriend functionality buttons etc.,
     *              and is responsible to create the ProfileActivity. Depending on the friendship state of two users, the buttons are populated and handled based on
     *              user's preference, and involved collections are altered in the firebase database.
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
        setContentView(R.layout.activity_profile);

        final String user_id  = getIntent().getStringExtra("user_id");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        mFriendReqDatabase= FirebaseDatabase.getInstance().getReference().child("friend_req");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("friends");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        mProfileImage = findViewById(R.id.profile_image);
        mProfileName = findViewById(R.id.profile_displayName);
        mProfileStatus = findViewById(R.id.profile_status);
        mProfileSendReqBtn = findViewById(R.id.profile_send_req_btn);
        mProfileDeclineReqBtn = findViewById(R.id.profile_decline_btn);
        mProfileDeclineReqBtn.setVisibility(View.INVISIBLE);
        mProfileDeclineReqBtn.setEnabled(false);

        mCurrent_state="not_friends";


        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();



        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String display_name = snapshot.child("name").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                String image = snapshot.child("image").getValue().toString();

                mProfileName.setText(display_name);
                mProfileStatus.setText(status);

                Picasso.get().load(image).placeholder(R.drawable.default_image).into(mProfileImage);

                // --------------------- FRIENDS LIST / REQUEST ----------------------------------
                mFriendReqDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.hasChild(user_id))
                        {

                            String req_type = snapshot.child(user_id).child("request_type").getValue().toString();

                            if(req_type.equals("received")){

                                mCurrent_state = "req_received";
                                mProfileSendReqBtn.setText("Accept Friend Request");
                                mProfileDeclineReqBtn.setVisibility(View.VISIBLE);
                                mProfileDeclineReqBtn.setEnabled(true);
                            }
                            else if (req_type.equals("sent"))
                            {
                                mCurrent_state="req_sent";
                                mProfileSendReqBtn.setText("Cancel Friend Request");
                                mProfileDeclineReqBtn.setVisibility(View.INVISIBLE);
                                mProfileDeclineReqBtn.setEnabled(false);
                            }



                            dialog.dismiss();
                        }

                        else{

                            mFriendDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.hasChild(user_id)){

                                        mCurrent_state = "friends";
                                        mProfileSendReqBtn.setText("Unfriend");
                                        mProfileDeclineReqBtn.setVisibility(View.INVISIBLE);
                                        mProfileDeclineReqBtn.setEnabled(false);
                                    }

                                    dialog.dismiss();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    dialog.dismiss();
                                }
                            });

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProfileSendReqBtn.setEnabled(false);

                // --------------------  IF NOT ADDED --------------------------------
                if(mCurrent_state.equals("not_friends"))
                {
                    mFriendReqDatabase.child(mCurrentUser.getUid()).child(user_id).child("request_type")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                mFriendReqDatabase.child(user_id).child(mCurrentUser.getUid()).child("request_type")
                                        .setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        HashMap<String, String> notificationData = new HashMap<>();
                                        notificationData.put("from", mCurrentUser.getUid());
                                        notificationData.put("type","request");

                                        mNotificationDatabase.child(user_id).push().setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mCurrent_state = "req_sent";
                                                mProfileSendReqBtn.setText("Cancel Friend Request");
                                                mProfileDeclineReqBtn.setVisibility(View.INVISIBLE);
                                                mProfileDeclineReqBtn.setEnabled(false);
                                            }
                                        });


                                    }
                                });
                            }else{
                                Toast.makeText(ProfileActivity.this,"Failed Sending Request",Toast.LENGTH_LONG).show();
                            }

                            mProfileSendReqBtn.setEnabled(true);
                        }
                    });
                }

                //---------------- CANCEL REQUEST -------------------
                if(mCurrent_state.equals("req_sent"))
                {
                    mFriendReqDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendReqDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProfileSendReqBtn.setEnabled(true);
                                    mCurrent_state = "not_friends";
                                    mProfileSendReqBtn.setText("Send Friend Request");
                                    mProfileDeclineReqBtn.setVisibility(View.INVISIBLE);
                                    mProfileDeclineReqBtn.setEnabled(false);
                                }
                            });
                        }
                    });

                }

                // ------------------Req received ----------------------

                if(mCurrent_state.equals("req_received")){

                    final String currentDate = DateFormat.getDateInstance().format(new Date());
                    mFriendDatabase.child(mCurrentUser.getUid()).child(user_id).child("date").setValue(currentDate)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendDatabase.child(user_id).child(mCurrentUser.getUid()).child("date").setValue(currentDate)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mFriendReqDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mFriendReqDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mProfileSendReqBtn.setEnabled(true);
                                                    mCurrent_state = "friends";
                                                    mProfileSendReqBtn.setText("Unfriend");
                                                    mProfileDeclineReqBtn.setVisibility(View.INVISIBLE);
                                                    mProfileDeclineReqBtn.setEnabled(false);
                                                }
                                            });
                                        }
                                    });

                                }
                            });

                        }
                    });


                }

                // --------------- Unfriend ----------------------

                if(mCurrent_state.equals("friends"))
                {
                    mFriendDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProfileSendReqBtn.setEnabled(true);
                                    mCurrent_state = "not_friends";
                                    mProfileSendReqBtn.setText("Send Friend Request");
                                }
                            });
                        }
                    });

                }

            }
        });

        mProfileDeclineReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrent_state.equals("req_received"))
                {
                    mFriendReqDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendReqDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProfileSendReqBtn.setEnabled(true);
                                    mCurrent_state = "not_friends";
                                    mProfileSendReqBtn.setText("Send Friend Request");
                                    mProfileDeclineReqBtn.setVisibility(View.INVISIBLE);
                                    mProfileDeclineReqBtn.setEnabled(false);
                                }
                            });
                        }
                    });

                }
            }
        });


    }
}