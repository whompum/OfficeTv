package com.californiadreamshostel.officetv.WEATHER.MODELS.DARKSKY;

import android.support.annotation.Nullable;

public class DarkSkyWeather {

    private double latitude;
    private double longitude;
    private String timezone;

    private Currently currently;
    private Daily daily;
    private Minutely minutely;

    private int offset;

    private int getOffset(){
        return  offset;
    }

    @Nullable
    public Daily getDaily(){
        return daily;
    }

    @Nullable
    public Currently getCurrently(){
        return currently;
    }

    @Nullable
    public Minutely getMinutely(){
        return minutely;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public String getTimezone(){
        return timezone;
    }

}
