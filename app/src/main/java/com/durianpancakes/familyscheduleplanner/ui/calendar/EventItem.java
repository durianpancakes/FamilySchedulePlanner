package com.durianpancakes.familyscheduleplanner.ui.calendar;

import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;

public class EventItem implements WeekViewDisplayable<EventItem> {
    private int id;
    private String title;
    private Calendar startTime;
    private Calendar endTime;
    private String location; // Used to contain ownerId due to API limitation
    private int color;

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

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
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
