package com.studytogether.studytogether.Models;

import com.google.firebase.database.ServerValue;

import java.util.List;


public class TutorCourseListOfUser {

    private Course course;
    private String courseKey;

    public TutorCourseListOfUser(Course course, String courseKey) {
        this.course = course;
        this.courseKey = courseKey;
    }

    public TutorCourseListOfUser() {
    }

    public Course getCourse() {
        return course;
    }

    public String getCourseKey() {
        return courseKey;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setCourseKey(String courseKey) {
        this.courseKey = courseKey;
    }
}

