package com.example.ezcashapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * NAME: Chats - Adapter class for chat messages
 *
 * DESCRIPTION: Adapter class which provides an interface whose implementations provides data and control to the display the messages sent/received by a user
 *
 * AUTHOR: Nitesh Parajuli
 *
 * DATE 7/31/2020
 *
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;
    private Context mContext;
    private DatabaseReference mUserDatabase;

    public MessageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     *
     * NAME: MessageAdapter() - constructor for MessageAdapter class
     *
     * SYNOPSIS: public MessageAdapter(List<Messages> mMessageList)
     *           mMessageList -> A list containing messages communicated between two users
     *
     *
     * DESCRIPTION: This is the constructor of the MessageAdapter class
     *
     * RETURNS: Nothing
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }

    /**
     *
     * NAME: MessageAdapter:onCreateViewHolder() - Called by RecyclerView to display the data at the specified position
     *
     * SYNOPSIS: public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
     *           viewGroup -> parent ViewGroup
     *           i -> position
     *
     *
     * DESCRIPTION: This function creates a ViewHolder constructed with a new View inflated from a messages_single_layout resouce file.
     *              This is the place where the RecyclerView comes when it needs a new ViewHolder.
     *
     * RETURNS: MessageViewHolder that was created
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_single_layout, viewGroup, false);
        return new MessageViewHolder(v);


    }

    /**
     *
     * NAME: MessageViewHolder - ViewHolder class to render the message ListView
     *
     * DESCRIPTION: This class is used to render the mMessageList listview. It describes an item view and metadata about its place within the RecyclerView.
     *
     * AUTHOR: Nitesh Parajuli
     *
     * DATE 7/31/2020
     *
     */

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        /**
         * member variables
         */
        public TextView senderMessageText,receiverMessageText;
        public CircleImageView profileImage;
        public TextView userName;
        public ImageView senderMessageImage, receiverMessageImage;

        /**
         *
         * NAME: MessageViewHolder::MessageViewHolder() - constructor for the class
         *
         * SYNOPSIS:   public ChatViewHolder(View view)
         *             view ->  each item of the ListView
         *
         * DESCRIPTION: This is the constructor of the MessageViewHolder class. It initializes the member variables.
         *
         * RETURNS: Nothing.
         *
         * AUTHOR: Nitesh Parajuli
         *
         * DATE 7/31/2020
         *
         */
        public MessageViewHolder(View view){
            super(view);

            senderMessageText = view.findViewById(R.id.sender_message_text);
            receiverMessageText = view.findViewById(R.id.receiver_message_text);
            profileImage = view.findViewById(R.id.message_profile_layout);
            senderMessageImage = view.findViewById(R.id.message_sender_image_view);
            receiverMessageImage = view.findViewById(R.id.message_receiver_image_view);
        }

    }

    /**
     *
     * NAME: MessageViewHolder:onBindViewHolder() - attaches the data to the view
     *
     * SYNOPSIS:  public void onBindViewHolder(final MessageViewHolder holder, int position)
     *           holder -> The ViewHolder which should be updated to represent the contents of the message at the given position in the messages data set.
     *          position -> The position of the item within the adapter's data set.
     *
     *
     * DESCRIPTION: This method updates the contents of the MessageViewHolder to reflect the item at the given position.
     *
     * RETURNS: Nothing.
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position){

        mAuth = FirebaseAuth.getInstance();
        String current_user_id = mAuth.getCurrentUser().getUid();
        Messages m = mMessageList.get(position);

        String from_user = m.getFrom();
        String message_type = m.getType();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(from_user);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image = snapshot.child("image").getValue().toString();
                Picasso.get().load(image).placeholder(R.drawable.default_image).into(holder.profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(message_type.equals("text")){



            holder.receiverMessageImage.setVisibility(View.INVISIBLE);
            holder.senderMessageImage.setVisibility(View.INVISIBLE);
            holder.receiverMessageImage.setVisibility(View.GONE);
            holder.senderMessageImage.setVisibility(View.GONE);

            if(from_user.equals(current_user_id)){
                holder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
                holder.senderMessageText.setTextColor(Color.WHITE);
                holder.senderMessageText.setText(m.getMessage());

                holder.receiverMessageText.setVisibility(View.INVISIBLE);
                holder.profileImage.setVisibility(View.INVISIBLE);
                holder.receiverMessageText.setVisibility(View.GONE);
                holder.profileImage.setVisibility(View.GONE);
                holder.senderMessageText.setVisibility(View.VISIBLE);

            }
            else
            {
                holder.senderMessageText.setVisibility(View.INVISIBLE);
                holder.senderMessageText.setVisibility(View.GONE);
                holder.profileImage.setVisibility(View.VISIBLE);
                holder.receiverMessageText.setVisibility(View.VISIBLE);

                holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
                holder.receiverMessageText.setTextColor(Color.BLACK);
                holder.receiverMessageText.setText(m.getMessage());

            }


        }
        else{




            if(from_user.equals(current_user_id)){
                Picasso.get().load(m.getMessage()).placeholder(R.drawable.default_image).into(holder.senderMessageImage);
                holder.senderMessageImage.setVisibility(View.VISIBLE);
            }
            else
            {
                Picasso.get().load(m.getMessage()).placeholder(R.drawable.default_image).into(holder.receiverMessageImage);
                holder.receiverMessageImage.setVisibility(View.VISIBLE);

            }

            holder.senderMessageText.setVisibility(View.INVISIBLE);
            holder.receiverMessageText.setVisibility(View.INVISIBLE);
            holder.senderMessageText.setVisibility(View.GONE);
            holder.receiverMessageText.setVisibility(View.GONE);
            holder.profileImage.setVisibility(View.INVISIBLE);
            holder.profileImage.setVisibility(View.GONE);


        }


    }

    /**
     *
     * NAME: MessageAdapter:onBindViewHolder() - returns the messages count
     *
     * SYNOPSIS:  public int getItemCount()
     *
     *
     * DESCRIPTION: This function returns the number of messages in the list
     *
     * RETURNS: messages count.
     *
     *  AUTHOR: Nitesh Parajuli
     *
     *  DATE 8/1/2020
     *
     */
    @Override
    public int getItemCount(){
        return mMessageList.size();
    }
}
