package com.example.ezcashapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * NAME: MessagesActivity - The controller for the Messages page of the application
 *
 * DESCRIPTION: This class populates the messages screen after the user clicks a user in chat page.
 *              It displays a list of messages exchanged between two users.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 8/1/2020
 *
 */

public class MessagesActivity extends AppCompatActivity {

    /**
     * member variables
     */
    private String mChatUser;
    private Toolbar mChatToolbar;

    private DatabaseReference mRootRef;

    private TextView mTitleView;
    private TextView mLastSeenView;
    private CircleImageView mProfileImage;
    private FirebaseAuth mAuth;
    private String mCurrentUser;

    private ImageButton mChatAddBtn;
    private ImageButton mChatSendBtn;
    private EditText mChatMessageView;

    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mRefreshLayout;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;

    private static final int ITEMS_TO_LOAD = 12;
    private int mCurrentPage = 1;

    private static final int GALLERY_PICK = 1;

    private StorageReference mImageStorage;

    private int itemPos = 0;
    private String mLastKey="";
    private String mPrevKey="";



    /**
     *
     * NAME: MessagesActivity::onCreate() - Initializes member variables of the class
     *
     * SYNOPSIS: protected void onCreate(Bundle savedInstanceState)
     *           savedInstanceState: State where application was left off previously
     *
     * DESCRIPTION: This method is called when Messages activity is first called or launched.
     *              It initializes all member variables, such as database references, RecyclerView, Toolbar,etc., and is responsible to create the MessagesActivity.
     *              It also handles the toolbar buttons, and send message button.
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
        setContentView(R.layout.activity_messages);

        mChatToolbar = findViewById(R.id.chat_page_toolbar);
        setSupportActionBar(mChatToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mChatToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser().getUid();

        mChatUser = getIntent().getStringExtra("user_id");

        String userName = getIntent().getStringExtra("user_name");
        //actionBar.setTitle(userName);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_custom_bar, null);

        actionBar.setCustomView(action_bar_view);

        mTitleView = findViewById(R.id.custom_bar_name);
        mLastSeenView = findViewById(R.id.custom_bar_seen);
        mProfileImage = findViewById(R.id.custom_bar_image);

        mChatAddBtn = findViewById(R.id.chat_add_btn);
        mChatSendBtn = findViewById(R.id.chat_send_btn);
        mChatMessageView = findViewById(R.id.chat_message_view);

        mAdapter = new MessageAdapter(messagesList);

        mMessagesList = findViewById(R.id.messages_list);
        mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdapter);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mRootRef.child("chat").child(mCurrentUser).child(mChatUser).child("seen").setValue(true);
        
        loadMessages();


        mTitleView.setText(userName);

        mRootRef.child("users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String online = snapshot.child("online").getValue().toString();
                String image = snapshot.child("image").getValue().toString();

                if(online.equals("yes"))
                {
                    mLastSeenView.setText("Online");
                }
                else
                {
                    String lastSeen = snapshot.child("lastSeen").getValue().toString();
                    mLastSeenView.setText("Last Online: "+ lastSeen);
                }

                Picasso.get().load(image).placeholder(R.drawable.default_image).into(mProfileImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mRootRef.child("chat").child(mCurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.hasChild(mChatUser)){

                    Map chatMap = new HashMap();
                    chatMap.put("seen",false);
                    final String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
                    chatMap.put("timestamp", currentDateTime);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("chat/"+mCurrentUser+"/"+mChatUser,chatMap);
                    chatUserMap.put("chat/"+mChatUser+"/"+mCurrentUser,chatMap);

                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if(error != null)
                            {
                                Log.d("CHAT_LOG", error.getMessage().toString());
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mChatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);
            }
        });




    }

    /**
     *
     * NAME: MessagesActivity:onActivityResult() - allowss user to pick images from the gallery
     *
     * SYNOPSIS: protected void onActivityResult(int requestCode, int resultCode, Intent data)
     *           requestCode ->  The integer request code originally supplied to startActivityForResult() in the onCreate method, allowing the application to identify
     *           where this result came from.
     *           resultCode -> The integer result code returned by the child activity through its setResult().
     *           data -> Gallery Intent, which can return image result data to the caller
     *
     * DESCRIPTION: This method is called when user select the + icon in the message screen to send an image message.
     *              It opens the phone gallery and allows the user to select the image they wish to send, and stores the image in Firebase storage.
     *
     * RETURNS: Nothing.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 8/1/2020
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            final Uri imageUri = data.getData();

            final String current_user_ref = "messages/" + mCurrentUser + "/" + mChatUser;
            final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUser;

            DatabaseReference user_message_push = mRootRef.child("messages")
                    .child(mCurrentUser).child(mChatUser).push();

            final String push_id = user_message_push.getKey();


            StorageReference filepath = mImageStorage.child("message_images").child(push_id + ".jpg");

            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {

                        Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String imageUrl = uri.toString();
                                Map messageMap = new HashMap();
                                messageMap.put("message", imageUrl);
                                messageMap.put("type", "image");
                                final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
                                messageMap.put("time", currentDate);
                                messageMap.put("from", mCurrentUser);

                                Map messageUserMap = new HashMap();
                                messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                                messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                                mChatMessageView.setText("");

                                mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        if (databaseError != null) {

                                            Log.d("CHAT_LOG", databaseError.getMessage().toString());

                                        }

                                    }
                                });

                            }
                        });


                    }

                }
            });


        }

    }


    /**
     *
     * NAME: MessagesActivity:loadMessages() - loads the messages between two users by retrieving data from messages collectoin
     *
     * SYNOPSIS: private void loadMessages()
     *
     * DESCRIPTION: This method is called from onCreate Method of MessagesActivity.
     *             It populates the messages screen with the messages sent/received by the user.
     *
     * RETURNS: Nothing.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 8/1/2020
     *
     */

    private void loadMessages() {

        Query message_query = mRootRef.child("messages").child(mCurrentUser).child(mChatUser);//.limitToLast(mCurrentPage * ITEMS_TO_LOAD);

        message_query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Messages message = snapshot.getValue(Messages.class);

                messagesList.add(message);
                mAdapter.notifyDataSetChanged();

                mMessagesList.scrollToPosition(messagesList.size()-1);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     *
     * NAME: MessagesActivity:sendMessage() - retrieves the message body and stores in firebase collection messages after the send button is clicked
     *
     * SYNOPSIS: private void sendMessage()
     *
     * DESCRIPTION: This method is called from when the send message button is clicked.
     *             It gets the message body and stores it in firebase collection, as well as updates the chat collection.
     *
     * RETURNS: Nothing.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 8/1/2020
     *
     */

    private void sendMessage() {

        String message = mChatMessageView.getText().toString();

        if(!TextUtils.isEmpty(message)){

            String current_user_ref = "messages/"+mCurrentUser+"/"+mChatUser;
            String chat_user_ref = "messages/"+mChatUser+"/"+mCurrentUser;

            DatabaseReference user_messages_push = mRootRef.child("messages").child(mCurrentUser).child(mChatUser).push();

            String push_id = user_messages_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("type", "text");
            final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
            messageMap.put("time", currentDate);
            messageMap.put("from", mCurrentUser);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
            messageUserMap.put(chat_user_ref+"/"+push_id, messageMap);

            mChatMessageView.setText("");

            mRootRef.child("chat").child(mCurrentUser).child(mChatUser).child("seen").setValue(true);
            final String currentDate2 = DateFormat.getDateTimeInstance().format(new Date());
            mRootRef.child("chat").child(mCurrentUser).child(mChatUser).child("timestamp").setValue(currentDate2);

            mRootRef.child("chat").child(mChatUser).child(mCurrentUser).child("seen").setValue(false);
            final String currentDate3 = DateFormat.getDateTimeInstance().format(new Date());
            mRootRef.child("chat").child(mChatUser).child(mCurrentUser).child("timestamp").setValue(currentDate3);

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    if(error != null){
                        Log.d("CHAT_LOG", error.getMessage().toString());
                    }

                }
            });

        }
    }
}