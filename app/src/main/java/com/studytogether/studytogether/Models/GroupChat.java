package com.studytogether.studytogether.Models;

import com.google.firebase.database.ServerValue;


public class GroupChat {

    private String content,userId,userImage,userName, groupKey;
    private Object timestamp;
    private  boolean myComment;


    public GroupChat() {
    }

    public GroupChat(String content, String userId, String userImage, String userName, String groupKey, boolean myComment) {
        this.content = content;
        this.userId = userId;
        this.userImage = userImage;
        this.userName = userName;
        this.timestamp = ServerValue.TIMESTAMP;
        this.groupKey = groupKey;
        this.myComment = myComment;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getuserImage() {
        return userImage;
    }

    public void setuserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public boolean isMyComment() {
        return myComment;
    }

    public void setMyComment(boolean myComment) {
        this.myComment = myComment;
    }
}
