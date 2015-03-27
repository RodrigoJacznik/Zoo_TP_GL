package com.globallogic.zoo.models;

import java.io.Serializable;

/**
 * Created by GL on 26/03/2015.
 */
public class Schudle implements Serializable {

    private static final long serialVersionUID = 4138897544516350399L;
    private String day;
    private String initialHour;
    private String finalHour;

    public Schudle(String day, String initialHour, String finalHour) {
        this.day = day;
        this.initialHour = initialHour;
        this.finalHour = finalHour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getInitialHour() {
        return initialHour;
    }

    public void setInitialHour(String initialHour) {
        this.initialHour = initialHour;
    }

    public String getFinalHour() {
        return finalHour;
    }

    public void setFinalHour(String finalHour) {
        this.finalHour = finalHour;
    }
}
