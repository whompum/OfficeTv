package com.californiadreamshostel.officetv.Weather;

import android.net.Uri;
import android.support.annotation.NonNull;

public class API_KEYS {

    public static final String NO_API_KEY = "No-Api-Key"; //Random String

    public static final String dark_sky_api(){
        //In a non build, return Empty
        return NO_API_KEY;
    }

    public static final String magic_sea_weed_api(){
        return NO_API_KEY;
    }

    public static final String spitcast_api_key(){
        //return "";  //Return "" when we want to fetch data;
        return NO_API_KEY;
    }

    public static final boolean hasKey(@NonNull final Uri uri){

        for(String path: uri.getPathSegments())
            if(path.equals(NO_API_KEY))
                return false;

        return true;
    }

}
