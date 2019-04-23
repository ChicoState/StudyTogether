package com.studytogether.studytogether.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.studytogether.studytogether.Adapters.UserAdapter;
import com.studytogether.studytogether.Models.Group;
import com.studytogether.studytogether.Models.GroupChat;
import com.studytogether.studytogether.Models.User;
import com.studytogether.studytogether.Models.UserGroupList;
import com.studytogether.studytogether.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

// GroupDetail Activity
public class GroupDetailActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    // Create items
    TextView detailGroupName, detailGroupPlace, detailGroupGoal, detailGroupAddedDate;
    Button detailJoinButton;

    List<User> userList;

    Boolean alreadyJoined;

    private RecyclerView userRecyclerView;
    private RecyclerView.Adapter userAdapter;
    private RecyclerView.LayoutManager userLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the contentView with group_detail
        setContentView(R.layout.group_detail);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();


        // Set up items with layouts
        detailGroupName = findViewById(R.id.detail_group_name);
        detailGroupPlace = findViewById(R.id.detail_group_place);
        detailGroupGoal = findViewById(R.id.detail_group_goal);
        detailGroupAddedDate = findViewById(R.id.detail_group_added);

        detailJoinButton = findViewById(R.id.detail_join_btn);

        userRecyclerView  = findViewById(R.id.userRV);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        userLayoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(userLayoutManager);


        // Create intent
        Intent intent = getIntent();
        // Get info through intent
        String groupName = intent.getExtras().getString("GroupName");
        String groupPlace = intent.getExtras().getString("GroupPlace");
        String groupGoal = intent.getExtras().getString("GroupGoal");
        String tutor = intent.getExtras().getString("tutor");
        String numOfGroupMember = intent.getExtras().getString("numOfGroupMember");
        String startTime = intent.getExtras().getString("startTime");
        String endTime = intent.getExtras().getString("endTime");
        String groupPicture = getIntent().getStringExtra("GroupPicture");
        String ownerId = intent.getExtras().getString("OwnerId");
        String groupOwnerPhoto = intent.getStringExtra("GroupOwnerPhoto");
        String groupKey = intent.getExtras().getString("GroupKey");
        String groupCreated = timestampToString(getIntent().getExtras().getLong("addedDate"));

        String userId = firebaseUser.getUid();


        final Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.detail_collapsing_toolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.toolbar_text);
        collapsingToolbar.setTitle(groupName);

        final ImageView imageView = findViewById(R.id.detail_backdrop);
        Glide.with(this).load(groupPicture).apply(RequestOptions.centerCropTransform()).into(imageView);


        // Set items with info
        detailGroupName.setText(groupName);
        detailGroupPlace.setText(groupPlace);
        detailGroupGoal.setText(groupGoal);
        detailGroupAddedDate.setText(groupCreated);


        updateUser(groupKey);




        alreadyJoined = false;


        DatabaseReference userGroupListReference = firebaseDatabase.getReference("UserGroupList").child(userId);
        userGroupListReference.addValueEventListener(new ValueEventListener() {
            // Detect the changes
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> groupList = new ArrayList<>();
                for (DataSnapshot groupsnap: dataSnapshot.getChildren()) {

                    UserGroupList userGroupList = groupsnap.getValue(UserGroupList.class);
                    showMessage(userGroupList.getGroupKey());

                    if(groupKey.equals(userGroupList.getGroupKey())) {
                        alreadyJoined = true;
                    }
                }
                showMessage("alreadyjoined is "+ alreadyJoined);
                if(alreadyJoined) {
                    detailJoinButton.setVisibility(View.INVISIBLE);
                    showMessage("You are already joined the group");
                }
                else {
                    showMessage("Come on in!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




        detailJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference userReference = firebaseDatabase.getReference("User").child(groupKey).push();

                DatabaseReference groupReference = firebaseDatabase.getReference("Groups").child(groupKey);

                DatabaseReference userGroupListReference = firebaseDatabase.getReference("UserGroupList").child(userId).push();

                showMessage("Welcome!");
                String userEmail = firebaseUser.getEmail();
                String userId = firebaseUser.getUid();
                String userName = firebaseUser.getDisplayName();
                String userImage = firebaseUser.getPhotoUrl().toString();
                User user = new User(userEmail,userId,userImage,userName);

                userReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        detailJoinButton.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("fail to add comment : "+e.getMessage());
                    }
                });


                Group group = new Group(groupName, groupGoal, groupPlace, tutor, numOfGroupMember, startTime, endTime, groupPicture, ownerId, groupOwnerPhoto );
                UserGroupList userGroupList = new UserGroupList(group, groupKey);

                userGroupListReference.setValue(userGroupList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        detailJoinButton.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("fail to add comment : "+e.getMessage());
                    }
                });
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    private void updateUser(String groupKey) {
        // Take a reference of User
        DatabaseReference userReference = firebaseDatabase.getReference("User").child(groupKey);


        // specify an adapter
        userReference.addValueEventListener(new ValueEventListener() {
            // Detect the changes
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Reinitialize the userList
                userList = new ArrayList<>();
                // Loop whole comments
                for (DataSnapshot usersnap: dataSnapshot.getChildren()) {

                    User user = usersnap.getValue(User.class);
                    // Add user
                    userList.add(user) ;
                }

                // Set recyclerView using userAdapter
                userAdapter = new UserAdapter(GroupDetailActivity.this,userList);
                userRecyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    // Get server time and convert into String
    private String timestampToString(long time) {

        // Get time
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        // Reform the time and cast into String type
        String date = DateFormat.format("MM-dd-yyyy",calendar).toString();
        return date;
    }

    // Show message to the user
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

}
