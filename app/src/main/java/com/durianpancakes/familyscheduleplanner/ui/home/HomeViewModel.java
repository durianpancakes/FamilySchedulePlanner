package com.durianpancakes.familyscheduleplanner.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.durianpancakes.familyscheduleplanner.DatabaseHelper;
import com.durianpancakes.familyscheduleplanner.Group;
import com.durianpancakes.familyscheduleplanner.ObtainUserGroupsListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Group>> groupLiveData;
    private MutableLiveData<String> greetingLiveData;
    private MutableLiveData<String> signInStatusLiveData;
    private ArrayList<Group> groupArrayList;

    private String uid;
    private String userName;
    private String email;

    public HomeViewModel() {
        groupLiveData = new MutableLiveData<>();
        greetingLiveData = new MutableLiveData<>();
        signInStatusLiveData = new MutableLiveData<>();

        init();
    }

    public MutableLiveData<ArrayList<Group>> getGroupLiveData() {
        return groupLiveData;
    }

    public MutableLiveData<String> getGreetingLiveData() {
        return greetingLiveData;
    }

    public MutableLiveData<String> getSignInStatusLiveData() {
        return signInStatusLiveData;
    }

    public void init() {
        initFirebaseUser();
        initGreeting();
        initUserGroups();
        initSignInStatus();
    }

    private void initGreeting() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            Log.d("Time of Day", "Morning");
            greetingLiveData.setValue("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greetingLiveData.setValue("Good Afternoon");
            Log.d("Time of Day", "Afternoon");
        } else {
            greetingLiveData.setValue("Good Evening");
            Log.d("Time of Day", "Evening");
        }
    }

    private void initSignInStatus() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            signInStatusLiveData.setValue("You are currently signed in as " + email + " (" + userName + ")");
        } else {
            signInStatusLiveData.setValue("You are currently not signed in");
        }
    }

    private void initUserGroups() {
        DatabaseHelper databaseHelper = new DatabaseHelper(new ObtainUserGroupsListener() {
            @Override
            public void onObtainUserGroups(ArrayList<Group> groups) {
                groupArrayList = groups;
                groupLiveData.setValue(groupArrayList);
            }
        });
        databaseHelper.getUserGroups(uid);
    }

    private void initFirebaseUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        email = firebaseUser.getEmail();
        userName = firebaseUser.getDisplayName();
    }
}