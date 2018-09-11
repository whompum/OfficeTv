package com.californiadreamshostel.officetv.Weather.model;

import static com.californiadreamshostel.officetv.Weather.model.WeatherProvider.*;


import android.support.annotation.NonNull;

public class DataPoint {
    private String summary;
    private Double windSpeed;
    private Double temperature;
    private long millis;

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getSummary() {
        return summary;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Object getDataFor(@NonNull final String fieldName){

        if(fieldName.equals(F_CONDITION))
            return summary;

        if(fieldName.equals(F_WIND_SPEED))
            return windSpeed;

        if(fieldName.equals(F_TEMPERATURE))
            return temperature;

        if(fieldName.equals(F_TIMESTAMP))
            return millis;

        return null;
    }

}
