package com.durianpancakes.familyscheduleplanner;

import android.util.Log;

import androidx.annotation.NonNull;

import com.durianpancakes.familyscheduleplanner.ui.calendar.CalendarFragment;
import com.durianpancakes.familyscheduleplanner.ui.calendar.EventItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    public void addEvent(int eventId, String groupId, EventItem event) {
        DatabaseReference eventsRef = ref.child("groupEvents");
        event.setId(eventId);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        event.setLocation(firebaseUser.getDisplayName());
        eventsRef.child(groupId).child(String.valueOf(eventId)).setValue(event);
    }

    public void addGroup(String groupTitle) {
        Group group = updateGroupNode(groupTitle);
        updateUserNode(group);
    }

    public void addMemberToGroup(String uid, String groupId) {

    }

    private Group updateGroupNode(String groupTitle) {
        DatabaseReference groupsRef = ref.child("groups");
        DatabaseReference newGroupRef = groupsRef.push();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Group group = new Group();
        Member member = new Member(firebaseUser.getUid(), firebaseUser.getDisplayName(),
                firebaseUser.getEmail());
        member.setMember(true);
        ArrayList<Member> members = new ArrayList<>();
        members.add(member);
        group.setGroupTitle(groupTitle);
        group.setMembers(members);
        group.setAdminId(firebaseUser.getUid());
        group.setId(newGroupRef.getKey());
        newGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DatabaseHelper", "addGroup successful");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        newGroupRef.setValue(group);

        return group;
    }

    private void updateUserNode(Group group) {
        DatabaseReference usersRef = ref.child("users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = usersRef.child(firebaseUser.getUid());
        DatabaseReference groupRef = userRef.child("groups").child(group.getId());
        groupRef.setValue(group);
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
        EventItem eventItem = new EventItem();

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
                    eventItem.setStartTime(CalendarParser.parseCalendarFromString(startTime));
                    break;
                case "endTime":
                    String endTime = ds.getValue(String.class);
                    eventItem.setEndTime(CalendarParser.parseCalendarFromString(endTime));
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
}
