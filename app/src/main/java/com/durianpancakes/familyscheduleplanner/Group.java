package com.durianpancakes.familyscheduleplanner;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String id;
    private String groupTitle;
    private String adminId;
    private long numMembers;
    private List<Member> members;

    public Group(String groupTitle, String adminId, ArrayList<Member> members) {
        this.groupTitle = groupTitle;
        this.adminId = adminId;
        this.members = members;
        this.numMembers = members.size();
    }

    public Group() {
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public long getNumMembers() {
        return numMembers;
    }

    public void setNumMembers(int numMembers) {
        this.numMembers = numMembers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }
}
