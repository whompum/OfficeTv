package com.californiadreamshostel.officetv.SURF.MODELS;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

//Represents Water Temperature Data (SPITCAST)
@Entity
public class WaterTemperature {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private double celsuis;
    private double fahrenheit = 0;
    private String wetsuit;

    public int getId() {
        return id;
    }

    public double getCelsuis() {
        return celsuis;
    }

    public double getFahrenheit() {
        return fahrenheit;
    }

    public String getWetsuit() {
        return wetsuit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCelsuis(double celsuis) {
        this.celsuis = celsuis;
    }

    public void setFahrenheit(double fahrenheit) {
        this.fahrenheit = fahrenheit;
    }

    public void setWetsuit(String wetsuit) {
        this.wetsuit = wetsuit;
    }
}
