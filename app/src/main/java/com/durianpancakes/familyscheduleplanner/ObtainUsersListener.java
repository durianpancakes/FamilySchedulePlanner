package com.durianpancakes.familyscheduleplanner;

import java.util.ArrayList;

public interface ObtainUsersListener extends DatabaseHelperListener {
    void onObtainUsersSuccess(ArrayList<User> users);
}
