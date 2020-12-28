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
import com.durianpancakes.familyscheduleplanner.EditEventDialogFragment;
import com.durianpancakes.familyscheduleplanner.EventManager;
import com.durianpancakes.familyscheduleplanner.Group;
import com.durianpancakes.familyscheduleplanner.GroupManager;
import com.durianpancakes.familyscheduleplanner.ObtainGroupDetailsListener;
import com.durianpancakes.familyscheduleplanner.ObtainGroupEventsListener;
import com.durianpancakes.familyscheduleplanner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class CalendarFragment extends Fragment {

    private WeekView<EventItem> weekView;
    private EventManager eventManager;
    private String groupId;
    private FloatingActionButton addEventFab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        assert getArguments() != null;
        groupId = getArguments().getString("groupId");

        weekView = (WeekView) root.findViewById(R.id.weekView);
        addEventFab = root.findViewById(R.id.add_event);

        initializeAddEventFab();
        initializeWeekView();
        initializeEventManager();

        return root;
    }

    private void initializeAddEventFab() {
        addEventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditEventDialogFragment editEventDialogFragment =
                        EditEventDialogFragment.newInstance(null);
                editEventDialogFragment.setEventDialogListener(
                        new EditEventDialogFragment.EventDialogListener() {
                    @Override
                    public void onEditPressed(EventItem event) {
                        eventManager.addEvent(event);
                        Snackbar.make(requireView(), "Event added", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
                editEventDialogFragment.show(requireActivity().getSupportFragmentManager(),
                        "eventInput");
            }
        });
    }

    private void initializeEventManager() {
        eventManager = new EventManager(groupId);
        eventManager.setEventManagerListener(new EventManager.EventManagerListener() {
            @Override
            public void onEventManagerChange() {
                weekView.notifyDataSetChanged();
            }
        });
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
                List<WeekViewDisplayable<EventItem>> events = eventManager.getEventsInRange(startDate, endDate);
                if (events == null) {
                    return new ArrayList<>();
                }
                return events;
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