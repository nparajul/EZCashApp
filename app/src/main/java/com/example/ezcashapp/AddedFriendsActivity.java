package com.example.ezcashapp;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 *
 * NAME: AddedFriendsActivity - The controller for the Friends page of the application
 *
 * DESCRIPTION: This class populates the friends page screen after the user clicks Friends on the home screen.
 *              It displays a list of users who have added the current user as a friend and vice-versa.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 7/31/2020
 *
 */


public class AddedFriendsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mAddedFriendsList;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    FirebaseUser mCurrentUser;



    /**
     *
     * NAME: AddedFriends::onCreate() - Initializes member variables of the class
     *
     * SYNOPSIS: protected void onCreate(Bundle savedInstanceState)
     *           savedInstanceState: State where application was left off previously
     *
     * DESCRIPTION: This method is called when AddedFriends activity is first called or launched.
     *              It initializes all member variables, such as database references, RecyclerView, Toolbar,etc., and is responsible to create the AddedFriendsActivity.
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
        setContentView(R.layout.activity_added_friends);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("friends").child(mCurrentUserId);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        mToolbar= findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Added Users");
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
     * NAME: AddedFriendsActivity::onCreateOptionsMenu() - inflates the toolbar menu
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
     * NAME: AddedFriendsActivity::onOptionsItemSelected() - handles the menu options
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
            Intent newIntent = new Intent(AddedFriendsActivity.this,SettingsActivity.class);
            startActivity(newIntent);
        }

        if(item.getItemId() == R.id.page_allUsers){
            Intent newIntent = new Intent(AddedFriendsActivity.this, UsersActivity.class);
            startActivity(newIntent);
        }


        return true;
    }


    /**
     *
     * NAME: AddedFriendsActivity::onStart() - makes the activity visible to the user
     *
     * SYNOPSIS: public void onStart()
     *
     * DESCRIPTION: This method is called when AddedFriendsActivity enters started state.
     *              It makes the activity visible to the user, as the app prepares for the activity to enter the foreground and become interactive.
     *              Also, it queries the firebase database and returns a list of users from the friend collections and displays their name, and the timestamp of when
     *              the request was accepted. In addition, it handles the OnCLick methods of each item in the list view and directs the user to other pages
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
        mAddedFriendsList = findViewById(R.id.added_users_list);
        mAddedFriendsList.setHasFixedSize(true);
        mAddedFriendsList.setLayoutManager(new LinearLayoutManager(AddedFriendsActivity.this));

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        FirebaseRecyclerOptions<AddedFriends> friendsFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<AddedFriends>()
                .setQuery(mFriendsDatabase, AddedFriends.class)
                .build();


        FirebaseRecyclerAdapter<AddedFriends, AddedFriendsViewHolder> addedFriendsAdapter =
                new FirebaseRecyclerAdapter<AddedFriends, AddedFriendsViewHolder>(friendsFirebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull final AddedFriendsViewHolder holder, int position, @NonNull final AddedFriends model) {

                            final String list_user_id = getRef(position).getKey();

                            mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final String username = dataSnapshot.child("name").getValue().toString();
                                    String status = dataSnapshot.child("status").getValue().toString();
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
                                    holder.mFriendStatus.setText("Friend Since: "+model.getDate());
                                    Picasso.get().load(image).placeholder(R.drawable.default_image).into(holder.picture);

                                    holder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CharSequence options [] = new CharSequence[]{"View Profile", "Send a message"};

                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddedFriendsActivity.this);
                                            builder.setTitle("Interact with "+username);
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if(which == 0)
                                                    {
                                                        Intent profileIntent = new Intent(AddedFriendsActivity.this, ProfileActivity.class);
                                                        profileIntent.putExtra("user_id", list_user_id );
                                                        startActivity(profileIntent);
                                                    }

                                                    if(which == 1){

                                                        Intent chatIntent = new Intent(AddedFriendsActivity.this, MessagesActivity.class);
                                                        chatIntent.putExtra("user_id", list_user_id );
                                                        chatIntent.putExtra("user_name", username);
                                                        startActivity(chatIntent);
                                                    }
                                                }
                                            });

                                            builder.show();
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
                    public AddedFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_single_layout, viewGroup, false);
                        return new AddedFriendsViewHolder(view);
                    }
                };


        mAddedFriendsList.setAdapter(addedFriendsAdapter);
        addedFriendsAdapter.startListening();
    }


    /**
     *
     * NAME: AddedFriendsViewHolder - ViewHolder class to render the AddedFriends ListView
     *
     * DESCRIPTION: This class is used to render the Listview. It describes an item view and metadata about its place within the RecyclerView.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 7/31/2020
     *
     */


    public static class AddedFriendsViewHolder extends RecyclerView.ViewHolder {

        /**
         * member variables
         */
        ImageView picture, mOnlineStatus;
        TextView mFriendName, mFriendStatus;
        View mView;

        /**
         *
         * NAME: AddedFriendsViewHolder::AddedFriendsViewHolder() - constructor for the class
         *
         * SYNOPSIS:   public AddedFriendsViewHolder(@NonNull View itemView)
         *             itemView ->  each item of the ListView
         *
         * DESCRIPTION: This is the constructor of the AddedFriendsViewHolder class. It initializes the member variables.
         *
         * RETURNS: Nothing.
         *
         * AUTHOR: Nitesh Parajuli
         *
         * DATE 7/31/2020
         *
         */
        public AddedFriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            picture = itemView.findViewById(R.id.users_single_image);
            mFriendName = itemView.findViewById(R.id.users_single_name);
            mFriendStatus = itemView.findViewById(R.id.users_single_status);
            mOnlineStatus = itemView.findViewById(R.id.users_single_online_icon);


        }
    }




}
