package com.studytogether.studytogether.Models;

import com.google.firebase.database.ServerValue;


public class User {

    private String userId,userImage,userName, userEmail, groupKey, userPosition;
    private Object timestamp;


    public User(String userEmail, String userId, String userImage, String userName, String groupKey, String userPosition) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.userImage = userImage;
        this.userName = userName;
        this.timestamp = ServerValue.TIMESTAMP;
        this.groupKey = groupKey;
        this.userPosition = userPosition;
    }

    public User() {

    }


    public String getUserEmail() { return userEmail; }

    public String getuserId() {
        return userId;
    }

    public String getuserImage() {
        return userImage;
    }

    public String getuserName() {
        return userName;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }
}
