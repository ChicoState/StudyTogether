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
import com.studytogether.studytogether.Models.Group;
import com.studytogether.studytogether.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {
    Context mContext;
    List<Group> mGroupData;

    public GroupAdapter(Context mContext, List<Group> mGroupData) {
        this.mContext = mContext;
        this.mGroupData = mGroupData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_group_item_new,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvGroupName.setText(mGroupData.get(position).getGroupName());
        holder.tvGroupPlace.setText(mGroupData.get(position).getGroupPlace());
        holder.tvNumOfGroupMembers.setText(mGroupData.get(position).getNum_of_group_members());
        holder.tvStartTimeInput.setText(mGroupData.get(position).getStartTime());
        holder.tvEndTimeInput.setText(mGroupData.get(position).getEndTime());
        Glide.with(mContext).load(mGroupData.get(position).getGroupPicture()).into(holder.imgGroup);
        Glide.with(mContext).load(mGroupData.get(position).getGroupOwnerPhoto()).into(holder.imgOwnerProfile);
    }

    @Override
    public int getItemCount() {
        return mGroupData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupName;
        TextView tvGroupPlace;
        TextView tvNumOfGroupMembers;
        TextView tvStartTimeInput;
        TextView tvEndTimeInput;
        ImageView imgGroup;
        ImageView imgOwnerProfile;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvGroupName = itemView.findViewById(R.id.row_group_name);
            tvGroupPlace = itemView.findViewById(R.id.row_group_place);
            tvNumOfGroupMembers = itemView.findViewById(R.id.row_num_of_group_members);
            tvStartTimeInput = itemView.findViewById(R.id.row_start_time_input);
            tvEndTimeInput = itemView.findViewById(R.id.row_end_time_input);
            imgGroup = itemView.findViewById(R.id.row_group_img);
            imgOwnerProfile = itemView.findViewById(R.id.row_owner_profile_img);
        }
    }
}

    /*
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    groupListFiltered = mGroupData;
                } else {
                    List<Group> filteredList = new ArrayList<>();
                    for (Group row : mGroupData) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getGroupName().toLowerCase().contains(charString.toLowerCase()) || row.getGroupPlace().toLowerCase().contains(charString.toLowerCase()) || row.getGroupGoal().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    groupListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = groupListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                groupListFiltered = (ArrayList<Group>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public Filter getFilter() {
        return groupFilter;
    }

    private Filter groupFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Group> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(mGroupDataCopy);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Group item : mGroupDataCopy) {
                    if (item.getGroupName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mGroupData.clear();
            mGroupData.addAll( (ArrayList<Group>) filterResults.values);
            notifyDataSetChanged();
        }
    };
    */



