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

    EditText courseSubject, categoryNum, courseTitle, startTime, endTime, buildingRoom;
    Button addCourseButton;
    CheckBox mon, tue, wed, tur, fri;

    private int startHour, startMin, endHour, endMin;
    private boolean monCheck, tueCheck, wedCheck, turCheck, friCheck;


    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        courseSubject = findViewById(R.id.edit_subject);
        categoryNum = findViewById(R.id.edit_category_num);
        courseTitle = findViewById(R.id.edit_course_title);

        startTime = findViewById(R.id.edit_start_time);
        endTime = findViewById(R.id.edit_end_time);
        buildingRoom = findViewById(R.id.edit_building_room);

        addCourseButton = findViewById(R.id.add_course_button);

        mon = findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wed = findViewById(R.id.wed);
        tur = findViewById(R.id.thur);
        fri = findViewById(R.id.fri);



        startTime.setOnClickListener(view -> {
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

                            startTime.setText(hourOfDay + " : " + minute);
                            startHour = hourOfDay;
                            startMin = minute;
                        }
                    }, startHour, startMin, false);
            timePickerDialog.show();
        });

        endTime.setOnClickListener(view -> {
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
                            endTime.setText(hourOfDay + " : " + minute);
                            endHour = hourOfDay;
                            endMin = minute;
                        }
                    }, endHour, endMin, false);
            timePickerDialog.show();
        });





        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean monChecked = ((CheckBox) findViewById(R.id.mon)).isChecked();
                boolean tueChecked = ((CheckBox) findViewById(R.id.tue)).isChecked();
                boolean wedChecked = ((CheckBox) findViewById(R.id.wed)).isChecked();
                boolean thurChecked = ((CheckBox) findViewById(R.id.thur)).isChecked();
                boolean friChecked = ((CheckBox) findViewById(R.id.fri)).isChecked();
                showMessage("mon: "+ monChecked + "tue: "+ tueChecked);
                Course course = new Course(
                        courseSubject.getText().toString(),
                        Integer.parseInt(categoryNum.getText().toString()),
                        courseTitle.getText().toString(),
                        startHour,
                        startMin,
                        endHour,
                        endMin,
                        monChecked,
                        tueChecked,
                        wedChecked,
                        thurChecked,
                        friChecked,
                        buildingRoom.getText().toString());
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
                //showMessage("Course Added successfully");
            }
        });
    }

    // Print Message into user
    private void showMessage(String message) {
        // Long time showed up message
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
