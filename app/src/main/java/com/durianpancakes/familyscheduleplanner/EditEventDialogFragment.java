package com.durianpancakes.familyscheduleplanner;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.durianpancakes.familyscheduleplanner.ui.calendar.EventItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditEventDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditEventDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String EVENT_ID = "param1";
    private static final String EVENT_TITLE = "param2";
    private static final String EVENT_START = "param3";
    private static final String EVENT_END = "param4";
    private static final String EVENT_OWNER = "param5";
    public static final int DATE_BUTTON_ID = 0;
    public static final int TIME_BUTTON_ID = 0;

    private int mEventId;
    private String mEventTitle;
    private Calendar mEventStart;
    private Calendar mEventEnd;
    private String mEventOwner;

    private TextView dialogTitle;
    private EditText eventTitle;
    private Button fromDate;
    private Button fromTime;
    private Button toDate;
    private Button toTime;


    boolean isNewEvent;

    private EventDialogListener mListener;

    public interface EventDialogListener {
        void onEditPressed(EventItem event);
    }

    public void setEventDialogListener(EventDialogListener eventDialogListener) {
        this.mListener = eventDialogListener;
    }

    public EditEventDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param event EventItem, null if new event.
     * @return A new instance of fragment EditEventDialogFragment.
     */
    public static EditEventDialogFragment newInstance(@Nullable EventItem event) {
        EditEventDialogFragment fragment = new EditEventDialogFragment();
        Bundle args = new Bundle();
        if (event != null) {
            args.putInt(EVENT_ID, event.getId());
            args.putString(EVENT_TITLE, event.getTitle());
            args.putString(EVENT_START, event.getStartTime().toString());
            args.putString(EVENT_END, event.getEndTime().toString());
            args.putString(EVENT_OWNER, event.getLocation());
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_AppCompat);
        if (getArguments() != null) {
            isNewEvent = false;
            mEventId = getArguments().getInt(EVENT_ID);
            mEventTitle = getArguments().getString(EVENT_TITLE);
            try {
                mEventStart = CalendarParser.parseCalendarFromString(getArguments().getString(EVENT_START));
                mEventEnd = CalendarParser.parseCalendarFromString(getArguments().getString(EVENT_END));
            } catch (ParseException e) {
                Log.e("EditEventDialogFragment", "Invalid start/end time passed to Fragment");
            }
            mEventOwner = getArguments().getString(EVENT_OWNER);
        } else {
            isNewEvent = true;
            initializeCalendars();
        }
    }

    private void initializeCalendars() {
        mEventStart = Calendar.getInstance();
        mEventEnd = Calendar.getInstance();
        mEventEnd.add(Calendar.HOUR_OF_DAY, 1);
    }

    private void initializePickers() {
        initializeDatePickers();
        initializeTimePickers();
    }

    private void initializeTimePickers() {
        initializeFromTimePicker();
        initializeToTimePicker();
    }

    private void initializeFromTimePicker() {
        TimePickerDialog.OnTimeSetListener fromTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    }
                };

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        fromTimeSetListener, mEventStart.get(Calendar.HOUR_OF_DAY),
                        mEventEnd.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });
    }

    private void initializeToTimePicker() {
        TimePickerDialog.OnTimeSetListener toTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    }
                };

        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        toTimeSetListener, mEventEnd.get(Calendar.HOUR_OF_DAY),
                        mEventEnd.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });
    }

    private void initializeDatePickers() {
        initializeFromDatePicker();
        initializeToDatePicker();
    }

    private void initializeFromDatePicker() {
        DatePickerDialog.OnDateSetListener fromDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    }
                };

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        fromDateSetListener, mEventStart.get(Calendar.YEAR),
                        mEventStart.get(Calendar.MONTH), mEventStart.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void initializeToDatePicker() {
        DatePickerDialog.OnDateSetListener toDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    }
                };

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        toDateSetListener, mEventStart.get(Calendar.YEAR),
                        mEventEnd.get(Calendar.MONTH), mEventEnd.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void setTitleText() {
        if (isNewEvent) {
            dialogTitle.setText("New event");
        } else {
            dialogTitle.setText("Edit event");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_event_dialog, container,
                false);

        ImageButton close = view.findViewById(R.id.event_input_cancel);
        TextView save = view.findViewById(R.id.event_input_save);
        dialogTitle = view.findViewById(R.id.event_input_title);
        fromDate = view.findViewById(R.id.start_date);
        fromTime = view.findViewById(R.id.start_time);
        toDate = view.findViewById(R.id.end_date);
        toTime = view.findViewById(R.id.end_time);
        eventTitle = view.findViewById(R.id.editTextEventTitle);

        close.setOnClickListener(this);
        save.setOnClickListener(this);

        setTitleText();
        initializePickers();

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.event_input_save:
                mEventTitle = eventTitle.getText().toString();
                EventItem eventItem;
                if (isNewEvent) {
                    eventItem = new EventItem(mEventTitle, mEventStart, mEventEnd);
                } else {
                    eventItem = new EventItem(mEventId, mEventTitle, mEventStart,
                            mEventEnd, mEventOwner);
                }
                mListener.onEditPressed(eventItem);
                dismiss();
                break;
            case R.id.event_input_cancel:
                dismiss();
                break;
        }
    }
}