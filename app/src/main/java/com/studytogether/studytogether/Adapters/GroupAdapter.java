package com.studytogether.studytogether.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
import com.studytogether.studytogether.Activities.GroupChatActivity;
import com.studytogether.studytogether.Activities.GroupDetailActivity;
import com.studytogether.studytogether.Models.Course;
import com.studytogether.studytogether.Models.Group;
import com.studytogether.studytogether.Models.User;
import com.studytogether.studytogether.Models.UserGroupList;
import com.studytogether.studytogether.R;

import java.util.ArrayList;
import java.util.List;

// GroupAdapter for recyclerView
public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    // View Type
    private static final int TYPE_STUDY = 1;
    private static final int TYPE_TUTOR = 2;

    // Context
    private Context mContext;
    // Declare filteredGroup
    private List<Group> filteredGroup;

    // groupAdapter Constructor
    public GroupAdapter(Context mContext, List<Group> srcGroups) {
        this.mContext = mContext;
        this.filteredGroup = srcGroups  ;
    }

    // Get view type
    @Override
    public int getItemViewType(int position) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Take a specific group
        final Group group = filteredGroup.get(position);
        String currentGroupKey = group.getGroupKey();

        DatabaseReference groupReference = firebaseDatabase.getReference("Groups").child(currentGroupKey);
        DatabaseReference userListReference = firebaseDatabase.getReference("User").child(currentGroupKey);
        userListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot usersnap: dataSnapshot.getChildren()) {

                    User user = usersnap.getValue(User.class);

                    DatabaseReference tutorCourseListOfUserReference = firebaseDatabase.getReference("TutorCourseListOfUser").child(user.getuserId());
                    tutorCourseListOfUserReference.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot coursesnap: dataSnapshot.getChildren()) {

                                Course course = coursesnap.getValue(Course.class);
                                if(course.getSubject().equals(group.getGroupCourseSubject())) {
                                    if(String.valueOf(course.getCategoryNum()).equals(group.getGroupCourseCategoryNum())) {
                                        group.setTutorHere(true);


                                        groupReference.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().child("tutorHere").setValue(true);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });

                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            // When the database doesn't response
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        // Check the group whether it is for tutoring or not
        if (group.isTutorHere()) {
            return TYPE_TUTOR;
        } else {
            return TYPE_STUDY;
        }

    }

    // Recycler viewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Declare viewHolder and initialize to null
        RecyclerView.ViewHolder viewHolder = null;
        // Declare view and initialize to null
        View view = null;

        // Seperate view by viewType
        switch (viewType) {
            case TYPE_STUDY:
                // Inflate with row_group_item_new view
                view = LayoutInflater.from(mContext).inflate(R.layout.row_group_item_new,parent,false);
                // Pass the view into viewHolder
                viewHolder = new MyViewHolder(view);
                break;

            case TYPE_TUTOR:
                // Inflate with row_group_item_tutor view
                view = LayoutInflater.from(mContext).inflate(R.layout.row_group_tutor_here,parent,false);
                // Pass the view into viewHolder
                viewHolder = new MyViewHolderTutor(view);
                break;
        }
        return viewHolder;
    }

    // Bind viewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        // Take the specific group
        final Group group = filteredGroup.get(position);

        // Seperate holder by viewType
        // Pass the information of the group into viewHolder
        switch (holder.getItemViewType()) {
            case TYPE_STUDY:
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                myViewHolder.tvGroupName.setText(group.getGroupName());
                myViewHolder.tvGroupPlace.setText(group.getGroupPlace());
                myViewHolder.tvNumOfGroupMembers.setText(String.valueOf(group.getMaximumGroupMembers()));
                myViewHolder.tvStartTimeInput.setText(group.getStartTime());
                myViewHolder.tvEndTimeInput.setText(group.getEndTime());
                Glide.with(mContext).load(group.getGroupPicture()).into(myViewHolder.imgGroup);
                Glide.with(mContext).load(group.getGroupOwnerPhoto()).into(myViewHolder.imgOwnerProfile);
                break;
            case TYPE_TUTOR:
                MyViewHolderTutor myViewHolderTutor = (MyViewHolderTutor) holder;
                myViewHolderTutor.tvGroupName.setText(group.getGroupName());
                myViewHolderTutor.tvGroupPlace.setText(group.getGroupPlace());
                myViewHolderTutor.tvNumOfGroupMembers.setText(String.valueOf(group.getMaximumGroupMembers()));
                myViewHolderTutor.tvStartTimeInput.setText(group.getStartTime());
                myViewHolderTutor.tvEndTimeInput.setText(group.getEndTime());
                Glide.with(mContext).load(group.getGroupPicture()).into(myViewHolderTutor.imgGroup);
                Glide.with(mContext).load(group.getGroupOwnerPhoto()).into(myViewHolderTutor.imgOwnerProfile);
                break;
        }
    }

    // Counts Items
    @Override
    public int getItemCount() {
        return filteredGroup.size();
    }


    // Create myViewHolder as RecyclerView.ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // Declare all attributes as same as TYPE_STUDY view
        TextView tvGroupName;
        TextView tvGroupPlace;
        TextView tvNumOfGroupMembers;
        TextView tvStartTimeInput;
        TextView tvEndTimeInput;
        ImageView imgGroup;
        ImageView imgOwnerProfile;
        CardView cardView;


        // Create myViewHolder
        public MyViewHolder(View itemView) {
            super(itemView);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();

            // Set the attributes with each item
            tvGroupName = itemView.findViewById(R.id.row_group_name);
            tvGroupPlace = itemView.findViewById(R.id.row_group_place);
            tvNumOfGroupMembers = itemView.findViewById(R.id.row_num_of_group_members);
            tvStartTimeInput = itemView.findViewById(R.id.row_start_time_input);
            tvEndTimeInput = itemView.findViewById(R.id.row_end_time_input);
            imgGroup = itemView.findViewById(R.id.row_group_img);
            imgOwnerProfile = itemView.findViewById(R.id.row_owner_profile_img);
            cardView = itemView.findViewById(R.id.cardview_group);

            // Click Listener to touch each group
            // Progress into the groupDetail activity
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userId = firebaseUser.getUid();
                    int position = getAdapterPosition();
                    String groupKey = filteredGroup.get(position).getGroupKey();

                    DatabaseReference userGroupListReference = firebaseDatabase.getReference("UserGroupList").child(userId);
                    userGroupListReference.addValueEventListener(new ValueEventListener() {
                        Boolean alreadyJoined = false;
                        // Detect the changes
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot groupsnap: dataSnapshot.getChildren()) {

                                UserGroupList userGroupList = groupsnap.getValue(UserGroupList.class);

                                if(groupKey.equals(userGroupList.getGroupKey())) {
                                    alreadyJoined = true;
                                }
                            }
                            if(alreadyJoined) {
                                Intent groupChatActivity = new Intent(mContext, GroupChatActivity.class);
                                groupChatActivity.putExtra("GroupPosition",position);
                                groupChatActivity.putExtra("GroupKey",filteredGroup.get(position).getGroupKey());

                                groupChatActivity.putExtra("GroupCourseSubject",filteredGroup.get(position).getGroupCourseSubject());
                                groupChatActivity.putExtra("GroupCourseCategoryNum",filteredGroup.get(position).getGroupCourseCategoryNum());
                                groupChatActivity.putExtra("GroupName",filteredGroup.get(position).getGroupName());
                                groupChatActivity.putExtra("GroupGoal",filteredGroup.get(position).getGroupGoal());
                                groupChatActivity.putExtra("GroupPlace",filteredGroup.get(position).getGroupPlace());

                                groupChatActivity.putExtra("GroupStartTime",filteredGroup.get(position).getStartTime());
                                groupChatActivity.putExtra("GroupStartHour",filteredGroup.get(position).getStartHour());
                                groupChatActivity.putExtra("GroupStartMin",filteredGroup.get(position).getStartMin());

                                groupChatActivity.putExtra("GroupEndTime",filteredGroup.get(position).getEndTime());
                                groupChatActivity.putExtra("GroupEndHour",filteredGroup.get(position).getEndHour());
                                groupChatActivity.putExtra("GroupEndMin",filteredGroup.get(position).getEndMin());

                                groupChatActivity.putExtra("GroupMaxMembers",filteredGroup.get(position).getMaximumGroupMembers());
                                groupChatActivity.putExtra("GroupCurrentMembers",filteredGroup.get(position).getCurrentGroupMembers());

                                groupChatActivity.putExtra("GroupOwnerId",filteredGroup.get(position).getOwnerId());
                                groupChatActivity.putExtra("GroupOwnerPhoto",filteredGroup.get(position).getGroupOwnerPhoto());

                                groupChatActivity.putExtra("GroupPicture",filteredGroup.get(position).getGroupPicture());
                                groupChatActivity.putExtra("groupKey",filteredGroup.get(position).getGroupKey());
                                long timestamp = (long) filteredGroup.get(position).getTimeStamp();
                                groupChatActivity.putExtra("addedDate", timestamp);

                                // start the GroupDetailActivity
                                mContext.startActivity(groupChatActivity);
                            }
                            else {
                                Intent groupDetailActivity = new Intent(mContext, GroupDetailActivity.class);
                                groupDetailActivity.putExtra("GroupPosition",position);
                                groupDetailActivity.putExtra("GroupKey",filteredGroup.get(position).getGroupKey());

                                groupDetailActivity.putExtra("GroupCourseSubject",filteredGroup.get(position).getGroupCourseSubject());
                                groupDetailActivity.putExtra("GroupCourseCategoryNum",filteredGroup.get(position).getGroupCourseCategoryNum());
                                groupDetailActivity.putExtra("GroupName",filteredGroup.get(position).getGroupName());
                                groupDetailActivity.putExtra("GroupGoal",filteredGroup.get(position).getGroupGoal());
                                groupDetailActivity.putExtra("GroupPlace",filteredGroup.get(position).getGroupPlace());

                                groupDetailActivity.putExtra("GroupStartTime",filteredGroup.get(position).getStartTime());
                                groupDetailActivity.putExtra("GroupStartHour",filteredGroup.get(position).getStartHour());
                                groupDetailActivity.putExtra("GroupStartMin",filteredGroup.get(position).getStartMin());

                                groupDetailActivity.putExtra("GroupEndTime",filteredGroup.get(position).getEndTime());
                                groupDetailActivity.putExtra("GroupEndHour",filteredGroup.get(position).getEndHour());
                                groupDetailActivity.putExtra("GroupEndMin",filteredGroup.get(position).getEndMin());

                                groupDetailActivity.putExtra("GroupMaxMembers",filteredGroup.get(position).getMaximumGroupMembers());
                                groupDetailActivity.putExtra("GroupCurrentMembers",filteredGroup.get(position).getCurrentGroupMembers());

                                groupDetailActivity.putExtra("GroupOwnerId",filteredGroup.get(position).getOwnerId());
                                groupDetailActivity.putExtra("GroupOwnerPhoto",filteredGroup.get(position).getGroupOwnerPhoto());

                                groupDetailActivity.putExtra("GroupPicture",filteredGroup.get(position).getGroupPicture());
                                groupDetailActivity.putExtra("groupKey",filteredGroup.get(position).getGroupKey());
                                long timestamp = (long) filteredGroup.get(position).getTimeStamp();
                                groupDetailActivity.putExtra("addedDate", timestamp);

                                // start the GroupDetailActivity
                                mContext.startActivity(groupDetailActivity);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });





                }
            });
        }
    }

    // Create myViewHolderTutor as RecyclerView.ViewHolder
    public class MyViewHolderTutor extends RecyclerView.ViewHolder {
        // Declare all attributes as same as TYPE_TUTOR view
        TextView tvGroupName;
        TextView tvGroupPlace;
        TextView tvNumOfGroupMembers;
        TextView tvStartTimeInput;
        TextView tvEndTimeInput;
        ImageView imgGroup;
        ImageView imgOwnerProfile;
        ImageView imgTutorHere;
        CardView cardView;


        // Create myViewHolderTutor
        public MyViewHolderTutor(View itemView) {
            super(itemView);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();

            // Set the attributes with each item
            tvGroupName = itemView.findViewById(R.id.row_group_name);
            tvGroupPlace = itemView.findViewById(R.id.row_group_place);
            tvNumOfGroupMembers = itemView.findViewById(R.id.row_num_of_group_members);
            tvStartTimeInput = itemView.findViewById(R.id.row_start_time_input);
            tvEndTimeInput = itemView.findViewById(R.id.row_end_time_input);
            imgGroup = itemView.findViewById(R.id.row_group_img);
            imgOwnerProfile = itemView.findViewById(R.id.row_owner_profile_img);
            imgTutorHere = itemView.findViewById(R.id.row_tutor_here_image);
            cardView = itemView.findViewById(R.id.cardview_group);

            // Click Listener to touch each group
            // Progress into the groupDetail activity
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String userId = firebaseUser.getUid();
                    int position = getAdapterPosition();
                    String groupKey = filteredGroup.get(position).getGroupKey();

                    DatabaseReference userGroupListReference = firebaseDatabase.getReference("UserGroupList").child(userId);
                    userGroupListReference.addValueEventListener(new ValueEventListener() {
                        Boolean alreadyJoined = false;
                        // Detect the changes
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot groupsnap: dataSnapshot.getChildren()) {

                                UserGroupList userGroupList = groupsnap.getValue(UserGroupList.class);

                                if(groupKey.equals(userGroupList.getGroupKey())) {
                                    alreadyJoined = true;
                                }
                            }
                            if(alreadyJoined) {
                                Intent groupChatActivity = new Intent(mContext, GroupChatActivity.class);
                                groupChatActivity.putExtra("GroupPosition",position);
                                groupChatActivity.putExtra("GroupKey",filteredGroup.get(position).getGroupKey());

                                groupChatActivity.putExtra("GroupCourseSubject",filteredGroup.get(position).getGroupCourseSubject());
                                groupChatActivity.putExtra("GroupCourseCategoryNum",filteredGroup.get(position).getGroupCourseCategoryNum());
                                groupChatActivity.putExtra("GroupName",filteredGroup.get(position).getGroupName());
                                groupChatActivity.putExtra("GroupGoal",filteredGroup.get(position).getGroupGoal());
                                groupChatActivity.putExtra("GroupPlace",filteredGroup.get(position).getGroupPlace());

                                groupChatActivity.putExtra("GroupStartTime",filteredGroup.get(position).getStartTime());
                                groupChatActivity.putExtra("GroupStartHour",filteredGroup.get(position).getStartHour());
                                groupChatActivity.putExtra("GroupStartMin",filteredGroup.get(position).getStartMin());

                                groupChatActivity.putExtra("GroupEndTime",filteredGroup.get(position).getEndTime());
                                groupChatActivity.putExtra("GroupEndHour",filteredGroup.get(position).getEndHour());
                                groupChatActivity.putExtra("GroupEndMin",filteredGroup.get(position).getEndMin());

                                groupChatActivity.putExtra("GroupMaxMembers",filteredGroup.get(position).getMaximumGroupMembers());
                                groupChatActivity.putExtra("GroupCurrentMembers",filteredGroup.get(position).getCurrentGroupMembers());

                                groupChatActivity.putExtra("GroupOwnerId",filteredGroup.get(position).getOwnerId());
                                groupChatActivity.putExtra("GroupOwnerPhoto",filteredGroup.get(position).getGroupOwnerPhoto());

                                groupChatActivity.putExtra("GroupPicture",filteredGroup.get(position).getGroupPicture());
                                groupChatActivity.putExtra("groupKey",filteredGroup.get(position).getGroupKey());
                                long timestamp = (long) filteredGroup.get(position).getTimeStamp();
                                groupChatActivity.putExtra("addedDate", timestamp);

                                // start the GroupDetailActivity
                                mContext.startActivity(groupChatActivity);
                            }
                            else {
                                Intent groupDetailActivity = new Intent(mContext, GroupDetailActivity.class);
                                groupDetailActivity.putExtra("GroupPosition",position);
                                groupDetailActivity.putExtra("GroupKey",filteredGroup.get(position).getGroupKey());

                                groupDetailActivity.putExtra("GroupCourseSubject",filteredGroup.get(position).getGroupCourseSubject());
                                groupDetailActivity.putExtra("GroupCourseCategoryNum",filteredGroup.get(position).getGroupCourseCategoryNum());
                                groupDetailActivity.putExtra("GroupName",filteredGroup.get(position).getGroupName());
                                groupDetailActivity.putExtra("GroupGoal",filteredGroup.get(position).getGroupGoal());
                                groupDetailActivity.putExtra("GroupPlace",filteredGroup.get(position).getGroupPlace());

                                groupDetailActivity.putExtra("GroupStartTime",filteredGroup.get(position).getStartTime());
                                groupDetailActivity.putExtra("GroupStartHour",filteredGroup.get(position).getStartHour());
                                groupDetailActivity.putExtra("GroupStartMin",filteredGroup.get(position).getStartMin());

                                groupDetailActivity.putExtra("GroupEndTime",filteredGroup.get(position).getEndTime());
                                groupDetailActivity.putExtra("GroupEndHour",filteredGroup.get(position).getEndHour());
                                groupDetailActivity.putExtra("GroupEndMin",filteredGroup.get(position).getEndMin());

                                groupDetailActivity.putExtra("GroupMaxMembers",filteredGroup.get(position).getMaximumGroupMembers());
                                groupDetailActivity.putExtra("GroupCurrentMembers",filteredGroup.get(position).getCurrentGroupMembers());

                                groupDetailActivity.putExtra("GroupOwnerId",filteredGroup.get(position).getOwnerId());
                                groupDetailActivity.putExtra("GroupOwnerPhoto",filteredGroup.get(position).getGroupOwnerPhoto());

                                groupDetailActivity.putExtra("GroupPicture",filteredGroup.get(position).getGroupPicture());
                                groupDetailActivity.putExtra("groupKey",filteredGroup.get(position).getGroupKey());
                                long timestamp = (long) filteredGroup.get(position).getTimeStamp();
                                groupDetailActivity.putExtra("addedDate", timestamp);

                                // start the GroupDetailActivity
                                mContext.startActivity(groupDetailActivity);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            });
        }
    }
}