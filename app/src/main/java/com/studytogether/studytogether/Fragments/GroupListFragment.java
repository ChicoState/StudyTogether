package com.studytogether.studytogether.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.studytogether.studytogether.Adapters.GroupAdapter;
import com.studytogether.studytogether.Models.Group;
import com.studytogether.studytogether.Models.UserGroupList;
import com.studytogether.studytogether.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupListFragment extends Fragment {

    // Declare a interaction listener for URI
    private GroupListFragment.OnFragmentInteractionListener mListener;

    // Declare a searchView for the filter at appBar
    private SearchView searchView = null;
    // Declare a queryTextListener to take a query from a User
    private SearchView.OnQueryTextListener queryTextListener;

    // Declare to set up the groupAdapter
    RecyclerView groupRecyclerView;
    // Declare the groupAdapter, and it holds groupList
    // The groupAdapter needs for this fragment because it's groupList will be showed in TutorFragment
    GroupAdapter groupAdapter;
    // Declare to hold groups
    List<Group> myGroupList;

    // Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userGroupListReference;
    DatabaseReference groupReference ;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public GroupListFragment() {
    }

    // OnCreate for the first call of this fragment of activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        setHasOptionsMenu(true);
    }

/*
    // Search Filter, working on appBar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Get groupList from the database
        updateList();
        // Get the menu item: search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        // Set up searchManager
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        // Check the item condition
        if (searchItem != null) {
            // If item is null, reset the item
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            // Connect the searchView item with searchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            // Get a query from a User
            queryTextListener = new SearchView.OnQueryTextListener() {
                // onDataChange detect user's query as real time
                @Override
                public boolean onQueryTextChange(final String query) {
                    // Check groupAdapter
                    if (groupAdapter == null) {
                        Toast.makeText(getContext(), "GroupAdapter is null", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    //Filtering groupList
                    //User can search by group's name, group's place, and group's goal in Tutor lists
                    userGroupListReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            // Reset the groupList
                            myGroupList = new ArrayList<>();

                            // Take groupList from database and loop through whole groups
                            for (DataSnapshot userGroupListSnap: dataSnapshot.getChildren()) {
                                UserGroupList userGroupList = userGroupListSnap.getValue(UserGroupList.class);
                                String groupName = userGroupList.getGroup().getGroupName();
                                String groupGoal = userGroupList.getGroup().getGroupGoal();
                                String groupPlace = userGroupList.getGroup().getGroupPlace();


                                // If groupName, groupPlace, or groupGoal holds query
                                if (groupName.toLowerCase().contains(query.toLowerCase()) || groupGoal.toLowerCase().contains(query.toLowerCase()) || groupPlace.toLowerCase().contains(query.toLowerCase())) {
                                    myGroupList.add(userGroupList.getGroup());
                                }
                            }
                            // Reverse the groupList to see the recently added groups on top
                            Collections.reverse(myGroupList);

                            // Set recyclerView through groupAdapter
                            groupAdapter = new GroupAdapter(getActivity(),myGroupList);
                            groupRecyclerView.setAdapter(groupAdapter);
                        }

                        // When the database doesn't response
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    return true;
                }
                // This function is called when a user press the return button after type some query
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }
            };
            // Pass the query into searchView
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
    */


    // Option for items on appBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return false;
            default:
                break;
        }
        // Pass the query into searchView
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){

        View fragmentView = inflater.inflate(R.layout.group_list, viewGroup, false);

        // Set up recyclerView
        groupRecyclerView  = fragmentView.findViewById(R.id.my_group_listRV);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        groupRecyclerView.setHasFixedSize(true);

        // Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String userId = firebaseUser.getUid();

        // Take a reference of
        groupReference = firebaseDatabase.getReference("Groups");
        userGroupListReference = firebaseDatabase.getReference("UserGroupList").child(userId);

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get List Posts from the database
        updateList();
    }

    // Update groupList
    private void updateList() {
        // Update groupList when a group is added
        userGroupListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Reinitialize the groupList
                myGroupList = new ArrayList<>();
                // Loop whole groups
                for (DataSnapshot userGroupListSnap: dataSnapshot.getChildren()) {

                    UserGroupList userGroupList = userGroupListSnap.getValue(UserGroupList.class);
                    String groupKey = userGroupList.getGroupKey();


                    List<Group> groupList = new ArrayList<>();
                    groupReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Loop whole groups
                            for (DataSnapshot groupsnap: dataSnapshot.getChildren()) {

                                Group group = groupsnap.getValue(Group.class);
                                if(groupKey.contains(group.getGroupKey())) {
                                    myGroupList.add(group);
                                }
                            }
                            groupAdapter = new GroupAdapter(getActivity(),myGroupList);
                            groupRecyclerView.setAdapter(groupAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });


                }
                // Reverse the groupList to see the recently added groups on top
                Collections.reverse(myGroupList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    // Show message to the user
    private void showMessage(String text) {
        Toast.makeText(getContext(),text,Toast.LENGTH_LONG).show();
    }

}