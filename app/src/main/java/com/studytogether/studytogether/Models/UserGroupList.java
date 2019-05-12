package com.studytogether.studytogether.Models;

import com.google.firebase.database.ServerValue;

import java.util.List;

public class UserGroupList {

    private Group group;
    private String groupKey;

    public UserGroupList(Group group, String groupKey) {
        this.group = group;
        this.groupKey = groupKey;
    }

    public UserGroupList() {
    }

    public Group getGroup() {
        return group;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }
}

