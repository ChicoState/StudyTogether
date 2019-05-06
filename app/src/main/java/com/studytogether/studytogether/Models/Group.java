package com.studytogether.studytogether.Models;

import com.google.firebase.database.ServerValue;

public class Group {

    // All attributes
    private String groupCourseSubject;
    private String groupCourseCategoryNum;
    private String groupName;
    private String groupGoal;
    private String groupPlace;
    private int currentGroupMembers = 1;
    private int maximumGroupMembers;
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;
    private String groupPicture;
    private String groupKey;
    private String ownerId;
    private String groupOwnerPhoto;
    private Object timeStamp;
    private boolean tutorHere;

    // Constructor

    public Group(String groupCourseSubject, String groupCourseCategoryNum, String groupName, String groupGoal, String groupPlace, int maximumGroupMembers, int startHour, int startMin, int endHour, int endMin, String groupPicture, String ownerId, String groupOwnerPhoto) {
        this.groupCourseSubject = groupCourseSubject;
        this.groupCourseCategoryNum = groupCourseCategoryNum;
        this.groupName = groupName;
        this.groupGoal = groupGoal;
        this.groupPlace = groupPlace;
        this.maximumGroupMembers = maximumGroupMembers;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.groupPicture = groupPicture;
        this.ownerId = ownerId;
        this.groupOwnerPhoto = groupOwnerPhoto;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.tutorHere = false;

    }

    public Group() {
    }


    public String getGroupCourseSubject() {
        return groupCourseSubject;
    }

    public String getGroupCourseCategoryNum() {
        return groupCourseCategoryNum;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupGoal() {
        return groupGoal;
    }

    public String getGroupPlace() {
        return groupPlace;
    }

    public int getCurrentGroupMembers() {
        return currentGroupMembers;
    }

    public int getMaximumGroupMembers() {
        return maximumGroupMembers;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public String getStartTime() {
        String time = "12 : 0 pm";
        if(startHour >= 12) {
            time = String.valueOf(startHour-12) + " : " + String.valueOf(startMin) + " PM";
        } else {
            time = String.valueOf(startHour) + " : " + String.valueOf(startMin) + " AM";
        }
        return time;
    }

    public String getEndTime() {
        String time = "12 : 0 pm";
        if(startHour >= 12) {
            time = String.valueOf(startHour-12) + " : " + String.valueOf(startMin) + " PM";
        } else {
            time = String.valueOf(startHour) + " : " + String.valueOf(startMin) + " AM";
        }
        return time;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMin() {
        return endMin;
    }

    public String getGroupPicture() {
        return groupPicture;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getGroupOwnerPhoto() {
        return groupOwnerPhoto;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public boolean isTutorHere() {
        return tutorHere;
    }

    public void setGroupCourseSubject(String groupCourseSubject) {
        this.groupCourseSubject = groupCourseSubject;
    }

    public void setGroupCourseCategoryNum(String groupCourseCategoryNum) {
        this.groupCourseCategoryNum = groupCourseCategoryNum;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupGoal(String groupGoal) {
        this.groupGoal = groupGoal;
    }

    public void setGroupPlace(String groupPlace) {
        this.groupPlace = groupPlace;
    }

    public void setCurrentGroupMembers(int currentGroupMembers) {
        this.currentGroupMembers = currentGroupMembers;
    }

    public void setMaximumGroupMembers(int maximumGroupMembers) {
        this.maximumGroupMembers = maximumGroupMembers;
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

    public void setGroupPicture(String groupPicture) {
        this.groupPicture = groupPicture;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setGroupOwnerPhoto(String groupOwnerPhoto) {
        this.groupOwnerPhoto = groupOwnerPhoto;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }


    public void addGroupMembers() {
        currentGroupMembers += 1;
    }
    public void reduceGroupMembers() {
        currentGroupMembers -= 1;
    }

    public void setTutorHere(boolean tutorHere) {
        this.tutorHere = tutorHere;
    }
}