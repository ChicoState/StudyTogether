package com.studytogether.studytogether.Models;

import com.google.firebase.database.ServerValue;

import java.util.List;

public class Course {

    // All attributes
    private String subject;
    private int categoryNum;
    private String courseTitle;
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;
    private Boolean mon;
    private Boolean tue;
    private Boolean wed;
    private Boolean tur;
    private Boolean fri;
    private String buildingRoom;

    public Course(String subject, int categoryNum, String courseTitle, int startHour, int startMin, int endHour, int endMin, Boolean mon, Boolean tue, Boolean wed, Boolean tur, Boolean fri, String buildingRoom) {
        this.subject = subject;
        this.categoryNum = categoryNum;
        this.courseTitle = courseTitle;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.tur = tur;
        this.fri = fri;
        this.buildingRoom = buildingRoom;
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

    public int getStartHour() {
        return startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMin() {
        return endMin;
    }

    public String getBuildingRoom() {
        return buildingRoom;
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

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public void setEndMin(int endMin) {
        this.endMin = endMin;
    }

    public void setBuildingRoom(String buildingRoom) {
        this.buildingRoom = buildingRoom;
    }
}