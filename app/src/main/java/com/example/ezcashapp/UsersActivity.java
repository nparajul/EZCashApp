package com.example.ezcashapp;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
 * NAME: UsersActivity - The controller for the Users page of the application
 *
 * DESCRIPTION: This class populates the All Users page screen after the user clicks All Users on the menu option.
 *              It displays a list of users who are registered with the application.
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 7/31/2020
 *
 */

public class UsersActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mUsersDatabase;


    /**
     *
     * NAME: UsersActivity::onCreate() - Initializes member variables of the class
     *
     * SYNOPSIS: protected void onCreate(Bundle savedInstanceState)
     *           savedInstanceState: State where application was left off previously
     *
     * DESCRIPTION: This method is called when Users activity is first called or launched.
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
        setContentView(R.layout.activity_users);


        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     *
     * NAME: UsersActivity::onCreateOptionsMenu() - inflates the toolbar menu
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
        getMenuInflater().inflate(R.menu.page_menu, menu);
        return true;
    }


    /**
     *
     * NAME: UsersActivity::onOptionsItemSelected() - handles the menu options
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

        if (item.getItemId() == R.id.page_account_settings) {
            Intent newIntent = new Intent(UsersActivity.this, SettingsActivity.class);
            startActivity(newIntent);
        }

        if (item.getItemId() == R.id.page_allUsers) {
            Intent newIntent = new Intent(UsersActivity.this, UsersActivity.class);
            startActivity(newIntent);
        }

        return true;
    }

    /**
     *
     * NAME: UsersActivity::onStart() - makes the activity visible to the user
     *
     * SYNOPSIS: public void onStart()
     *
     * DESCRIPTION: This method is called when UsersActivity enters started state.
     *              It makes the activity visible to the user, as the app prepares for the activity to enter the foreground and become interactive.
     *              Also, it queries the firebase database and returns a list of users from the users collections and displays their name, and status.
     *              In addition, it handles the OnCLick methods of each item in the list view and directs the user to other pages
     *              based on user's preference.
     *
     * RETURNS: Nothing. It's a void function.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 8/1/2020
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        mUsersList = findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(UsersActivity.this));

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        FirebaseRecyclerOptions<Users> usersFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(mUsersDatabase, Users.class)
                .build();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> usersAdapter =
                new FirebaseRecyclerAdapter<Users, UsersViewHolder>(usersFirebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull final UsersViewHolder holder, int position, @NonNull final Users model) {

                        final String list_user_id = getRef(position).getKey();
                        System.out.println(list_user_id);
                        Log.d("ID", list_user_id);




                        mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    if (dataSnapshot.exists()) {
                                        String username = dataSnapshot.child("name").getValue().toString();
                                        String status = dataSnapshot.child("status").getValue().toString();
                                        String image = dataSnapshot.child("image").getValue().toString();


                                        holder.mUserStatus.setText(status);
                                        Picasso.get().load(image).placeholder(R.drawable.default_image).into(holder.mPicture);
                                        holder.mOnlineStatus.setVisibility(View.INVISIBLE);

                                        if (!list_user_id.equals(mCurrentUser.getUid().toString())) {
                                            holder.mUserName.setText(username);
                                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                                                    profileIntent.putExtra("user_id", list_user_id);
                                                    startActivity(profileIntent);
                                                }
                                            });
                                        } else {
                                            username = username + " (Me)";
                                            holder.mUserName.setText(username);
                                            holder.mView.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    Intent settingsIntent = new Intent(UsersActivity.this, SettingsActivity.class);
                                                    startActivity(settingsIntent);
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }

                        @NonNull
                        @Override
                        public UsersActivity.UsersViewHolder onCreateViewHolder
                        (@NonNull ViewGroup viewGroup,int i){
                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_single_layout, viewGroup, false);
                            return new UsersActivity.UsersViewHolder(view);
                        }

                };

        mUsersList.setAdapter(usersAdapter);
        usersAdapter.startListening();
    }
    /**
     *
     * NAME: UsersViewHolder - ViewHolder class to render the mUsersList ListView
     *
     * DESCRIPTION: This class is used to render the Listview. It describes an item view and metadata about its place within the RecyclerView.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 8/1/2020
     *
     */

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        /**
         * member variables
         */
        ImageView mPicture, mOnlineStatus;
        TextView mUserName, mUserStatus;
        View mView;

        /**
         *
         * NAME: UsersViewHolder::UsersViewHolder() - constructor for the class
         *
         * SYNOPSIS:   public UsersViewHolder(@NonNull View itemView)
         *             itemView ->  each item of the ListView
         *
         * DESCRIPTION: This is the constructor of the UsersViewHolder class. It initializes the member variables.
         *
         * RETURNS: Nothing.
         *
         * AUTHOR: Nitesh Parajuli
         *
         * DATE 8/1/2020
         *
         */
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            mPicture = itemView.findViewById(R.id.users_single_image);
            mUserName = itemView.findViewById(R.id.users_single_name);
            mUserStatus = itemView.findViewById(R.id.users_single_status);
            mOnlineStatus = itemView.findViewById(R.id.users_single_online_icon);


        }

    }
}
