package com.californiadreamshostel.officetv.Surf;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.californiadreamshostel.officetv.Surf.MODELS.WindAndSwell;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WindSwellUtils {

    @Nullable
    public static WindAndSwell findClosest(@NonNull final List<WindAndSwell> data){

        Log.i("WIND_SWELL", "DATA SIZE: " + data.size());

        //Convert our milli time to seconds
        final long currTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        WindAndSwell windAndSwell = null;

        for(int i = 0; i < data.size(); i++)
            if(windAndSwell == null || data.get(i).getLocalTimestamp() <= currTime)
                windAndSwell = data.get(i);

        return windAndSwell;
    }

}











