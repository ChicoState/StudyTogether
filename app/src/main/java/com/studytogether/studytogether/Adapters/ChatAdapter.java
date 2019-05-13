package com.studytogether.studytogether.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.studytogether.studytogether.Models.Course;
import com.studytogether.studytogether.Models.GroupChat;
import com.studytogether.studytogether.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    private static final int TYPE_OTHERS_CHAT = 1;
    private static final int TYPE_MY_CHAT = 2;

    private Context mContext;
    // Declare groupChats
    private List<GroupChat> groupChats;

    // ChatAdapter Constructor
    public ChatAdapter(Context mContext, List<GroupChat> groupChats) {
        this.mContext = mContext;
        this.groupChats = groupChats;
    }


    // Get view type
    @Override
    public int getItemViewType(int position) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        String currentUserId = firebaseUser.getUid();

        // Take a specific group
        final GroupChat groupChat = groupChats.get(position);
        String groupKey = groupChat.getGroupKey();
        DatabaseReference userChatListReference = firebaseDatabase.getReference("GroupChat").child(groupKey);


        userChatListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot commentsnap: dataSnapshot.getChildren()) {

                    GroupChat groupChat = commentsnap.getValue(GroupChat.class);
                    if(groupChat.getuserId().equals(currentUserId)) {

                        //userChatListReference.child(commentsnap.getKey()).child("myComment").setValue(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        /*

        if (!groupChat.isMyComment()) {
            return TYPE_OTHERS_CHAT;
        } else {
            return TYPE_MY_CHAT;
        }
        */
        return TYPE_OTHERS_CHAT;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Declare viewHolder and initialize to null
        RecyclerView.ViewHolder viewHolder = null;
        // Declare view and initialize to null
        View view = null;

        switch (viewType) {
            case TYPE_OTHERS_CHAT:
                view = LayoutInflater.from(mContext).inflate(R.layout.row_comment,parent,false);
                // Pass the view into viewHolder
                viewHolder = new ChatOthersViewHolder(view);
                break;

            case TYPE_MY_CHAT:
                view = LayoutInflater.from(mContext).inflate(R.layout.row_my_chat,parent,false);
                // Pass the view into viewHolder
                viewHolder = new ChatMyViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final GroupChat groupChat = groupChats.get(position);

        switch (holder.getItemViewType()) {
            case TYPE_OTHERS_CHAT:
                ChatOthersViewHolder chatOhtersViewHolder = (ChatOthersViewHolder) holder;

                chatOhtersViewHolder.tvChatUserName.setText(groupChat.getuserName());
                chatOhtersViewHolder.tvChatUserComment.setText(groupChat.getContent());
                Glide.with(mContext).load(groupChat.getuserImage()).into(chatOhtersViewHolder.imgChatUser);
                break;
            case TYPE_MY_CHAT:
                ChatMyViewHolder chatMyViewHolder = (ChatMyViewHolder) holder;

                chatMyViewHolder.tvChatUserComment.setText(groupChat.getContent());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return groupChats.size();
    }

    // Create myViewHolder as RecyclerView.ViewHolder
    public class ChatOthersViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatUserName;
        TextView tvChatUserComment;
        ImageView imgChatUser;



        // Create myViewHolder
        public ChatOthersViewHolder(View itemView) {
            super(itemView);

            // Set the attributes with each item
            tvChatUserName = itemView.findViewById(R.id.row_user_name);
            tvChatUserComment = itemView.findViewById(R.id.row_user_comment);
            imgChatUser = itemView.findViewById(R.id.row_user_img);

        }
    }

    public class ChatMyViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatUserComment;



        // Create myViewHolder
        public ChatMyViewHolder(View itemView) {
            super(itemView);

            // Set the attributes with each item
            tvChatUserComment = itemView.findViewById(R.id.row_my_comment);

        }
    }
}
