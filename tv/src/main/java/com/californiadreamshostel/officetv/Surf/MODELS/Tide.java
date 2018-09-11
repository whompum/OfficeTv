package com.californiadreamshostel.officetv.Surf.MODELS;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public final class Tide {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;
    private String day;
    private String gmt;
    private String hour;
    private String name;

    //Tide height expressed as feet
    private double tide;

    @Ignore
    private double tideMeters;

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getGmt() {
        return gmt;
    }

    public String getHour() {
        return hour;
    }

    public String getName() {
        return name;
    }

    public double getTide() {
        return tide;
    }

    public double getTideMeters() {
        return tideMeters;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setGmt(String gmt) {
        this.gmt = gmt;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTide(double tide) {
        this.tide = tide;
    }

    public void setTideMeters(double tideMeters) {
        this.tideMeters = tideMeters;
    }



    /**
    "date":"Wednesday Jun 27 2018",
            "day":"Wed",
            "gmt":"2018-6-27 7",
            "hour":"12AM",
            "name":"San Diego",
            "tide":3.3267716586000002,
            "tideMeters":1.014
    **/
}
