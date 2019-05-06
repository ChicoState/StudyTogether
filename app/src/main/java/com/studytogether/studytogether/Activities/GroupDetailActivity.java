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
    TextView detailGroupName, detailGroupPlace, detailGroupGoal, detailGroupAddedDate, detailTerminateDescription, detailCourseSubject, detailCategoryNum, detailCourseDescription;
    Button detailJoinButton, detailQuitButton, detailTerminateGroupButton;

    List<User> userList;
    String courseTitle;


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

        String ownerId = intent.getExtras().getString("GroupOwnerId");
        String groupOwnerPhoto = intent.getStringExtra("GroupOwnerPhoto");

        String groupPicture = getIntent().getStringExtra("GroupPicture");

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
        detailCourseSubject.setText(courseSubject);
        detailCategoryNum.setText(courseCategoryNum);



        DatabaseReference courseReference = firebaseDatabase.getReference("Course");


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

                buttonController(alreadyJoined, firebaseUser.getUid(), groupKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




        detailJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group group = new Group(courseSubject, courseCategoryNum ,groupName, groupGoal, groupPlace, groupMaxMembers, startHour, startMin, endHour, endMin, groupPicture, ownerId, groupOwnerPhoto );

                if(group.getMaximumGroupMembers() <= group.getCurrentGroupMembers()) {
                    showMessage("The group is FULL, SORRY");
                } else {

                    DatabaseReference userReference = firebaseDatabase.getReference("User").child(groupKey).push();

                    DatabaseReference groupReference = firebaseDatabase.getReference("Groups").child(groupKey);

                    DatabaseReference userGroupListReference = firebaseDatabase.getReference("UserGroupList").child(userId).push();

                    String userEmail = firebaseUser.getEmail();
                    String userId = firebaseUser.getUid();
                    String userName = firebaseUser.getDisplayName();
                    String userImage = firebaseUser.getPhotoUrl().toString();
                    User user = new User(userEmail,userId,userImage,userName);

                    userReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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


                    UserGroupList userGroupList = new UserGroupList(group, groupKey);

                    userGroupListReference.setValue(userGroupList).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            group.addGroupMembers();
                            buttonController(true, firebaseUser.getUid(), groupKey);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("fail to add comment : "+e.getMessage());
                        }
                    });


                    DatabaseReference tutorCourseListOfUserReference = firebaseDatabase.getReference("TutorCourseListOfUser").child(userId);
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
                                                dataSnapshot.getRef().child("tutorHere").setValue(false);
                                                dataSnapshot.getRef().child("currentGroupMembers").setValue(group.getCurrentGroupMembers()+1);
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
                }
            }
        });





        detailQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();

                String userId = firebaseUser.getUid();


                DatabaseReference userReference = firebaseDatabase.getReference("User").child(groupKey);

                DatabaseReference groupReference = firebaseDatabase.getReference("Groups");

                DatabaseReference userGroupListReference = firebaseDatabase.getReference("UserGroupList").child(userId);


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
                                userGroupListReference.child(groupsnap.getKey()).setValue(null);
                                alreadyJoined = false;



                                Group group = groupsnap.getValue(Group.class);

                                DatabaseReference tutorCourseListOfUserReference = firebaseDatabase.getReference("TutorCourseListOfUser").child(userId);
                                tutorCourseListOfUserReference.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot coursesnap: dataSnapshot.getChildren()) {

                                            Course course = coursesnap.getValue(Course.class);
                                            if(course.getSubject().equals(group.getGroupCourseSubject())) {
                                                if(String.valueOf(course.getCategoryNum()).equals(group.getGroupCourseCategoryNum())) {

                                                    group.setTutorHere(false);

                                                    groupReference.addValueEventListener(new ValueEventListener() {

                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            dataSnapshot.getRef().child("tutorHere").setValue(false);
                                                            dataSnapshot.getRef().child("currentGroupMembers").setValue(group.getCurrentGroupMembers()-1);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        }
                                                    });

                                                    Toast.makeText(GroupDetailActivity.this, "Tutor Out!!!!", Toast.LENGTH_LONG).show();
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
                        Boolean alreadyJoined = false;



                        userList = new ArrayList<>();
                        // Reinitialize the groupList
                        // Loop whole groups
                        for (DataSnapshot usersnap: dataSnapshot.getChildren()) {
                            User user = usersnap.getValue(User.class);

                            if(userId.equals(user.getuserId())) {
                                userReference.child(usersnap.getKey()).setValue(null);
                                alreadyJoined = false;
                            }
                            else {
                                userList.add(user);
                            }
                        }

                        buttonController(alreadyJoined, firebaseUser.getUid(), groupKey);

                        if(alreadyJoined == false) {
                            Intent homeIntent = new Intent(GroupDetailActivity.this, Home.class);
                            startActivity(homeIntent);
                            finish();
                        }

                        userAdapter = new UserAdapter(GroupDetailActivity.this,userList);
                        userRecyclerView.setAdapter(userAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
        });

        detailTerminateGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();

                DatabaseReference userReference = firebaseDatabase.getReference("User");

                DatabaseReference userGroupListReference = firebaseDatabase.getReference("UserGroupList");

                DatabaseReference groupChatReference = firebaseDatabase.getReference("GroupChat");

                DatabaseReference groupReference = firebaseDatabase.getReference("Groups");




                userReference.child(groupKey).setValue(null);

                userGroupListReference.child(groupKey).setValue(null);

                groupChatReference.child(groupKey).setValue(null);

                groupReference.child(groupKey).setValue(null);




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
                            if (currentUserId.equals(currentGroupOwnerId)) {
                                detailTerminateGroupButton.setVisibility(View.VISIBLE);
                                detailTerminateDescription.setVisibility(View.VISIBLE);
                            } else {
                                detailTerminateGroupButton.setVisibility(View.GONE);
                                detailTerminateDescription.setVisibility(View.GONE);
                                detailQuitButton.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if(firebaseUser.getUid().equals(currentGroupOwnerId)){
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
