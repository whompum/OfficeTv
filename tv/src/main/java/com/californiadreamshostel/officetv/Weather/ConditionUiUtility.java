package com.californiadreamshostel.officetv.Weather;

;
import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.R;

public class ConditionUiUtility {


    public static final int DERP = -1;
    public static final int NOT_SUPPORTED = DERP << 1;

    public static int getRepresentation(@NonNull final String c){

        switch(c){
            case "clear-day": return R.drawable.sun_image;
            case "clear-night":
            case "partly-cloudy-night": return R.drawable.night_moon;
            case "rain": return R.drawable.rain_image;
            case "wind": return R.drawable.windy_image;
            case "fog": return R.drawable.foggy_image;
            case "partly-cloudy-day":
            case "cloudy": return R.drawable.cloudy_image;
            case "snow": case "sleet": return NOT_SUPPORTED;
            default: return DERP;
        }

    }

}
