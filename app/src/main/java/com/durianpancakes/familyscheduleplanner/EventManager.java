package com.durianpancakes.familyscheduleplanner;

import com.alamkanak.weekview.WeekViewDisplayable;
import com.durianpancakes.familyscheduleplanner.ui.calendar.EventItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventManager {
    private ArrayList<EventItem> events;

    public EventManager(ArrayList<EventItem> events) {
        this.events = events;
    }

    public void setEvents(ArrayList<EventItem> events) {
        this.events = events;
    }

    public List<WeekViewDisplayable<EventItem>> getEventsInRange(Calendar startDate, Calendar endDate) {
        List<WeekViewDisplayable<EventItem>> eventItems = new ArrayList<>();
        int startDateMonth = startDate.get(Calendar.MONTH);
        int endDateMonth = endDate.get(Calendar.MONTH);

        for (EventItem e : events) {
            int eventStartMonth = e.getStartTime().get(Calendar.MONTH);

            if (eventStartMonth >= startDateMonth && eventStartMonth <= endDateMonth) {
                eventItems.add(e);
            }
        }

        return eventItems;
    }
}
