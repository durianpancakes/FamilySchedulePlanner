package com.durianpancakes.familyscheduleplanner.ui.calendar;

import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alamkanak.weekview.EventClickListener;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.OnVerticalScrollListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.durianpancakes.familyscheduleplanner.DatabaseHelper;
import com.durianpancakes.familyscheduleplanner.EventManager;
import com.durianpancakes.familyscheduleplanner.Group;
import com.durianpancakes.familyscheduleplanner.GroupManager;
import com.durianpancakes.familyscheduleplanner.ObtainGroupDetailsListener;
import com.durianpancakes.familyscheduleplanner.ObtainGroupEventsListener;
import com.durianpancakes.familyscheduleplanner.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {

    private WeekView<EventItem> weekView;
    private EventManager eventManager = new EventManager(new ArrayList<>());
    private String groupId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        assert getArguments() != null;
        groupId = getArguments().getString("groupId");

        weekView = (WeekView) root.findViewById(R.id.weekView);
        initializeWeekView();
        initializeEventManager();
        return root;
    }

    private void initializeEventManager() {
        initGroupEvents(groupId);
    }

    private void initGroupEvents(String groupId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(new ObtainGroupEventsListener() {
            @Override
            public void onObtainGroupEvents(ArrayList<EventItem> events) {
                eventManager = new EventManager(events);
                weekView.notifyDataSetChanged();
            }
        });
        databaseHelper.getSpecificGroupEvents(groupId);
    }

    private void initializeWeekView() {
        weekView.setOnEventClickListener(new EventClickListener<EventItem>() {
            @Override
            public void onEventClick(EventItem data, RectF eventRect) {

            }
        });

        weekView.setMonthChangeListener(new MonthLoader.MonthChangeListener<EventItem>() {
            @Override
            public List<WeekViewDisplayable<EventItem>> onMonthChange(Calendar startDate, Calendar endDate) {
                return eventManager.getEventsInRange(startDate, endDate);
            }
        });

        weekView.setVerticalScrollListener(new OnVerticalScrollListener() {
            @Override
            public void onScrollTop() {

            }

            @Override
            public void onScrollDown() {

            }
        });

        weekView.goToCurrentTime();
    }
}