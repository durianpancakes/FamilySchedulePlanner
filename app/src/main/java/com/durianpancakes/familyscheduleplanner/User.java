package com.durianpancakes.familyscheduleplanner;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<Group> groups;
    private String id;
    private String name;

    public User(ArrayList<Group> groups, String id, String name) {
        this.groups = groups;
        this.id = id;
        this.name = name;
    }

    public User() {
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
