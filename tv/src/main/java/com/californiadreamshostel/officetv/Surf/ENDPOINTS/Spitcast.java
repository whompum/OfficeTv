package com.californiadreamshostel.officetv.Surf.ENDPOINTS;

import android.net.Uri;
import android.support.annotation.NonNull;

public class Spitcast {

    public static final String SCHEME = "http";
    public static final String HOST = "api.spitcast.com";
    public static final String PATH_API = "api";
    public static final String PATH_COUNTY = "county";

    public static final String PATH_COUNTY_NAME = "san-diego";

    //Spit cast Tide Query parameter
    public static final String PATH_TIDE = "tide";

    public static class UriBuilder{

        public Uri getUri(@NonNull final String dataType){

            final Uri.Builder b = new Uri.Builder();

            b.scheme(SCHEME)
             .authority(HOST)
             .appendPath(PATH_API)
             .appendPath(PATH_COUNTY)
             .appendPath(dataType)
             .appendPath(PATH_COUNTY_NAME);

        return b.build();
        }


    }

}
