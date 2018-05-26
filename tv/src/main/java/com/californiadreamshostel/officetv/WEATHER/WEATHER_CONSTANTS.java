package com.californiadreamshostel.officetv.WEATHER;

import android.support.annotation.NonNull;

public class WEATHER_CONSTANTS {

    public static final String BASE_DARK_SKY_URL = "https://api.darksky.net/forecast/";

    //Append to the end of a REST string to exclude all but the daily information
    public static final String DAILY_ONLY_URL = "?exclude=currently,minutely,hourly,alerts,flags";

    public static final String DAILY_CURRENTLY_URL = "?exclude=minutely,hourly,alerts,flags";

    public static String getQuery(@NonNull Location location, @NonNull String appendableExcludes){

        return BASE_DARK_SKY_URL + API_KEYS.API_KEY_WEATHER_DARK_SKY + "/" +
        location.getLat() + "," + location.getLon() + appendableExcludes;

    }

    public static final class Location{
        double lat, lon;
        public Location(double lat, double lon){
            this.lat = lat;
            this.lon = lon;
        }

        String getLat(){
            return String.valueOf(lat);
        }

        String getLon(){
            return String.valueOf(lon);
        }

    }

}
