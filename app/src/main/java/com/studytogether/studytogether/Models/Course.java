package com.studytogether.studytogether.Models;

import com.google.firebase.database.ServerValue;

import java.util.List;

public class Course {

    // All attributes
    private String subject;
    private int categoryNum;
    private String courseTitle;

    public Course(String subject, int categoryNum, String courseTitle/*, int startHour, int startMin, int endHour, int endMin, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, String buildingRoom*/) {
        this.subject = subject;
        this.categoryNum = categoryNum;
        this.courseTitle = courseTitle;
    }

    public Course() {

    }

    public String getSubject() {
        return subject;
    }

    public int getCategoryNum() {
        return categoryNum;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCategoryNum(int categoryNum) {
        this.categoryNum = categoryNum;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
}