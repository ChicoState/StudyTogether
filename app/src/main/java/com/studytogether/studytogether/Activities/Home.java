package com.studytogether.studytogether.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.studytogether.studytogether.Fragments.HomeFragment;
import com.studytogether.studytogether.Fragments.ProfileFragment;
import com.studytogether.studytogether.Fragments.SettingsFragment;
import com.studytogether.studytogether.Fragments.TutorFragment;
import com.studytogether.studytogether.Models.Course;
import com.studytogether.studytogether.Models.Group;
import com.studytogether.studytogether.Models.User;
import com.studytogether.studytogether.Models.UserGroupList;
import com.studytogether.studytogether.R;
import com.studytogether.studytogether.Adapters.GroupAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


// Home Activity
public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Declare the groupAdapter
    GroupAdapter adapter;
    // Declare the groupList and initialize to empty list
    List<Group> groupList = new ArrayList<>();
    List<String> categoryList = new ArrayList<>();

    // Flags
    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;

    // Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    GoogleSignInClient mGoogleSignInClient;

    DatabaseReference userReference;

    DatabaseReference userGroupListReference;

    // Dialog for popup
    Dialog popAddGroup;

    // Items
    Spinner popupSubjectSpinner, popupCategoryNumSpinner;
    ImageView popupGroupImage, popupAddBtn;
    TextView popupGroupName, popupGroupGoal, popupGroupPlace, popupMaximumGroupMembers, popupStartTimeInput, popupEndTimeInput;
    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;

    private int startHour, startMin, endHour, endMin;
    private String courseSubject = null, courseCategoryNum = null;

    // Initialize the groupAdapter
    private void initGroupAdapter() {
        // If groupAdapter is null
        if(adapter == null ) {
            // Reinitialize the groupAdapter
            adapter = new GroupAdapter(this, groupList);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the contentView as activity_home2
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set up the appBar
        setSupportActionBar(toolbar);

        // Authorization
        mAuth = FirebaseAuth.getInstance();
        // Identify the current user
        currentUser = mAuth.getCurrentUser();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // Initialize the popup
        iniPopup();
        // Set up the popup Image Click
        setupPopupImageClick();

        // Set up the floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // If the floating action button is clicked, show the popup
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddGroup.show();
            }
        });

        // Drawer - Left side slide that contains the current user info and nevigations such as Home, Profile, Sign out
        // Set up the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Set up action bar toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        // Sync the toggle
        toggle.syncState();

        // Set up the navigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();
        // set the home fragment as the default one
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }


    // Image selection
    private void setupPopupImageClick() {

        popupGroupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // It is called when a user clicked image spot in popup
                // It is required permission
                checkAndRequestForPermission();
            }
        });
    }


    // Ask request permission
    private void checkAndRequestForPermission() {

        // If the user allows permission, access into user's gallery
        if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(Home.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(Home.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            // If the user allowed the permission already, open the user's gallery
            openGallery();
    }


    // Open the user's gallery
    private void openGallery() {

        // Get intent
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        // set up the intent type
        galleryIntent.setType("image/*");
        // Get into gallery
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    // Grab the image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If all flags is good with no data
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            // Get the image data
            pickedImgUri = data.getData();
            // Show up into popup the image
            popupGroupImage.setImageURI(pickedImgUri);
        }
    }


    // Initialize popup
    private void iniPopup() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Create a new dialog for popup
        popAddGroup = new Dialog(this);
        // Set up the contentView into popup
        popAddGroup.setContentView(R.layout.popup_add_group);
        // Window setting
        popAddGroup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddGroup.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddGroup.getWindow().getAttributes().gravity = Gravity.TOP;

        // Initialize the popup items
        popupSubjectSpinner = popAddGroup.findViewById(R.id.popup_subject_spinner);
        popupCategoryNumSpinner = popAddGroup.findViewById(R.id.popup_category_num_spinner);

        popupGroupName = popAddGroup.findViewById(R.id.popup_edit_groupName);
        popupGroupGoal = popAddGroup.findViewById(R.id.popup_edit_groupGoal);
        popupGroupPlace = popAddGroup.findViewById(R.id.popup_edit_groupPlace);

        popupMaximumGroupMembers = popAddGroup.findViewById(R.id.popup_member_limit);

        popupStartTimeInput = popAddGroup.findViewById(R.id.popup_edit_starttime);
        popupEndTimeInput = popAddGroup.findViewById(R.id.popup_edit_endtime);

        popupGroupImage = popAddGroup.findViewById(R.id.popup_groupImage);

        popupAddBtn = popAddGroup.findViewById(R.id.popup_addButton);
        popupClickProgress = popAddGroup.findViewById(R.id.popup_progressbar);



        ArrayAdapter<String> popupSubjectAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.courseSubjectList));
        popupSubjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        popupSubjectSpinner.setAdapter(popupSubjectAdapter);
        popupSubjectSpinner.setOnItemSelectedListener(new subjectOnClickListener());





        /*

        if(!popupSubjectSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose subject…") && (popupSubjectSpinner.getSelectedItem().toString() != null)) {
            courseSubject = popupSubjectSpinner.getSelectedItem().toString();
        }
        if(!popupCategoryNumSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose course number…") && (popupCategoryNumSpinner.getSelectedItem().toString() != null)) {
            courseCategoryNum = popupCategoryNumSpinner.getSelectedItem().toString();
        }
        */





        /*



        ArrayAdapter<String> popupCategoryNumAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.csciCategoryNum));
        popupCategoryNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        popupCategoryNumSpinner.setAdapter(popupCategoryNumAdapter);




        popupSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                switch (parent.getId()) {
                    case R.id.popup_subject_spinner:
                        courseSubject = parent.getItemAtPosition(pos).toString();

                        DatabaseReference courseReference = firebaseDatabase.getReference("Course");
                        courseReference.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot coursesnap: dataSnapshot.getChildren()) {

                                    categoryList = new ArrayList<>();
                                    Course course = coursesnap.getValue(Course.class);
                                    if(course.getSubject().equals(courseSubject)) {
                                        categoryList.add(String.valueOf(course.getCategoryNum()));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                        break;
                    case R.id.popup_category_num_spinner:
                        // do stuffs with you spinner 2
                        break;
                    default:
                        break;
                }


                showMessage(courseSubject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        */








        popupStartTimeInput.setOnClickListener(view -> {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            startHour = c.get(Calendar.HOUR_OF_DAY);
            startMin = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            if(hourOfDay > 12) {
                                popupStartTimeInput.setText(hourOfDay + " : " + minute + " PM");
                            } else {
                                popupStartTimeInput.setText(hourOfDay + " : " + minute + " AM");
                            }

                            startHour = hourOfDay;
                            startMin = minute;
                        }
                    }, startHour, startMin, false);
            timePickerDialog.show();
        });

        popupEndTimeInput.setOnClickListener(view -> {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            endHour = c.get(Calendar.HOUR_OF_DAY);
            endMin = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if(hourOfDay > 12) {
                                popupEndTimeInput.setText(hourOfDay + " : " + minute + " PM");
                            } else {
                                popupEndTimeInput.setText(hourOfDay + " : " + minute + " AM");
                            }
                            endHour = hourOfDay;
                            endMin = minute;
                        }
                    }, endHour, endMin, false);
            timePickerDialog.show();
        });




        // Listener for add-button on popup
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the group is added, let the add-button disappear and the progress-button show up.
                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                // If all inputs is typed,
                if (!popupGroupName.getText().toString().isEmpty() && !popupGroupGoal.getText().toString().isEmpty() && !popupGroupPlace.getText().toString().isEmpty() /*&& !popupStartTimeInput.getText().toString().isEmpty() && !popupEndTimeInput.getText().toString().isEmpty() && pickedImgUri != null*/) {

                    // Get Groups's storage reference
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Groups");
                    // Store the inputs into storage on Firebase database
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownlaodLink = uri.toString();

                                    // Make the group object with user's inputs
                                    Group group = new Group(
                                            courseSubject,
                                            courseCategoryNum,
                                            popupGroupName.getText().toString(),
                                            popupGroupGoal.getText().toString(),
                                            popupGroupPlace.getText().toString(),
                                            Integer.parseInt(popupMaximumGroupMembers.getText().toString()),
                                            startHour,
                                            startMin,
                                            endHour,
                                            endMin,
                                            imageDownlaodLink,
                                            currentUser.getUid(),
                                            currentUser.getPhotoUrl().toString());


                                    // Add the group
                                    addGroup(group);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    // Failure Case
                                    // Show error message
                                    showMessage(e.getMessage());
                                    // Back up the add-button and let the progress-button disappear
                                    popupClickProgress.setVisibility(View.INVISIBLE);
                                    popupAddBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                } else {
                    // If any inputs doesn't typed, print an error message
                    showMessage("Please verify all input fields and choose Group Image");
                    // Back up the add-button and let the progress-button disappear
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    // Add a group
    private void addGroup(Group group) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        String userId = firebaseUser.getUid();

        // Firebase
        // Get database instance
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // get reference of the database
        DatabaseReference myRef = database.getReference("Groups").push();

        // Get the current user's Auth key
        String groupKey = myRef.getKey();
        // Set up the groupKey with the user's key
        group.setGroupKey(groupKey);

        // SuccessListener
        myRef.setValue(group).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // If a group is added successfully, print a success message
                //showMessage("Group Added successfully");
                // Back up the add-button and let the progress-button disappear
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                // Dismiss the popup
                popAddGroup.dismiss();
            }
        });



        userReference = database.getReference("User").child(groupKey).push();

        userGroupListReference = database.getReference("UserGroupList").child(currentUser.getUid()).push();


        String userEmail = currentUser.getEmail();
        String userName = currentUser.getDisplayName();
        String userImage = currentUser.getPhotoUrl().toString();
        User user = new User(userEmail,userId,userImage,userName);


        userReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        userGroupListReference.setValue(userGroupList).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    }


    // Print Message into user
    private void showMessage(String message) {
        // Long time showed up message
        Toast.makeText(Home.this, message, Toast.LENGTH_LONG).show();
    }

    // Back-button
    @Override
    public void onBackPressed() {

        // Get the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // If the drawer showed up, close the drawer
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // Otherwise, go back
            super.onBackPressed();
        }
    }



    // Home menu in appBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    // Home menu option
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Get menu item id
        int id = item.getItemId();

        // If the setting menu clicked,
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Drawer
    // Navigation
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Get item id
        int id = item.getItemId();

        // If each section is clicked, replace the transaction into the section's fragment
        if (id == R.id.nav_home) {
            getSupportActionBar().setTitle("Home");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        } else if (id == R.id.nav_tutor) {
            getSupportActionBar().setTitle("Tutoring");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new TutorFragment()).commit();
        } else if (id == R.id.nav_profile) {
            //Intent profileActivity = new Intent(this, ProfileActivity.class);
            //startActivity(profileActivity);
            getSupportActionBar().setTitle("Profile");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
        } else if (id == R.id.nav_settings) {
            getSupportActionBar().setTitle("AddCourse");
            Intent addCourseActivity = new Intent(this, AddCourse.class);
            startActivity(addCourseActivity);
            //getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
        } else if (id == R.id.nav_signout) {
            // If the user want to sign out, sign out through Firebase authorization
            //FirebaseAuth.getInstance().signOut();
            mAuth.signOut();

            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            showMessage("Successfully sign out");
                        }
                    });

            // Get intent
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            // Go to login Activity
            startActivity(loginActivity);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Close the drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Update Navigation
    // It contains the current user info and navigation sections
    public void updateNavHeader() {

        // Set up the navigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        // Show the user info on drawer
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_email);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);

        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }





    private void chooseCategoryNum(String courseSubject){
        /*

        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference courseReference = firebaseDatabase.getReference("Course");

        courseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot coursesnap: dataSnapshot.getChildren()) {

                    categoryList = new ArrayList<>();
                    Course course = coursesnap.getValue(Course.class);
                    if(course.getSubject().equals(courseSubject)) {
                        categoryList.add(String.valueOf(course.getCategoryNum()));
                        showMessage(String.valueOf(course.getCategoryNum()) + " is in List !!!!!");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        */
        ArrayAdapter<String> popupCategoryNumAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.csciCategoryNum));
        popupCategoryNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        popupCategoryNumSpinner.setAdapter(popupCategoryNumAdapter);


        popupCategoryNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                courseCategoryNum = parent.getItemAtPosition(pos).toString();
                showMessage(courseCategoryNum);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }


    public class subjectOnClickListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int pos,
                                   long id) {

            parent.getItemAtPosition(pos);

            if (parent.getItemAtPosition(pos).toString().equals("CSCI")){

                ArrayAdapter<String> popupCategoryNumAdapter = new ArrayAdapter<String>(Home.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.csciCategoryNum));
                popupCategoryNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                popupCategoryNumAdapter.notifyDataSetChanged();
                popupCategoryNumSpinner.setAdapter(popupCategoryNumAdapter);

            } else if(parent.getItemAtPosition(pos).toString().equals("CINS")){
                ArrayAdapter<String> popupCategoryNumAdapter = new ArrayAdapter<String>(Home.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.cinsCategoryNum));
                popupCategoryNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                popupCategoryNumAdapter.notifyDataSetChanged();
                popupCategoryNumSpinner.setAdapter(popupCategoryNumAdapter);
            } else if (parent.getItemAtPosition(pos).toString().equals("EECE")){
                ArrayAdapter<String> popupCategoryNumAdapter = new ArrayAdapter<String>(Home.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.eeceCategoryNum));
                popupCategoryNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                popupCategoryNumAdapter.notifyDataSetChanged();
                popupCategoryNumSpinner.setAdapter(popupCategoryNumAdapter);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    }

}