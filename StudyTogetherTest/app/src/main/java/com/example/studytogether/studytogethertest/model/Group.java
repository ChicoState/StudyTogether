package com.example.studytogether.studytogethertest.model;

public class Group {
    private String Group_name;
    private String Group_title;
    private Boolean Group_active;
    private Integer Group_members;
    private String Group_goal;

    public Group() {

    }

    public Group(String group_name, String group_title, Boolean group_active, Integer group_members, String group_goal) {
        Group_name  = group_name;
        Group_title = group_title;
        Group_active = group_active;
        Group_members = group_members;
        Group_goal = group_goal;
    }

    public String getGroup_name() {
        return Group_name;
    }

    public String getGroup_title() {
        return Group_title;
    }

    public Boolean getGroup_active() {
        return Group_active;
    }

    public Integer getGroup_members() {
        return Group_members;
    }

    public String getGroup_goal() {
        return Group_goal;
    }


    public void setGroup_name(String group_name) {
        Group_name = group_name;
    }

    public void setGroup_title(String group_title) {
        Group_title = group_title;
    }

    public void setGroup_active(Boolean group_active) {
        Group_active = group_active;
    }

    public void setGroup_members(Integer group_members) {
        Group_members = group_members;
    }

    public void setGroup_goal(String group_goal) {
        Group_goal = group_goal;
    }
}
