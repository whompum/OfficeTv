package com.californiadreamshostel.officetv.SURF;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.californiadreamshostel.officetv.NETWORKING.JSON.JsonDownloadTask;
import com.californiadreamshostel.officetv.NETWORKING.JSON.ResponseListener;
import com.californiadreamshostel.officetv.SURF.MODELS.HighTide;
import com.californiadreamshostel.officetv.SURF.MODELS.SurfData;
import com.californiadreamshostel.officetv.SURF.MODELS.WaterTemperature;
import com.californiadreamshostel.officetv.SURF.MODELS.WindSwellHeight;
import com.californiadreamshostel.officetv.TIMING.TickTockerTasker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.TimeZone;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SurfUpdator extends TickTockerTasker{

    public static final String HIGH_TIDE_KEY = "HighTide";
    public static final String WATER_INFO_KEY = "WaterInfo";
    public static final String WIND_SWELL_HEIGHT_KEY = "WindSwellHeight";


    private SurfData.Builder surfDataBuilder;
    private Set<SurfApiMetaInfo> downloadTasks = new HashSet<>(3);

    private OnSurfData surfDataListener;

    public interface OnSurfData{
        void onSurfDataFetched(@NonNull final SurfData surfingData);
    }

    public SurfUpdator(final long tickDur, final Map<String, String> urls, @NonNull final OnSurfData surfDataListener){
        super(tickDur);

        this.surfDataListener = surfDataListener;

        if(urls.containsKey(HIGH_TIDE_KEY))
            downloadTasks.add(
                    new SurfApiMetaInfo(
                            new JsonDownloadTask(new HighTideListener()), urls.get(HIGH_TIDE_KEY)));

        if(urls.containsKey(WATER_INFO_KEY))
            downloadTasks.add(
                    new SurfApiMetaInfo(
                            new JsonDownloadTask(new WaterTemperatureListener()), urls.get(WATER_INFO_KEY)));

        if(urls.containsKey(WIND_SWELL_HEIGHT_KEY))
            downloadTasks.add(
                    new SurfApiMetaInfo(new JsonDownloadTask(new WindSwellHeightListener()), urls.get(WIND_SWELL_HEIGHT_KEY)));

    }

    @Override
    public void update() {
        //Return the Data that will be the most relevant.

        //Fetch all the Surfing Data

        surfDataBuilder = new SurfData.Builder();

        final Iterator<SurfApiMetaInfo> metaInfoIterator = downloadTasks.iterator();

        while(metaInfoIterator.hasNext()) {
            final SurfApiMetaInfo metaInfo = metaInfoIterator.next();
            metaInfo.downloadTask.execute(metaInfo.url);
        }
    }

    private void notifyDataReady(){
       surfDataListener.onSurfDataFetched(surfDataBuilder.create());
    }


    private static class SurfApiMetaInfo{
        JsonDownloadTask downloadTask;
        String url;
        SurfApiMetaInfo(final JsonDownloadTask downloadTask, final String  url){
            this.downloadTask = downloadTask;
            this.url = url;
        }

    }

    /***************************************************
     *TODO refractor below code to a much better design*
     **************************************************/


    /****
     *
     *
     * FOR THE LOVE OF ALL THAT IS HOLY, PLEASE REFRACTOR THIS CODE
     *
     * SOME THINGS TO DO INCLUDE: Adding Adapter classes to convert these json strings into usable models
     * Removing the Dependency on json value semantics, for data parsing E.G.
     *
     * " boolean isMorning = jsonValueHourField.contains("AM") "
     *
     *
     *
     */

    private final class HighTideListener implements ResponseListener{
        @Override
        public void onResponse(@NonNull String data) {
            Log.i("SURF_DATA", "High Tide Data: " + data);

            final Gson modelFactory = new Gson();

            final Collection<HighTide> tides =
                    modelFactory.fromJson(data, new TypeToken<Collection<HighTide>>(){}.getType());

            //Find the tide between a certain period of time, with the highest result

            //Surf Times: 6AM - 9PM
            //We find the highest surf time data between these times, since we want the best surf times
            //People are most likely to surf;
            if(tides == null) return;

            final Iterator<HighTide> tideIterator = tides.iterator();

            double highestTide = Double.MIN_VALUE;
            HighTide bestTide = new HighTide();

            while(tideIterator.hasNext()){

                final HighTide tide = tideIterator.next();

                final String hour = tide.getHour();

                final boolean isMorning = hour.contains("AM");

                int integerHour = Integer.MIN_VALUE;

                if(hour.length() == 4) //for hour values with 10^2 time (10/11/12)
                    integerHour = Integer.parseInt(hour.substring(0, 2));
                else //For hour values with 10^1 (1/2/3..9)
                    integerHour = Integer.parseInt(hour.substring(0,1));

                Log.i("SURF_DATA", "The integer value of the hour is: " + String.valueOf(integerHour));

                if(isMorning) //para dia
                    if(integerHour >= 6 & integerHour <= 11)  //If time is between 6AM, and 11AM
                        if (tide.getTide() > highestTide) {
                            highestTide = tide.getTide();
                            bestTide = tide;
                        }

                if(!isMorning) //es notches
                    if(integerHour < 10 | integerHour == 12) //If the time is between 12PM, or
                        if(tide.getTide() > highestTide) {
                            highestTide = tide.getTide();
                            bestTide = tide;
                        }



            }

            surfDataBuilder.setTideInfo(bestTide);

            if(surfDataBuilder.isReady()) //If is ready, notify
                notifyDataReady();

        } //End of response method
    }

    private final class WaterTemperatureListener implements ResponseListener{
        @Override
        public void onResponse(@NonNull String data) {
          //    Log.i("SURF_DATA", "Water Temperature Data: " + data);

           final Gson modelFactory = new Gson();
           surfDataBuilder.setWaterInfo(modelFactory.fromJson(data, WaterTemperature.class));

           if(surfDataBuilder.isReady())
               notifyDataReady();
        }
    }


    private final class WindSwellHeightListener implements ResponseListener{
        @Override
        public void onResponse(@NonNull String data) {
            Log.i("SURF_DATA", "Wind Swell Height Data: " + data);
            //Find the data that is the closest trailing in terms of time


            final Gson modelFactory = new Gson();

            final Collection<WindSwellHeight> windSwellData =
                    modelFactory.fromJson(data, new TypeToken<Collection<WindSwellHeight>>(){}.getType());

          //Api uses seconds to represent time. Convert current Millis, to Seconds.
          long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

          WindSwellHeight windSwellHeight = new WindSwellHeight();

          final Iterator<WindSwellHeight> dataIterator = windSwellData.iterator();

          long bestTime = Long.MIN_VALUE;

          while(dataIterator.hasNext()){

              final WindSwellHeight surfData = dataIterator.next();

              final long millis = surfData.getLocalTimestamp();

              if(millis > currentTime)
                  break;

              else if(millis > bestTime){
                  bestTime = millis;
                  windSwellHeight = surfData;
              }

          }

          surfDataBuilder.setSwellHeight(windSwellHeight);


          final Calendar timestamp = Calendar.getInstance(TimeZone.getTimeZone("PST"));
          timestamp.setTimeInMillis(TimeUnit.SECONDS.toMillis(bestTime));

          int hour = timestamp.get(Calendar.HOUR);

          if(hour == 0)
              hour = 12;

          final int AM_PM = timestamp.get(Calendar.AM_PM);

          final String hourStr = String.valueOf(hour) + ( (AM_PM == Calendar.AM) ? "AM" : "PM" );

          Log.i("SURF_DATA","The millisecond value of best time is: " + TimeUnit.SECONDS.toMillis(bestTime));

          final String msg = "Fetching data for day " + String.valueOf(timestamp.get(Calendar.DAY_OF_MONTH)) +
          " of this month at the hour: " + hourStr;

            Log.i("SURF_DATA", msg);



          if(surfDataBuilder.isReady())
              notifyDataReady();

        }
    }

}








