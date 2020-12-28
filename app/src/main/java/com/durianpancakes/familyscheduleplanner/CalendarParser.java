package com.durianpancakes.familyscheduleplanner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarParser {
    public static Calendar parseCalendarFromString(String calString) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",
                Locale.getDefault());
        cal.setTime(sdf.parse(calString));

        return cal;
    }
}
