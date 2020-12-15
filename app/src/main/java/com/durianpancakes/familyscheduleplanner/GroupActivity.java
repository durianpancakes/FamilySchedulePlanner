package com.durianpancakes.familyscheduleplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.durianpancakes.familyscheduleplanner.ui.calendar.CalendarFragment;
import com.durianpancakes.familyscheduleplanner.ui.calendar.EventItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Calendar;

public class GroupActivity extends AppCompatActivity {

    private GroupManager groupManager;
    private ArrayList<EventItem> eventArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("groupId");
        initGroup(groupId);

        setContentView(R.layout.activity_group);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_calendar, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_group);
        NavArgument groupIdArg = new NavArgument.Builder().setDefaultValue(groupId).build();
        NavInflater navInflater = navController.getNavInflater();
        NavGraph navGraph = navInflater.inflate(R.navigation.group_navigation);
        navGraph.addArgument("groupId", groupIdArg);
        navController.setGraph(navGraph);
    }

    private void initGroup(String groupId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(new ObtainGroupDetailsListener() {
            @Override
            public void onObtainSpecificGroup(Group group) {
                groupManager = new GroupManager(group);
            }
        });
        databaseHelper.getSpecificUserGroup(groupId);
    }
}