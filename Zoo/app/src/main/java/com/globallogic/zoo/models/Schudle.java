package com.globallogic.zoo.models;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by GL on 26/03/2015.
 */
public class Schudle implements Serializable {

    private static final long serialVersionUID = 4138897544516350399L;
    private static GregorianCalendar calendar = new GregorianCalendar();
    private Date initialHour;
    private Date finalHour;

    public Schudle(Date initialHour, Date finalHour) {
        this.initialHour = initialHour;
        this.finalHour = finalHour;
    }

    public void setInitialHour(Date initialHour) {
        this.initialHour = initialHour;
    }

    public void setFinalHour(Date finalHour) {
        this.finalHour = finalHour;
    }

    public Date getInitialHour() {
        return initialHour;
    }

    public Date getFinalHour() {
        return finalHour;
    }

    public String getDay() {
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
