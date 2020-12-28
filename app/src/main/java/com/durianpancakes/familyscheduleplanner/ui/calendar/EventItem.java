package com.durianpancakes.familyscheduleplanner.ui.calendar;

import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.database.Exclude;

import java.util.Calendar;

public class EventItem implements WeekViewDisplayable<EventItem> {
    private int id;
    private String title;
    private Calendar startTime;
    private Calendar endTime;
    private String location; // Used to contain ownerId due to API limitation
    private int color;

    public EventItem(String title, Calendar startTime, Calendar endTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public EventItem() {

    }

    public EventItem(int id, String title, Calendar startTime, Calendar endTime, String owner) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime.getTime().toString();
    }

    @Exclude
    public Calendar getStartTimeCalendar() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime.getTime().toString();
    }

    @Exclude
    public Calendar getEndTimeCalendar() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public WeekViewEvent<EventItem> toWeekViewEvent() {
        boolean isAllDay = false;
        return new WeekViewEvent<>(id, title, startTime, endTime,
                location, color, isAllDay, this);
    }
}
