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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.studytogether.studytogether.R;

import java.net.URI;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {

    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;

    // Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ImageView editUserPhoto;
    TextView editUserName, editUserEmail;
    Button addCourse;

    public EditProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View fragmentView = inflater.inflate(R.layout.edit_profile, viewGroup, false);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String userName = firebaseUser.getDisplayName();
        String userEmail = firebaseUser.getEmail();

        editUserName = fragmentView.findViewById(R.id.edit_profile_user_name);
        editUserEmail = fragmentView.findViewById(R.id.edit_profile_user_email);
        editUserPhoto = fragmentView.findViewById(R.id.edit_profile_user_photo);



        editUserName.setText(userName);
        editUserEmail.setText(userEmail);

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
                        if(!subjectSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose subject…")) {
                            Toast.makeText(getActivity(), subjectSpinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
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

}