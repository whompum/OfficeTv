package com.californiadreamshostel.officetv.Weather.ENDPOINTS;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.californiadreamshostel.officetv.Weather.API_KEYS;


public class DarkSky {

    public static final String SCHEME = "https";
    public static final String HOST = "api.darksky.net";
    public static final String ROOT_PATH = "forecast";

    public static final String BASE_URL = "https://api.darksky.net/forecast/";

    public static final String EXCLUDE_QUERY = "exclude";

    public static final String MINUTELY_EXCLUDE = "minutely";
    public static final String HOURLY_EXCLUDE = "hourly";
    public static final String CURRENTLY_EXCLUDE = "currently";
    public static final String DAILY_EXCLUDE = "daily";
    public static final String FLAGS_EXCLUDE = "flags";
    public static final String ALERTS_EXCLUDE = "alerts";

    public static String fetchExcludes(@NonNull final String... excludes){
        final StringBuilder builder = new StringBuilder();

        for(int i = 0; i < excludes.length; i++){

            final String param = excludes[i];

               builder.append(param);

             //If we're not at the last param, append a separator
            if(i != excludes.length-1)
                builder.append(",");

        }

    return builder.toString();
    }


    public static class UriBuilder{

        public Uri getQueryUri(@NonNull final String longLat, @Nullable final String... excludes){

            final Uri.Builder b = new Uri.Builder();


            b.scheme(SCHEME)
             .authority(HOST)
             .appendPath(ROOT_PATH)
             .appendPath(API_KEYS.API_KEY_WEATHER_DARK_SKY)
             .appendPath(longLat);

            if(excludes != null)
                if(excludes.length > 0)
                    b.appendQueryParameter(EXCLUDE_QUERY, fetchExcludes(excludes));

            return b.build();
        }

    }

}
