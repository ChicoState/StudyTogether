package com.studytogether.studytogether.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.studytogether.studytogether.Adapters.CourseAdapter;
import com.studytogether.studytogether.Models.Course;
import com.studytogether.studytogether.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {

    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;

    // Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference tutorCourseListReference;
    DatabaseReference courseReference;

    ImageView editUserPhoto;
    TextView editUserName, editUserEmail;
    Button addCourse;


    RecyclerView courseRecyclerView;
    CourseAdapter courseAdapter;
    List<Course> courseList;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

    public EditProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View fragmentView = inflater.inflate(R.layout.edit_profile, viewGroup, false);

        // Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String currentUserId = firebaseUser.getUid();

        String userName = firebaseUser.getDisplayName();
        String userEmail = firebaseUser.getEmail();

        editUserName = fragmentView.findViewById(R.id.edit_profile_user_name);
        editUserEmail = fragmentView.findViewById(R.id.edit_profile_user_email);
        editUserPhoto = fragmentView.findViewById(R.id.edit_profile_user_photo);

        editUserName.setText(userName);
        editUserEmail.setText(userEmail);


        courseRecyclerView  = fragmentView.findViewById(R.id.courseListRV);





        // Grab the image
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(editUserPhoto);

        editUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 22) {

                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                            Toast.makeText(getContext(),"Please accept for required permission",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    PReqCode);
                        }
                    }
                    else {
                        //Open gallery
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, REQUESCODE);
                    }
                }
                else {
                    //Open gallery
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent,REQUESCODE);
                }
            }

        });
        return fragmentView;
    }

    private void updateTutorCourseList() {

        courseRecyclerView.setLayoutManager(linearLayoutManager);
        courseRecyclerView.setHasFixedSize(true);

        // Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String currentUserId = firebaseUser.getUid();

        DatabaseReference tutorCourseListReference = firebaseDatabase.getReference("TutorCourseListOfUser").child(currentUserId);
        tutorCourseListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                courseList = new ArrayList<>();
                for (DataSnapshot coursesnap: dataSnapshot.getChildren()) {
                    Course course = coursesnap.getValue(Course.class);
                    courseList.add(course);
                    //Toast.makeText(getContext()," Currenr userid: " + currentUserId + "  course name: " + course.getCourseTitle(),Toast.LENGTH_LONG).show();
                }
                courseAdapter = new CourseAdapter(getActivity(),courseList);
                courseRecyclerView.setAdapter(courseAdapter);
            }

            // When the database doesn't response
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String userName = firebaseUser.getDisplayName();

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            Uri pickedImgUri = data.getData() ;
            editUserPhoto.setImageURI(pickedImgUri);

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .setPhotoUri(pickedImgUri)
                    .build();

            firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(),"Successfully updated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        updateTutorCourseList();

        // Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String currentUserId = firebaseUser.getUid();
        tutorCourseListReference = firebaseDatabase.getReference("TutorCourseListOfUser").child(currentUserId).push();
        courseReference = firebaseDatabase.getReference("Course");

        addCourse = (Button) getActivity().findViewById(R.id.edit_add_course_button);

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder spinnerBuilder = new AlertDialog.Builder(getContext());
                View addCourseSpinnerView = getLayoutInflater().inflate(R.layout.add_course_spinner, null);
                spinnerBuilder.setTitle("Choose subject and category number");
                Spinner subjectSpinner = (Spinner) addCourseSpinnerView.findViewById(R.id.subjectSpinner);
                Spinner categoryNumSpinner = (Spinner) addCourseSpinnerView.findViewById(R.id.categoryNumSpinner);

                ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.courseSubjectList));
                subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subjectSpinner.setAdapter(subjectAdapter);

                ArrayAdapter<String> categoryNumAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.csciCategoryNum));
                categoryNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoryNumSpinner.setAdapter(categoryNumAdapter);

                spinnerBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if((!subjectSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose subject…")) && (!categoryNumSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose course number…"))) {
                            Toast.makeText(getActivity(), subjectSpinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();


                            courseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    //courseList = new ArrayList<>();
                                    for (DataSnapshot coursesnap: dataSnapshot.getChildren()) {

                                        Course course = coursesnap.getValue(Course.class);
                                        String courseNum = String.valueOf(course.getCategoryNum());

                                        if((course.getSubject().contains(subjectSpinner.getSelectedItem().toString())) && (courseNum.contains(categoryNumSpinner.getSelectedItem().toString()))) {
                                            // SuccessListener
                                            tutorCourseListReference.setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext(),"Successfully added",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }

                                // When the database doesn't response
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });

                            dialogInterface.dismiss();
                        }
                    }
                });

                spinnerBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                spinnerBuilder.setView(addCourseSpinnerView);
                AlertDialog dialog = spinnerBuilder.create();
                dialog.show();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get List Posts from the database
        updateTutorCourseList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}