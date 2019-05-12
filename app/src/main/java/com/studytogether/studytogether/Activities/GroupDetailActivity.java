package com.studytogether.studytogether.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.BoringLayout;
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
import com.studytogether.studytogether.Adapters.GroupAdapter;
import com.studytogether.studytogether.Adapters.UserAdapter;
import com.studytogether.studytogether.Models.Course;
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
    TextView detailGroupName, detailGroupPlace, detailGroupGoal, detailGroupAddedDate, detailTerminateDescription;
    TextView detailCourseSubject, detailCategoryNum, detailCourseDescription;
    TextView detailStartTime, detailEndtime;
    TextView detailMaxMembers, detailCurrentMembers;
    Button detailJoinButton, detailQuitButton, detailTerminateGroupButton;

    List<User> userList;
    private boolean ownerPosition = false;
    private boolean tutorPosition = false;


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
        detailTerminateDescription = findViewById(R.id.detail_terminate_description);
        detailCourseSubject = findViewById(R.id.detail_course_subject);
        detailCategoryNum = findViewById(R.id.detail_course_category_num);
        detailCourseDescription = findViewById(R.id.detail_course_description);
        detailStartTime = findViewById(R.id.detail_start_time);
        detailEndtime = findViewById(R.id.detail_end_time);
        detailMaxMembers = findViewById(R.id.detail_maximum_members);
        detailCurrentMembers = findViewById(R.id.detail_current_members);

        detailJoinButton = findViewById(R.id.detail_join_btn);
        detailQuitButton = findViewById(R.id.detail_quit_btn);
        detailTerminateGroupButton = findViewById(R.id.detail_terminate_btn);
        detailJoinButton.setVisibility(View.GONE);
        detailQuitButton.setVisibility(View.GONE);
        detailTerminateGroupButton.setVisibility(View.GONE);
        detailTerminateDescription.setVisibility(View.GONE);

        userRecyclerView  = findViewById(R.id.userRV);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        userLayoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(userLayoutManager);


        // Create intent
        Intent intent = getIntent();
        // Get info through intent
        String courseSubject = intent.getExtras().getString("GroupCourseSubject");
        String courseCategoryNum = intent.getExtras().getString("GroupCourseCategoryNum");

        String groupName = intent.getExtras().getString("GroupName");
        String groupPlace = intent.getExtras().getString("GroupPlace");
        String groupGoal = intent.getExtras().getString("GroupGoal");


        String startTime = intent.getExtras().getString("GroupStartTime");
        int startHour = intent.getExtras().getInt("GroupStartHour");
        int startMin = intent.getExtras().getInt("GroupStartMin");

        String endTime = intent.getExtras().getString("GroupEndTime");
        int endHour = intent.getExtras().getInt("GroupEndHour");
        int endMin = intent.getExtras().getInt("GroupEndMin");

        int groupMaxMembers = intent.getExtras().getInt("GroupMaxMembers");
        int groupCurrentMembers = intent.getExtras().getInt("GroupCurrentMembers");

        String groupOwnerName = intent.getExtras().getString("GroupOwnerName");
        String ownerId = intent.getExtras().getString("GroupOwnerId");
        String groupOwnerPhoto = intent.getStringExtra("GroupOwnerPhoto");

        String groupPicture = getIntent().getStringExtra("GroupPicture");

        String groupKey = intent.getExtras().getString("GroupKey");
        String groupCreated = timestampToString(getIntent().getExtras().getLong("addedDate"));

        String userId = firebaseUser.getUid();
        String userName = firebaseUser.getDisplayName();


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
        detailCourseSubject.setText(courseSubject);
        detailCategoryNum.setText(courseCategoryNum);
        detailStartTime.setText(startTime);
        detailEndtime.setText(endTime);
        detailMaxMembers.setText(String.valueOf(groupMaxMembers));
        detailCurrentMembers.setText(String.valueOf(groupCurrentMembers));



        DatabaseReference courseReference = firebaseDatabase.getReference("Course");
        DatabaseReference userGroupListReference = firebaseDatabase.getReference("UserGroupList").child(userId);

        DatabaseReference userReference = firebaseDatabase.getReference("User").child(groupKey);

        DatabaseReference groupsReference = firebaseDatabase.getReference("Groups");
        DatabaseReference groupReference = firebaseDatabase.getReference("Groups").child(groupKey);

        DatabaseReference userPushReference = firebaseDatabase.getReference("User").child(groupKey).push();

        DatabaseReference userGroupListPushReference = firebaseDatabase.getReference("UserGroupList").child(userId).push();
        DatabaseReference tutorCourseListOfUser = firebaseDatabase.getReference("TutorCourseListOfUser").child(userId);

        DatabaseReference tutorCourseListOfUserReference = firebaseDatabase.getReference("TutorCourseListOfUser").child(userId);





        DatabaseReference usersReference = firebaseDatabase.getReference("User");

        DatabaseReference userGroupListsReference = firebaseDatabase.getReference("UserGroupList").child(userId);

        DatabaseReference groupChatReference = firebaseDatabase.getReference("GroupChat");







        // specify an adapter
        courseReference.addValueEventListener(new ValueEventListener() {
            // Detect the changes
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Loop whole comments
                for (DataSnapshot coursesnap: dataSnapshot.getChildren()) {

                    Course course = coursesnap.getValue(Course.class);
                    if(course.getSubject().equals(courseSubject) && String.valueOf(course.getCategoryNum()).equals(courseCategoryNum)) {
                        detailCourseDescription.setText(course.getCourseTitle());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        updateUser(groupKey);



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

                buttonController(alreadyJoined, firebaseUser.getUid(), groupKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




        detailJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group group = new Group(courseSubject, courseCategoryNum ,groupName, groupGoal, groupPlace, groupMaxMembers, startHour, startMin, endHour, endMin, groupPicture, ownerId, groupOwnerPhoto, groupOwnerName );

                if((group.getMaximumGroupMembers() <= group.getCurrentGroupMembers())) {
                    showMessage("The group is FULL, SORRY");
                } else {


                    String userEmail = firebaseUser.getEmail();
                    String userId = firebaseUser.getUid();
                    String userName = firebaseUser.getDisplayName();
                    String userImage = firebaseUser.getPhotoUrl().toString();





                    groupsReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for (DataSnapshot groupsnap: dataSnapshot.getChildren()) {
                                Group group = groupsnap.getValue(Group.class);
                                if(group.getGroupKey().equals(groupKey)) {
                                    if(group.getOwnerId().equals(userId)) {
                                        ownerPosition = true;
                                    }
                                    if(tutorCourseListOfUser != null) {
                                        tutorCourseListOfUser.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot coursesnap: dataSnapshot.getChildren()) {
                                                    Course course = coursesnap.getValue(Course.class);
                                                    if(course.getSubject().equals(group.getGroupCourseSubject()) && String.valueOf(course.getCategoryNum()).equals(group.getGroupCourseCategoryNum())) {
                                                        tutorPosition = true;

                                                    }
                                                }
                                            }
                                            // When the database doesn't response
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }
                            }
                            String position = "";

                            if(ownerPosition && tutorPosition) {
                                position = "Owner & Tutor";
                            } else if(ownerPosition) {
                                position = "Owner";
                            } else if(tutorPosition) {
                                position = "Tutor";
                            }

                            /*
                            User user = new User(userEmail,userId,userImage,userName, groupKey, position);


                            userPushReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //showMessage("Successfully added");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showMessage("fail to add comment : "+e.getMessage());
                                }
                            });
                            */
                        }
                        // When the database doesn't response
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                    User user = new User(userEmail,userId,userImage,userName, groupKey, "");


                    userPushReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //showMessage("Successfully added");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("fail to add comment : "+e.getMessage());
                        }
                    });






                    UserGroupList userGroupList = new UserGroupList(group, groupKey);

                    userGroupListPushReference.setValue(userGroupList).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            buttonController(true, firebaseUser.getUid(), groupKey);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("fail to add comment : "+e.getMessage());
                        }
                    });


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

                                        Toast.makeText(GroupDetailActivity.this, "Find tutor!!!!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });


                    /*
                    groupReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("currentGroupMembers").setValue(groupCurrentMembers+1);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    */
                    groupReference.getRef().child("currentGroupMembers").setValue(groupCurrentMembers+1);
                }
            }
        });





        detailQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                groupReference.getRef().child("currentGroupMembers").setValue(groupCurrentMembers-1);
                /*
                groupReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("currentGroupMembers").setValue(groupCurrentMembers-1);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                */

                userGroupListReference.addValueEventListener(new ValueEventListener() {
                    // Detect the changes
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Boolean alreadyJoined = false;

                        // Reinitialize the groupList
                        // Loop whole groups
                        for (DataSnapshot groupsnap: dataSnapshot.getChildren()) {
                            UserGroupList userGroupList = groupsnap.getValue(UserGroupList.class);

                            if(groupKey.equals(userGroupList.getGroupKey())) {
                                alreadyJoined = false;

                                Group group = groupsnap.getValue(Group.class);

                                tutorCourseListOfUserReference.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot coursesnap: dataSnapshot.getChildren()) {

                                            Course course = coursesnap.getValue(Course.class);
                                            if(course.getSubject().equals(group.getGroupCourseSubject())) {
                                                if(String.valueOf(course.getCategoryNum()).equals(group.getGroupCourseCategoryNum())) {

                                                    groupsReference.child(groupKey).child("tutorHere").setValue(false);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });

                                //groupsReference.child(groupKey).child("currentGroupMembers").setValue(groupCurrentMembers-1);
                                groupsnap.getRef().setValue(null);

                            }
                        }

                        buttonController(alreadyJoined, firebaseUser.getUid(), groupKey);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


                userReference.addValueEventListener(new ValueEventListener() {
                    // Detect the changes
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Boolean alreadyJoined = true;



                        userList = new ArrayList<>();
                        // Reinitialize the groupList
                        // Loop whole groups
                        for (DataSnapshot usersnap: dataSnapshot.getChildren()) {
                            User thisuser = usersnap.getValue(User.class);

                            if(userId.equals(thisuser.getuserId())) {
                                usersnap.getRef().setValue(null);
                                alreadyJoined = false;
                            }
                            else {
                                userList.add(thisuser);
                            }
                        }

                        buttonController(alreadyJoined, firebaseUser.getUid(), groupKey);

                        userAdapter = new UserAdapter(GroupDetailActivity.this,userList);
                        userRecyclerView.setAdapter(userAdapter);

                        if(!alreadyJoined) {
                            Intent homeIntent = new Intent(GroupDetailActivity.this, Home.class);
                            startActivity(homeIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                /*
                groupReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("currentGroupMembers").setValue(groupCurrentMembers-1);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                */
                //showMessage("current members: " + groupCurrentMembers + " -> " + (groupCurrentMembers-1));
            }
        });

        detailTerminateGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                usersReference.child(groupKey).setValue(null);
                /*
                usersReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot usersnap: dataSnapshot.getChildren()) {
                            User user = usersnap.getValue(User.class);
                            if(user.getGroupKey().equals(groupKey)) {
                                usersnap.getRef().setValue(null);
                            }
                        }
                    }
                    // When the database doesn't response
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                */

                //userGroupListsReference.child(groupKey).setValue(null);
                userGroupListsReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot groupsnap: dataSnapshot.getChildren()) {
                            UserGroupList group = groupsnap.getValue(UserGroupList.class);
                            if(group.getGroupKey().equals(groupKey)) {
                                groupsnap.getRef().setValue(null);
                            }
                        }
                    }
                    // When the database doesn't response
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


                groupChatReference.child(groupKey).setValue(null);


                groupsReference.child(groupKey).setValue(null);
                //usersReference.child(groupKey).setValue(null);
                //userGroupListsReference.child(userId).child(groupKey).setValue(null);




                Intent homeIntent = new Intent(GroupDetailActivity.this, Home.class);
                startActivity(homeIntent);
                finish();

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

    private void buttonController(Boolean alreadyJoined, String currentUserId, String groupKey) {

        DatabaseReference currentGroupReference = firebaseDatabase.getReference("Groups");

        currentGroupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Loop whole groups
                for (DataSnapshot groupsnap: dataSnapshot.getChildren()) {

                    Group group = groupsnap.getValue(Group.class);
                    if(groupKey.contains(group.getGroupKey())) {
                        String currentGroupOwnerId = group.getOwnerId();

                        if (alreadyJoined) {
                            detailJoinButton.setVisibility(View.GONE);
                            if (currentUserId.contentEquals(currentGroupOwnerId)) {
                                detailTerminateGroupButton.setVisibility(View.VISIBLE);
                                detailTerminateDescription.setVisibility(View.VISIBLE);
                            } else {
                                detailTerminateGroupButton.setVisibility(View.GONE);
                                detailTerminateDescription.setVisibility(View.GONE);
                                detailQuitButton.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if(firebaseUser.getUid().contentEquals(currentGroupOwnerId)){
                                detailJoinButton.setVisibility(View.GONE);
                                detailTerminateGroupButton.setVisibility(View.VISIBLE);
                                detailTerminateDescription.setVisibility(View.VISIBLE);
                            } else {
                                detailJoinButton.setVisibility(View.VISIBLE);
                            }
                            detailQuitButton.setVisibility(View.GONE);
                        }

                    }
                }
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

    // Back-button
    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(GroupDetailActivity.this, Home.class);
        startActivity(homeIntent);
        finish();
    }

}
