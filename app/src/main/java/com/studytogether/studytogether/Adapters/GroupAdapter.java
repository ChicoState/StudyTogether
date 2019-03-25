package com.studytogether.studytogether.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.studytogether.studytogether.Activities.GroupDetailActivity;
import com.studytogether.studytogether.Models.Group;
import com.studytogether.studytogether.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> implements Filterable{
    private Context mContext;
    private List<Group> srcGroups;
    private List<Group> filteredGroup;

    public GroupAdapter(Context mContext, List<Group> srcGroups) {
        this.mContext = mContext;
        this.srcGroups = srcGroups;
        this.filteredGroup = srcGroups  ;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_group_item_new,parent,false);
        View row_tutor = LayoutInflater.from(mContext).inflate(R.layout.row_group_item_new,parent,false);
        final MyViewHolder viewHolder = new MyViewHolder(row);
        final MyViewHolder tutorViewHolder = new MyViewHolder(row_tutor);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Group group = filteredGroup.get(position);
        holder.tvGroupName.setText(group.getGroupName());
        holder.tvGroupPlace.setText(group.getGroupPlace());
        holder.tvNumOfGroupMembers.setText(group.getNum_of_group_members());
        holder.tvStartTimeInput.setText(group.getStartTime());
        holder.tvEndTimeInput.setText(group.getEndTime());
        Glide.with(mContext).load(group.getGroupPicture()).into(holder.imgGroup);
        Glide.with(mContext).load(group.getGroupOwnerPhoto()).into(holder.imgOwnerProfile);

        /*
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "Group Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, GroupDetailActivity.class);
                int position = getAdapterPosition();

                // passing data to the GroupDetailActivity
                intent.putExtra("GroupName",filteredGroup.get(position).getGroupName());
                intent.putExtra("GroupPlace",filteredGroup.get(position).getGroupPlace());
                intent.putExtra("GroupGoal",filteredGroup.get(position).getGroupGoal());
                intent.putExtra("GroupImg",filteredGroup.get(position).getGroupPicture());
                // start the GroupDetailActivity
                mContext.startActivity(intent);
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return filteredGroup.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchString = charSequence.toString();
                if (searchString.isEmpty()) {
                    filteredGroup = srcGroups;
                }
                else {
                    List<Group> resultList = new ArrayList<>();
                    if (srcGroups.isEmpty()) {
                        Toast.makeText(mContext, "srcGroups is empty", Toast.LENGTH_LONG).show();
                    }
                    for (Group group : srcGroups) {
                        if (group.getGroupName().toLowerCase().contains(searchString.toLowerCase())) {
                            resultList.add(group);
                        }
                    }
                    filteredGroup = resultList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredGroup;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredGroup.clear();
                filteredGroup = (ArrayList<Group>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupName;
        TextView tvGroupPlace;
        TextView tvNumOfGroupMembers;
        TextView tvStartTimeInput;
        TextView tvEndTimeInput;
        ImageView imgGroup;
        ImageView imgOwnerProfile;
        CardView cardView;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvGroupName = itemView.findViewById(R.id.row_group_name);
            tvGroupPlace = itemView.findViewById(R.id.row_group_place);
            tvNumOfGroupMembers = itemView.findViewById(R.id.row_num_of_group_members);
            tvStartTimeInput = itemView.findViewById(R.id.row_start_time_input);
            tvEndTimeInput = itemView.findViewById(R.id.row_end_time_input);
            imgGroup = itemView.findViewById(R.id.row_group_img);
            imgOwnerProfile = itemView.findViewById(R.id.row_owner_profile_img);
            cardView = itemView.findViewById(R.id.cardview_group);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent groupDetailActivity = new Intent(mContext, GroupDetailActivity.class);
                    int position = getAdapterPosition();

                    // passing data to the GroupDetailActivity
                    groupDetailActivity.putExtra("GroupName",filteredGroup.get(position).getGroupName());
                    groupDetailActivity.putExtra("GroupPlace",filteredGroup.get(position).getGroupPlace());
                    groupDetailActivity.putExtra("GroupGoal",filteredGroup.get(position).getGroupGoal());
                    groupDetailActivity.putExtra("GroupImg",filteredGroup.get(position).getGroupPicture());
                    long timestamp = (long) filteredGroup.get(position).getTimeStamp();
                    groupDetailActivity.putExtra("addedDate", timestamp);
                    // start the GroupDetailActivity
                    mContext.startActivity(groupDetailActivity);
                }
            });
        }
    }

    private GroupAdapter.ClickListener mClickListener;

    //Interface to send Callbacks
    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(GroupAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

}