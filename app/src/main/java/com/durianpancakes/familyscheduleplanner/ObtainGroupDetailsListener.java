package com.durianpancakes.familyscheduleplanner;

public interface ObtainGroupDetailsListener extends DatabaseHelperListener {
    void onObtainSpecificGroup(Group group);
}
