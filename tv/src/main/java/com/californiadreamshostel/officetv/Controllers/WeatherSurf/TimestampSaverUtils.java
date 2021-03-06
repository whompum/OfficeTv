package com.californiadreamshostel.officetv.Controllers.WeatherSurf;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class TimestampSaverUtils {

    public static final String NAME = "timestampSaver";

    public static void saveTimestamp(@NonNull final String key, @NonNull final SharedPreferences.Editor e){
        e.putLong(key, System.currentTimeMillis()).apply();
    }

    public static long getTimestamp(@NonNull final String key, @NonNull final SharedPreferences preferences){
        return preferences.getLong(key, Long.MIN_VALUE);
    }
}
