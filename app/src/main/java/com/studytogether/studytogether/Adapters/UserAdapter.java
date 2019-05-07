package com.studytogether.studytogether.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.studytogether.studytogether.Models.Course;
import com.studytogether.studytogether.Models.Group;
import com.studytogether.studytogether.Models.User;
import com.studytogether.studytogether.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<User> users;

    public UserAdapter(Context mContext, List<User> users) {
        this.mContext = mContext;
        this.users = users;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Declare viewHolder and initialize to null
        RecyclerView.ViewHolder viewHolder = null;
        // Declare view and initialize to null
        View view = null;

        // Inflate with row_group_item_new view
        view = LayoutInflater.from(mContext).inflate(R.layout.row_user,parent,false);
        // Pass the view into viewHolder
        viewHolder = new UserViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final User user = users.get(position);


        UserViewHolder userViewHolder = (UserViewHolder) holder;

        if(user != null) {
            if(user.getuserName() != null && user.getUserEmail() != null && user.getuserImage() != null) {
                userViewHolder.tvUserUserName.setText(user.getuserName());
                userViewHolder.tvUserUserEmail.setText(user.getUserEmail());
                userViewHolder.tvUserUserPosition.setText(user.getUserPosition());
                Glide.with(mContext).load(user.getuserImage()).into(userViewHolder.imgUserUserPhoto);
            }
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // Create myViewHolder as RecyclerView.ViewHolder
    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserUserName;
        TextView tvUserUserEmail;
        TextView tvUserUserPosition;
        ImageView imgUserUserPhoto;



        // Create myViewHolder
        public UserViewHolder(View itemView) {
            super(itemView);

            // Set the attributes with each item
            tvUserUserName = itemView.findViewById(R.id.row_big_user_name);
            tvUserUserEmail = itemView.findViewById(R.id.row_user_email);
            tvUserUserPosition = itemView.findViewById(R.id.row_user_position);
            imgUserUserPhoto = itemView.findViewById(R.id.row_user_image);

        }
    }
}
