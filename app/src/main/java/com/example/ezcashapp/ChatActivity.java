package com.example.ezcashapp;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 *
 * NAME: ChatActivity - The controller for the Chats page of the application
 *
 * DESCRIPTION: This class populates the chats page screen after the user clicks Chats on the home screen.
 *              It displays a list of friends who have communicated with the current user.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 7/31/2020
 *
 */
public class ChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mChatList;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;

    private DatabaseReference mChatDatabase;
    private DatabaseReference mMessageDatabase;

    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    FirebaseUser mCurrentUser;


    /**
     *
     * NAME: ChatActivity::onCreate() - Initializes member variables of the class
     *
     * SYNOPSIS: protected void onCreate(Bundle savedInstanceState)
     *           savedInstanceState: State where application was left off previously
     *
     * DESCRIPTION: This method is called when ChatActivity activity is first called or launched.
     *              It initializes all member variables, such as database references, RecyclerView, Toolbar,etc., and is responsible to create the ChatActivity.
     *              It also handles the toolbar buttons.
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
        setContentView(R.layout.activity_chat);



        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mChatDatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(mCurrentUserId);
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("friends").child(mCurrentUserId);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrentUserId);

        mToolbar= findViewById(R.id.chat_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     *
     * NAME: ChatActivity::onCreateOptionsMenu() - inflates the toolbar menu
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
        getMenuInflater().inflate(R.menu.page_menu,menu);
        return true;
    }

    /**
     *
     * NAME: ChatActivity::onOptionsItemSelected() - handles the menu options
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

        if(item.getItemId() == R.id.page_account_settings){
            Intent newIntent = new Intent(ChatActivity.this,SettingsActivity.class);
            startActivity(newIntent);
        }

        if(item.getItemId() == R.id.page_allUsers){
            Intent newIntent = new Intent(ChatActivity.this, UsersActivity.class);
            startActivity(newIntent);
        }


        return true;
    }

    /**
     *
     * NAME: ChatActivity::onStart() - makes the activity visible to the user
     *
     * SYNOPSIS: public void onStart()
     *
     * DESCRIPTION: This method is called when ChatActivity enters started state.
     *              It makes the activity visible to the user, as the app prepares for the activity to enter the foreground and become interactive.
     *              Also, it queries the firebase database and returns a list of users from the chat collections and displays their name, and the last message sent/received
     *              by the user. In addition, it handles the OnCLick methods of each item in the list view and directs the user to other pages
     *              based on user's preference.
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mChatList = findViewById(R.id.chat_list);
        mChatList.setHasFixedSize(true);
        mChatList.setLayoutManager(linearLayoutManager);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        Query conversationQuery = mChatDatabase.orderByChild("timestamp");

        FirebaseRecyclerOptions<Chats> friendsFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Chats>()
                .setQuery(conversationQuery, Chats.class)
                .build();


        FirebaseRecyclerAdapter<Chats, ChatViewHolder> chatAdapter =
                new FirebaseRecyclerAdapter<Chats, ChatViewHolder>(friendsFirebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull final Chats model) {

                        final String list_user_id = getRef(position).getKey();

                        Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);

                        lastMessageQuery.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                String data = snapshot.child("message").getValue().toString();
                                String type = snapshot.child("type").getValue().toString();
                                Boolean isSeen = model.isSeen();

                                if(type.equals("text")) {
                                    holder.mFriendStatus.setText(data);
                                }
                                else
                                {
                                    String displayText = "Photo Image";
                                    holder.mFriendStatus.setText(displayText);
                                }
                                if(!isSeen){
                                    holder.mFriendStatus.setTypeface(holder.mFriendStatus.getTypeface(), Typeface.BOLD);
                                } else {
                                    holder.mFriendStatus.setTypeface(holder.mFriendStatus.getTypeface(), Typeface.NORMAL);
                                }


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

                        mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String username = dataSnapshot.child("name").getValue().toString();
                                String image = dataSnapshot.child("image").getValue().toString();

                                if(dataSnapshot.hasChild("online")) {
                                    String userOnline = dataSnapshot.child("online").getValue().toString();
                                    if (userOnline.equals("yes")) {
                                        holder.mOnlineStatus.setVisibility(View.VISIBLE);
                                    } else {
                                        holder.mOnlineStatus.setVisibility(View.INVISIBLE);
                                    }
                                }

                                holder.mFriendName.setText(username);
                                Picasso.get().load(image).placeholder(R.drawable.default_image).into(holder.picture);

                                holder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent chatIntent = new Intent(ChatActivity.this, MessagesActivity.class);
                                        chatIntent.putExtra("user_id", list_user_id);
                                        chatIntent.putExtra("user_name", username);
                                        startActivity(chatIntent);
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ChatActivity.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_single_layout, viewGroup, false);
                        return new ChatActivity.ChatViewHolder(view);
                    }
                };


        mChatList.setAdapter(chatAdapter);
        chatAdapter.startListening();
    }

    /**
     *
     * NAME: ChatViewHolder - ViewHolder class to render the Chat ListView
     *
     * DESCRIPTION: This class is used to render the mChatList listview. It describes an item view and metadata about its place within the RecyclerView.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 7/31/2020
     *
     */


    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        /**
         * member variables
         */
        ImageView picture, mOnlineStatus;
        TextView mFriendName, mFriendStatus;
        View mView;

        /**
         *
         * NAME: ChatViewHolder::ChatViewHolder() - constructor for the class
         *
         * SYNOPSIS:   public ChatViewHolder(@NonNull View itemView)
         *             itemView ->  each item of the ListView
         *
         * DESCRIPTION: This is the constructor of the ChatViewHolder class. It initializes the member variables.
         *
         * RETURNS: Nothing.
         *
         * AUTHOR: Nitesh Parajuli
         *
         * DATE 7/31/2020
         *
         */

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            picture = itemView.findViewById(R.id.users_single_image);
            mFriendName = itemView.findViewById(R.id.users_single_name);
            mFriendStatus = itemView.findViewById(R.id.users_single_status);
            mOnlineStatus = itemView.findViewById(R.id.users_single_online_icon);


        }
    }

}
