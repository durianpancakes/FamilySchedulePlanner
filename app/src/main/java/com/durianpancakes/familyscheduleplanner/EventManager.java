package com.durianpancakes.familyscheduleplanner;

import com.alamkanak.weekview.WeekViewDisplayable;
import com.durianpancakes.familyscheduleplanner.ui.calendar.EventItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventManager {
    private ArrayList<EventItem> mEvents;
    private final String groupId;
    private EventManagerListener mListener;

    public interface EventManagerListener {
        void onEventManagerChange();
    }

    public EventManager(String groupId) {
        this.groupId = groupId;
        refreshEvents();
    }

    public void setEventManagerListener(EventManagerListener eventManagerListener) {
        this.mListener = eventManagerListener;
    }

    private void refreshEvents() {
        DatabaseHelper databaseHelper = new DatabaseHelper(new ObtainGroupEventsListener() {
            @Override
            public void onObtainGroupEvents(ArrayList<EventItem> events) {
                mEvents = events;
                mListener.onEventManagerChange();
            }
        });
        databaseHelper.getSpecificGroupEvents(groupId);
    }

    public void setEvents(ArrayList<EventItem> events) {
        this.mEvents = events;
    }

    public List<WeekViewDisplayable<EventItem>> getEventsInRange(Calendar startDate, Calendar endDate) {
        List<WeekViewDisplayable<EventItem>> eventItems = new ArrayList<>();
        int startDateMonth = startDate.get(Calendar.MONTH);
        int endDateMonth = endDate.get(Calendar.MONTH);

        if (mEvents != null) {
            for (EventItem e : mEvents) {
                int eventStartMonth = e.getStartTimeCalendar().get(Calendar.MONTH);

                if (eventStartMonth >= startDateMonth && eventStartMonth <= endDateMonth) {
                    eventItems.add(e);
                }
            }
        }

        return eventItems;
    }

    public void addEvent(EventItem event) {
        DatabaseHelper databaseHelper = new DatabaseHelper(new AddEventListener() {
            @Override
            public void onEventAdded() {
                mEvents.add(event);
                mListener.onEventManagerChange();
            }
        });
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        event.setLocation(firebaseUser.getDisplayName());
        int eventId = (int) Calendar.getInstance().getTimeInMillis();
        databaseHelper.addEvent(eventId, groupId, event);
    }
}
