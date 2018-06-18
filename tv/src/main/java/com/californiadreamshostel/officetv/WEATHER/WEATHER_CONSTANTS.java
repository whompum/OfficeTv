package com.californiadreamshostel.officetv.WEATHER;

import android.support.annotation.DrawableRes;
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

    public static final class Conditions{

        public static final String CONDITION_CLEAR_DAY = "clear-day";
        public static final String CONDITION_CLEAR_NIGHT = "clear_night";
        public static final String CONDITION_RAIN = "rain";
        public static final String CONDITION_SNOW = "snow";
        public static final String CONDITION_SLEET = "sleet";
        public static final String CONDITION_FOG = "fog";
        public static final String CONDITION_WIND = "wind";
        public static final String CONDITION_CLOUDY = "cloudy";
        public static final String CONDITION_PARTYLY_CLOUDY_DAY = "partly-cloudy-day";
        public static final String CONDITION_PARTYLY_CLOUDY_NIGHT = "partly-cloudy-night";

        //Remove this responsibility...
        @DrawableRes
        public static int getIcon(final String condition){
            return 0;
        }

    }
/**
 *  clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.
 */
}
