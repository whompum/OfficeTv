package com.californiadreamshostel.officetv.Surf;

import android.icu.text.NumberFormat;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.californiadreamshostel.officetv.Surf.MODELS.Tide;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TideUtils {

    //must be appended to a daily floor value (its midnight) for these values to be usable
    public static final int FLOOR = 6; //6 AM
    public static final int CIEL = 21; //Military Time: 9PM

    public static final String AM = "AM";
    public static final String PM = "PM";

    @Nullable
    public static Tide highestTideFrom(@NonNull final List<Tide> tides){
        return highestTideFrom(tides, FLOOR, CIEL);
    }

    @Nullable
    public static Tide highestTideFrom(@NonNull final List<Tide> tides, @NonNull final int floor,
                                       @NonNull final int ciel){

        if(tides.size() == 0)
            return null;

        Tide tide = null;

        for(Tide t: tides){

            final String hour = t.getHour();
            String hourStr;
            int intHour = -1;

            //Remove AM/PM from string
            //Convert hour to military time (Add 12 if PM)

            //If less than floor/ greater than ciel, continue;

            //else if wantedvalue is greater than tides value, set it

            final String pattern = hour.contains(PM) ? PM : AM;

            hourStr = hour.replaceAll(pattern, "");

            try{
                intHour = Integer.parseInt(hourStr);
            }catch (NumberFormatException e){
                Log.i("TIDE_LIVE_DATA", "ERROR paring: " + e.getMessage());
            }

            if(pattern.equals(PM) && intHour != 12) intHour += 12;

            if( (intHour < floor || intHour > ciel) || hour.equals("12AM") )//If intHour less than floor, and greater than ciel. Skip
                continue;


            if(tide == null || t.getTide() > tide.getTide())
                tide = t;

        }

        return tide;
    }


}














