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
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private String buildingRoom;

    public Course(String subject, int categoryNum, String courseTitle, int startHour, int startMin, int endHour, int endMin, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, String buildingRoom) {
        this.subject = subject;
        this.categoryNum = categoryNum;
        this.courseTitle = courseTitle;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
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

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
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

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }
}