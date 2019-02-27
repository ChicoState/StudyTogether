package com.studytogether.studytogether.Models;

import com.google.firebase.database.ServerValue;

public class Group {
    private String groupName;
    private String groupGoal;
    private String groupPicture;
    private String postKey;
    private String userId;
    private String groupPlace;
    private String groupOwnerPhoto;
    private String startTime;
    private String endTime;
    private Object timeStamp;

    public Group(String groupName, String groupGoal, String groupPlace, String startTime, String endTime, String groupPicture, String userId, String groupOwnerPhoto) {
        this.groupName = groupName;
        this.groupGoal = groupGoal;
        this.groupPlace = groupPlace;
        this.startTime = startTime;
        this.endTime = endTime;
        this.groupPicture = groupPicture;
        this.userId = userId;
        this.groupOwnerPhoto = groupOwnerPhoto;
        this.timeStamp = ServerValue.TIMESTAMP;
    }
    public Group() {
    }


    public String getGroupOwnerPhoto() {
        return groupOwnerPhoto;
    }

    public void setGroupOwnerPhoto(String groupOwnerPhoto) {
        this.groupOwnerPhoto = groupOwnerPhoto;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupGoal() {
        return groupGoal;
    }

    public String getGroupPicture() {
        return groupPicture;
    }

    public String getPostKey() {
        return postKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroupPlace() {
        return groupPlace;
    }

    public String  getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }


    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupGoal(String groupGoal) {
        this.groupGoal = groupGoal;
    }

    public void setGroupPicture(String groupPicture) {
        this.groupPicture = groupPicture;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGroupPlace(String groupPlace) {
        this.groupPlace = groupPlace;
    }

    public void setStartTime(String  startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
