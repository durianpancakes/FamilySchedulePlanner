package com.durianpancakes.familyscheduleplanner;

import com.durianpancakes.familyscheduleplanner.ui.calendar.EventItem;

import java.util.ArrayList;

public interface ObtainGroupEventsListener extends DatabaseHelperListener{
    void onObtainGroupEvents(ArrayList<EventItem> events);
}
