package com.globallogic.zoo.models;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by GL on 09/04/2015.
 */
public class Schedule {
    private static GregorianCalendar calendar = new GregorianCalendar();

    private Date initialHour;
    private Date finalHour;

    public Schedule(String initialHour, int duration) {
        String[] schedule = initialHour.split(":");

        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(schedule[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(schedule[1]));
        this.initialHour = calendar.getTime();
        calendar.add(Calendar.MINUTE, duration);
        this.finalHour = calendar.getTime();
    }

    public Date getInitialHour() {
        return initialHour;
    }

    public Date getFinalHour() {
        return finalHour;
    }

    public String getDayOfTheWeek() {
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
        String weekdays[] = dfs.getWeekdays();

        calendar.setTime(initialHour);
        return weekdays[calendar.get(Calendar.DAY_OF_WEEK)];
    }

    public String getInitialHourString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(initialHour);
    }

    public String getFinalHourString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(finalHour);
    }

    public static Date addOneHoursToDate(Date date, int hours) {

        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);

        return calendar.getTime();
    }
}