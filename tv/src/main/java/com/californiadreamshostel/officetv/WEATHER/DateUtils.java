package com.californiadreamshostel.officetv.WEATHER;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.californiadreamshostel.officetv.WEATHER.model.WeatherData;

import java.util.Calendar;
import java.util.List;

public class DateUtils {

    public static final int YEAR = 365;

    public static final int Y = Calendar.YEAR;
    public static final int M = Calendar.MONTH;
    public static final int D = Calendar.DAY_OF_YEAR;

    private static final Calendar now = Calendar.getInstance();
    private static final Calendar utility = Calendar.getInstance();

    /**
     * Finds the data point that is representing value *projection* days ahead of where we are now
     * @param data The non null, non empty list of DataPoints we want to search through
     * @param projection How many days ahead we want the value for
     * @return The data point representing the values *projection* days ahead of now
     */
    @Nullable
    public static WeatherData.Daily.DataPoints dailyFromProjection(
            @NonNull final List<WeatherData.Daily.DataPoints> data,
                                                           int projection){

        synchronized (DateUtils.class) { //Since we're using global calendar objects, we'll synchronize @ class level
            if (data.size() == 0)
                return null;

            now.setTimeInMillis(System.currentTimeMillis());

            //Offset now to its current floor value
            offsetToFloor(now);

            for (WeatherData.Daily.DataPoints d : data) {
                //Find each timestamp
                //Offset it to its floor just in case
                //See if it is as far away as we want it to be.

                utility.setTimeInMillis(d.getTimestamp());

                Log.i("DATE_TESTING", "TIMESTAMP OF CURRENT DATAPOINT: " + d.getTimestamp());

                //Offsetting the projection calendar to its floor millis (ITS MIDNIGHT)
                offsetToFloor(utility);

                if (utility.getTimeInMillis() < now.getTimeInMillis())
                    continue;

                Log.i("DATE_TESTING", "Offset the future data point");

                //If same year, simply subtract the datapoints current day and the day right now
                if (thisYear(utility) == thisYear(now)) {
                 Log.i("DATE_TESTING", "SAME YEAR. DIFF OF DAYS: " + String.valueOf(thisDay(utility) - thisDay(now)) );
                    if ((thisDay(utility) - thisDay(now)) == projection)
                        return d;
                    else continue;
                }

                //Handles when we're at the threshold of a new year.
                if (thisYear(utility) == thisYear(now) + 1)//If utility is one year ahead. E.G. jan 1, and now is dec 31st
                    if (((YEAR - thisDay(now)) + thisDay(utility)) == projection) //Subtract 365 and the current day, then add to utilities day
                        return d;
            }
            return null;
        }
    }

    @Nullable
    public static String getDayRep(final long millis){
        utility.setTimeInMillis(millis);
        final int doW = utility.get(Calendar.DAY_OF_WEEK);

        String rep = null;

        switch(doW){
            case Calendar.SUNDAY: rep = "Sunday"; break;
            case Calendar.MONDAY: rep = "Monday"; break;
            case Calendar.TUESDAY: rep = "Tuesday"; break;
            case Calendar.WEDNESDAY: rep = "Wednesday"; break;
            case Calendar.THURSDAY: rep = "Thursday"; break;
            case Calendar.FRIDAY: rep = "Friday"; break;
            case Calendar.SATURDAY: rep = "Saturday"; break;
        }

        return rep;
    }

    /**
     * Offsets the milliseconds for this date to be its lowest representation for the day
     * NOTE that this method assumes the Calendar already has a timestamp associated with it
     * @param c the calendar we want to set
     */
    private static void offsetToFloor(@NonNull final Calendar c){
        c.set(c.get(Y), c.get(M), c.get(D));
    }

    private static int thisYear(@NonNull final Calendar c){
        return c.get(Y);
    }

    private static int thisMonth(@NonNull final Calendar c){
        return c.get(M);
    }

    private static int thisDay(@NonNull final Calendar c){
        return c.get(D);
    }

}









