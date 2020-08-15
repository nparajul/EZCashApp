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

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
 * NAME: TransactionsActivity - The controller for the Transactions page of the application
 *
 * DESCRIPTION: This class populates the Transactions page screen after the user clicks Transactions in the home page.
 *              It displays a list of users who are friends with the current user.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 8/1/2020
 *
 */

public class TransactionsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mAddedFriendsList;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mPaymentsDatabase;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    FirebaseUser mCurrentUser;
    private String mCurrent_state;

    /**
     *
     * NAME: TransactionsActivity::onCreate() - Initializes member variables of the class
     *
     * SYNOPSIS: protected void onCreate(Bundle savedInstanceState)
     *           savedInstanceState: State where application was left off previously
     *
     * DESCRIPTION: This method is called when Transaction activity is first called or launched.
     *              It initializes all member variables, such as database references, RecyclerView, Toolbar,etc., and is responsible to create the UsersActivity.
     *              It also handles the toolbar buttons.
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
        setContentView(R.layout.activity_added_friends);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("friends").child(mCurrentUserId);
        mPaymentsDatabase = FirebaseDatabase.getInstance().getReference().child("payment_details");
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        mToolbar= findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Send Money");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCurrent_state = "not_added";



        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     *
     * NAME: TransactionsActivity::onCreateOptionsMenu() - inflates the toolbar menu
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
     * DATE 8/1/2020
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.transaction_page_menu,menu);
        return true;
    }

    /**
     *
     * NAME: TransactionsActivity::onKeyDown() - controls the back button press
     *
     * SYNOPSIS:  public boolean onKeyDown(int keyCode, KeyEvent event)
     *            keycode ->  integer value indicating the key pressed
     *            event -> event to be executed
     *
     * DESCRIPTION: This method overrides the default action on the back key press to ensure the users are directed to the appropriate screen
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
            this.startActivity(new Intent(TransactionsActivity.this,MainActivity.class));
        }
        return true;
    }


    /**
     *
     * NAME: TransactionsActivity::onOptionsItemSelected() - handles the menu options
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
     * DATE 8/1/2020
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_account_settings){
            Intent newIntent = new Intent(TransactionsActivity.this,SettingsActivity.class);
            startActivity(newIntent);
        }

        if(item.getItemId() == R.id.main_add_bank){


            mPaymentsDatabase.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!snapshot.hasChild("paymentUrl") || !snapshot.hasChild("bankUrl")) {

                        mCurrent_state = "added";
                        Intent registerBank = new Intent(TransactionsActivity.this, SendMoney.class);
                        startActivity(registerBank);
                    }

                    else
                    {
                        if(mCurrent_state.equals("not_added")) {
                            Toast.makeText(TransactionsActivity.this, "You've already added your bank information!", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }


//            Intent newIntent = new Intent(TransactionsActivity.this,UsersActivity.class);
//            startActivity(newIntent);
            });

        }


        return true;
    }

    /**
     *
     * NAME: TransactionsActivity::onStart() - makes the activity visible to the user
     *
     * SYNOPSIS: public void onStart()
     *
     * DESCRIPTION: This method is called when TransactionsActivity enters started state.
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
        mAddedFriendsList.setLayoutManager(new LinearLayoutManager(TransactionsActivity.this));

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
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
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
                                        CharSequence options [] = new CharSequence[]{"View Profile", "Send Money"};

                                        AlertDialog.Builder builder = new AlertDialog.Builder(TransactionsActivity.this);
                                        builder.setTitle("Interact with "+username);
                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(which == 0)
                                                {
                                                    Intent profileIntent = new Intent(TransactionsActivity.this, ProfileActivity.class);
                                                    profileIntent.putExtra("user_id", list_user_id );
                                                    startActivity(profileIntent);
                                                }

                                                if(which == 1){

                                                    mPaymentsDatabase.child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                            if(!snapshot.hasChild("paymentUrl") || !snapshot.hasChild("bankUrl")) {

                                                                if(mCurrent_state.equals("not_added"))
                                                                {
                                                                    Toast.makeText(TransactionsActivity.this, "You must add your bank from the menu options before sending/receiving money!!!", Toast.LENGTH_LONG).show();

                                                                }
                                                            }
                                                            else
                                                            {

                                                                mPaymentsDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                        if (snapshot.hasChild("paymentUrl") && snapshot.hasChild("bankUrl")) {
                                                                            String sender_bank_url = snapshot.child("bankUrl").getValue().toString();
                                                                            Intent transferIntent = new Intent(TransactionsActivity.this, TransferActivity.class);
                                                                            transferIntent.putExtra("receiver_user_id", list_user_id);
                                                                            transferIntent.putExtra("sender_bank_url", sender_bank_url);
                                                                            startActivity(transferIntent);
                                                                        }
                                                                        else {
                                                                            if(mCurrent_state.equals("not_added")){
                                                                                Toast.makeText(TransactionsActivity.this,"Recipient hasn't added their bank yet!", Toast.LENGTH_LONG).show();
                                                                            }

                                                                        }


                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });


                                                            }


                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

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
     * DESCRIPTION: This class is used to render the mAddedFriendsList Listview. It describes an item view and metadata about its place within the RecyclerView.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 8/1/2020
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
         * DATE 8/1/2020
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
