package com.studytogether.studytogether.Activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.studytogether.studytogether.Models.Course;
import com.studytogether.studytogether.R;

import java.util.Calendar;
import java.util.List;

public class AddCourse extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    EditText courseSubject, categoryNum, courseTitle;
    Button addCourseButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_simple);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        courseSubject = findViewById(R.id.edit_subject);
        categoryNum = findViewById(R.id.edit_category_num);
        courseTitle = findViewById(R.id.edit_course_title);


        addCourseButton = findViewById(R.id.add_course_button);






        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Course course = new Course(
                        courseSubject.getText().toString(),
                        Integer.parseInt(categoryNum.getText().toString()),
                        courseTitle.getText().toString()
                        );
                addCourse(course);
            }
        });
    }
    private void addCourse(Course course) {
        // Firebase
        // Get database instance
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference courseReference = database.getReference("Course").push();


        // SuccessListener
        courseReference.setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Course Added successfully");
            }
        });
    }

    // Print Message into user
    private void showMessage(String message) {
        // Long time showed up message
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
