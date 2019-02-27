package com.studytogether.studytogether.Models;

import com.google.firebase.database.ServerValue;

public class Group {
    private String groupName;
    private String groupGoal;
    private String groupActive;
    private String groupPicture;
    private String postKey;
    private String userId;
    private String userPhoto;
    private Object timeStamp;

    public Group(String groupName, String groupGoal, String groupActive, String groupPicture, String userId, String userPhoto) {
        this.groupName = groupName;
        this.groupGoal = groupGoal;
        this.groupActive = groupActive;
        this.groupPicture = groupPicture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public Group() {
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupGoal() {
        return groupGoal;
    }

    public String getGroupActive() {
        return groupActive;
    }

    public String getGroupPicture() {
        return groupPicture;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
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

    public void setGroupActive(String groupActive) {
        this.groupActive = groupActive;
    }

    public void setGroupPicture(String groupPicture) {
        this.groupPicture = groupPicture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
