package com.durianpancakes.familyscheduleplanner;

import java.util.ArrayList;

public interface ObtainUserGroupsListener extends DatabaseHelperListener {
    void onObtainUserGroups(ArrayList<Group> groups);
}
