package com.durianpancakes.familyscheduleplanner;

import android.util.Log;

import androidx.annotation.NonNull;

import com.durianpancakes.familyscheduleplanner.ui.calendar.CalendarFragment;
import com.durianpancakes.familyscheduleplanner.ui.calendar.EventItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DatabaseHelper {
    private DatabaseReference ref;
    private DatabaseHelperListener mListener;

    public DatabaseHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();
    }

    public DatabaseHelper(DatabaseHelperListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        this.mListener = listener;
    }

    public void addUser(String uid, User user) {
        DatabaseReference usersRef = ref.child("users");
        usersRef.child(uid).setValue(user);
    }

    public void getUsers() {
        ArrayList<User> users = new ArrayList<>();
        DatabaseReference usersRef = ref.child("users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DatabaseHelper", "getUsers successful");
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    users.add(user);
                }
                ObtainUsersListener listener = (ObtainUsersListener) mListener;
                listener.onObtainUsersSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseHelper", "getUsers unsuccessful");
            }
        });
    }

    public void getSpecificUserGroup(String groupId) {
        DatabaseReference groupRef = ref.child("groups").child(groupId);
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DatabaseHelper", "getSpecificUserGroup successful");
                Group group = snapshot.getValue(Group.class);
                ObtainGroupDetailsListener listener = (ObtainGroupDetailsListener) mListener;
                listener.onObtainSpecificGroup(group);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseHelper", "getSpecificUserGroup unsuccessful");
            }
        });
    }

    public void getUserGroups(String uid) {
        ArrayList<Group> groups = new ArrayList<>();
        DatabaseReference groupsRef = ref.child("users").child(uid).child("groups");
        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DatabaseHelper", "getUserGroups successful");
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Group group = ds.getValue(Group.class);
                    groups.add(group);
                }
                ObtainUserGroupsListener listener = (ObtainUserGroupsListener) mListener;
                listener.onObtainUserGroups(groups);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseHelper", "getUserGroups unsuccessful");
            }
        });
    }

    public void getSpecificGroupEvents(String groupId) {
        ArrayList<EventItem> events = new ArrayList<EventItem>();
        DatabaseReference eventsRef = ref.child("groupEvents").child(groupId);
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DatabaseHelper", "getSpecificGroupEvents successful");
                for (DataSnapshot ds : snapshot.getChildren()) {
                    try {
                        events.add(processEventData(ds));
                        Log.d("DatabaseHelper", "Event added successfully");
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.e("DatabaseHelper", "There was an error reading an event");
                    }
                }
                ObtainGroupEventsListener listener = (ObtainGroupEventsListener) mListener;
                listener.onObtainGroupEvents(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseHelper", "getSpecificGroupEvents unsuccessful");
            }
        });
    }

    private EventItem processEventData(DataSnapshot snapshot) throws ParseException {
        EventItem eventItem  = new EventItem();

        for (DataSnapshot ds : snapshot.getChildren()) {
            String key = ds.getKey();

            switch (key) {
                case "id":
                    int id = ds.getValue(int.class);
                    eventItem.setId(id);
                    break;
                case "title":
                    String title = ds.getValue(String.class);
                    eventItem.setTitle(title);
                    break;
                case "startTime":
                    String startTime = ds.getValue(String.class);
                    eventItem.setStartTime(parseCalendarFromString(startTime));
                    break;
                case "endTime":
                    String endTime = ds.getValue(String.class);
                    eventItem.setEndTime(parseCalendarFromString(endTime));
                    break;
                case "location":
                    // This field contains the ownerId instead of location
                    String owner = ds.getValue(String.class);
                    eventItem.setLocation(owner);
                    break;
                default:
                    // Fall through. No other cases are expected.
            }
        }

        return eventItem;
    }

    private Calendar parseCalendarFromString(String calString) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",
                Locale.getDefault());
        cal.setTime(sdf.parse(calString));

        return cal;
    }
}
