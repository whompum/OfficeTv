package com.californiadreamshostel.officetv.Surf.ENDPOINTS;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.californiadreamshostel.officetv.Weather.API_KEYS;

public class MagicSeaWeed {

    public static final String SCHEME = "http";
    public static final String HOST = "magicseaweed.com";
    public static final String ROOT_PATH = "api";
    public static final String FORECAST_PATH = "forecast";

    public static final String LOCATION_PATH = "663";

    public static final String FIELDS_QUERY = "fields";
    public static final String SPOT_QUERY = "spot_id";

    public static final String TIMESTAMP = "localTimestamp";
    public static final String SWELL_MINIMUM = "swell.minBreakingHeight";
    public static final String SWELL_MAXIMUM = "swell.maxBreakingHeight";
    public static final String WINDSPEED = "wind.speed";

    private static String resolveParams(final String... params){
        if(params == null)
            return "";

        final StringBuilder builder = new StringBuilder();

        for(int i =0; i < params.length; i++){

            final String param = params[i];

            builder.append(param);

            if(i != params.length-1)
                builder.append(",");

        }

        return builder.toString();
    }


    public static class UriBuilder{

        public Uri getUri(@Nullable final String... params){

            final Uri.Builder builder = new Uri.Builder();

            builder.scheme(SCHEME)
                    .authority(HOST)
                    .appendPath(ROOT_PATH)
                    .appendPath(API_KEYS.API_KEY_MSW)
                    .appendPath(FORECAST_PATH)
                    .appendQueryParameter(SPOT_QUERY, LOCATION_PATH);

            if(params !=null)
                if(params.length > 0)
                    builder.appendQueryParameter(FIELDS_QUERY, resolveParams(params));

            return builder.build();
        }


    }


}
