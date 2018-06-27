package com.californiadreamshostel.officetv.SURF;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.californiadreamshostel.officetv.WEATHER.API_KEYS;

public class SURF_CONSTANTS {


    public static class SPITCAST {
        //search query for Spitcast
        public static final String SPIT_CAST_COUNTY = "san-diego";

        //Base url for Spitcast
        public static final String SPIT_CAST_BASE = "http://api.spitcast.com/api/county/";

        //Water Temperature query parameter
        public static final String SPIT_CAST_WATER_TEMP = "water-temperature";

        //Spit cast Tide Query parameter
        public static final String SPIT_CAST_TIDE = "tide";


        public static String getQuery(@NonNull final String queryType){
            return SPIT_CAST_BASE + queryType + SPIT_CAST_COUNTY;
        }
    }


    public static class MAGICSEAWEED {
        //FOR USE ONLY WITH MAGIC SEA WEED!!!
        public static final String SURF_PACIFIC_BEACH_LOCATION_ID = "663";
        public static final String MSW_BASE = "http://magicseaweed.com/api/";
        public static final String MSW_QUERY = "/forecast/?spot_id=" + SURF_PACIFIC_BEACH_LOCATION_ID;

        public static final String MSW_URL = MSW_BASE + API_KEYS.API_KEY_MSW +
                MSW_QUERY;

        public static final String MSW_FIELDS_BASE = "&fields=";
        public static final String MSW_TIMESTAMP = "localTimestamp";
        public static final String MSW_SWELL_MIN = "swell.minBreakingHeight";
        public static final String MSW_SWELL_MAX = "swell.maxBreakingHeight";
        public static final String MSW_WIND_SPEED = "wind.speed";

        public static String getQuery(@Nullable String... params){
            return MSW_URL + resolveParams(params);
        }

        private static String resolveParams(final String... params){
            if(params == null)
                return "";


            final String sep = ",";

            final StringBuilder builder = new StringBuilder();

            builder.append(MSW_FIELDS_BASE);

            for(int i =0; i < params.length; i++) {
                builder.append(params[i]);
                if(i!= params.length-1)
                    builder.append(sep);
            }

            return builder.toString();
        }

    }

}
