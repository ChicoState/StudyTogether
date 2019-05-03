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
    private Boolean monCheck, tueCheck, wedCheck, turCheck, friCheck;

    List<Boolean> courseDays;


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
        courseTitle = findViewById(R.id.edit_category_num);

        startTime = findViewById(R.id.edit_start_time);
        endTime = findViewById(R.id.edit_end_time);
        buildingRoom = findViewById(R.id.edit_building_room);

        addCourseButton = findViewById(R.id.add_course_button);

        mon = findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wed = findViewById(R.id.wed);
        tur = findViewById(R.id.tur);
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
                        }
                    }, endHour, endMin, false);
            timePickerDialog.show();
        });




        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monCheck = false;
                if (((CheckBox) v).isChecked()) {
                    monCheck = true;
                }
            }
        });
        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tueCheck = false;
                if (((CheckBox) v).isChecked()) {
                    tueCheck = true;
                }
            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wedCheck = false;
                if (((CheckBox) v).isChecked()) {
                    wedCheck = true;
                }
            }
        });
        tur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turCheck = false;
                if (((CheckBox) v).isChecked()) {
                    turCheck = true;
                }
            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friCheck = false;
                if (((CheckBox) v).isChecked()) {
                    friCheck = true;
                }
            }
        });





        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Course course = new Course(
                        courseSubject.getText().toString(),
                        Integer.parseInt(categoryNum.getText().toString()),
                        courseTitle.getText().toString(),
                        startHour,
                        startMin,
                        endHour,
                        endMin,
                        monCheck,
                        tueCheck,
                        wedCheck,
                        turCheck,
                        friCheck,
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
