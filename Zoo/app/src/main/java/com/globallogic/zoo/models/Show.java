package com.globallogic.zoo.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by GL on 26/03/2015.
 */
public class Show implements Serializable {
    private static List<Show> shows = new ArrayList<>();

    private long id;
    private String name;
    private List<Schedule> schedules;
    private int duration;
    private Calendar finalHour;

    public Show(long id, String name, List<Schedule> schedules, int duration) {
        this.id = id;
        this.name = name;
        this.schedules = schedules;
        this.duration = duration;
    }

    public Show(long id, String name, String initialHours, String duration) {
        this.id = id;
        this.name = name;
        this.schedules = new ArrayList<>();
        this.duration = Integer.valueOf(duration);

        String[] schedules = initialHours.split(",");

        for (int i = 0; i < schedules.length; i++) {
            String initialHour = schedules[i];
            this.schedules.add(new Schedule(initialHour, this.duration));
        }

    }

    public static Show fromJson(String str) throws JSONException {
        JSONObject jShow = new JSONObject(str);
        long id = jShow.getLong("id");

        if (getById(id) != null) {
            return getById(id);
        }

        String name = jShow.getString("name");

        List<Schedule> schedules = new ArrayList<>();
        JSONArray jSchedules = jShow.getJSONArray("schedules");
        int duration = jShow.getInt("duration");

        for (int i = 0; i < jSchedules.length(); i++) {
            schedules.add(new Schedule(jSchedules.getString(i), duration));
        }

        Show show = new Show(id, name, schedules, duration);
        shows.add(show);
        return show;
    }

    static public Show getById(long id) {
        if (id == -1 || shows.isEmpty()) {
            return null;
        }
        for (Show show: shows) {
            if (show.getId() == id) {
                return show;
            }
        }
        return null;
    }

    public static List<Show> getShows() {
        return shows;
    }

    public static void setShows(List<Show> shows) {
        Show.shows = shows;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Calendar getFinalHour() {
        return finalHour;
    }

    public void setFinalHour(Calendar finalHour) {
        this.finalHour = finalHour;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public String getSchedulesString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < schedules.size(); i++) {
            sb.append(schedules.get(i).getFinalHourString());
            if (i != schedules.size() - 1) {
                sb.append(',');
            }
        }

        return sb.toString();
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
