package com.studytogether.studytogether.Models;

import com.google.firebase.database.ServerValue;

import java.util.List;


public class TutorCourseOfUser {

    private Course course;
    private String courseKey;

    public TutorCourseOfUser(Course course, String courseKey) {
        this.course = course;
        this.courseKey = courseKey;
    }

    public TutorCourseOfUser() {
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

