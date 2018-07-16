package com.californiadreamshostel.officetv.CONTROLLERS.weather$surf;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.californiadreamshostel.officetv.NETWORKING.DOWNLOADING.DataDownloadService;
import com.californiadreamshostel.officetv.NETWORKING.DOWNLOADING.DownloadIntent;
import com.californiadreamshostel.officetv.PARSING.IParsingFinishedObserver;
import com.californiadreamshostel.officetv.SURF.ENDPOINTS.MagicSeaWeed;
import com.californiadreamshostel.officetv.SURF.ENDPOINTS.Spitcast;
import com.californiadreamshostel.officetv.SURF.MODELS.Tide;
import com.californiadreamshostel.officetv.SURF.MODELS.WindAndSwell;
import com.californiadreamshostel.officetv.SURF.TideParsingStrategy;
import com.californiadreamshostel.officetv.SURF.WindSwellParsingStrategy;
import com.californiadreamshostel.officetv.WEATHER.model.DarkSKyDataPointAdapter;
import com.californiadreamshostel.officetv.WEATHER.DarkSkyParsingStrategy;
import com.californiadreamshostel.officetv.WEATHER.ENDPOINTS.DarkSky;
import com.californiadreamshostel.officetv.WEATHER.model.DarkSkyWeather;
import com.californiadreamshostel.officetv.WEATHER.model.WeatherDataProxy;
import com.californiadreamshostel.officetv.WEATHER.model.WeatherProvider;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.californiadreamshostel.officetv.SURF.ENDPOINTS.MagicSeaWeed.SWELL_MAXIMUM;
import static com.californiadreamshostel.officetv.SURF.ENDPOINTS.MagicSeaWeed.SWELL_MINIMUM;
import static com.californiadreamshostel.officetv.SURF.ENDPOINTS.MagicSeaWeed.TIMESTAMP;
import static com.californiadreamshostel.officetv.SURF.ENDPOINTS.MagicSeaWeed.WINDSPEED;
import static com.californiadreamshostel.officetv.SURF.ENDPOINTS.Spitcast.PATH_TIDE;
import static com.californiadreamshostel.officetv.WEATHER.ENDPOINTS.DarkSky.ALERTS_EXCLUDE;
import static com.californiadreamshostel.officetv.WEATHER.ENDPOINTS.DarkSky.FLAGS_EXCLUDE;
import static com.californiadreamshostel.officetv.WEATHER.ENDPOINTS.DarkSky.MINUTELY_EXCLUDE;

/**
 * Central class responsible for handling the downloading and caching of
 * weather and surf data, so that data can be used throughout the application.
 *
 * This class lacks a few key features of OOP, and OOD. The endpoints it references are
 * hardcoded into its state, so if i wanted to change the provider for the Weather information
 * i'd have to add data specific to that Endpoint. (URI, and a specific Parsing Strategy).
 * Another issue of this class is how it treats downloaded data. Ideally i'd like to merge
 * the downloaded data into a common model that exposes data for various weather/surf conditions,
 * so it becomes easier to insert into a cache. This class is a WIP made to be more of a dump class
 * that i'll get back to.
 *
 * TODO merge surf model data from an API into a common interface for easier caching.
 * TODO use only a single Thread object to insert my data instead of resolving multiple threads @ Runtime.
 *
 */
public class WeatherSurfController extends JobService{

    private enum SOURCE_TYPE{ WEATHER, TIDE, WAVE_WIND, WATER_TEMPERATURE, NO_SABES_NADA }

    public static final long INTERVAL = TimeUnit.MINUTES.toMillis(2);

    public static final long MAXIMUM_XECUTE_DELAY = TimeUnit.SECONDS.toMillis(5);

    public static final int ID = 0;

    public static final int NO_MATCH = 1;
    public static final int MATCH = 0;

    public static final long WEATHER_INTERVAL = TimeUnit.MINUTES.toMillis(61); //One hour
    public static final long TIDE_INTERVAL = TimeUnit.DAYS.toMillis(1); //One day
    public static final long WIND_WAVE_INTERVAL = TimeUnit.DAYS.toMillis(1); //One day

    private final Uri WEATHER_URI = new DarkSky.UriBuilder().getQueryUri("32.80, -117.24", FLAGS_EXCLUDE, ALERTS_EXCLUDE, MINUTELY_EXCLUDE);
    private final Uri TIDE_URI = new Spitcast.UriBuilder().getUri(PATH_TIDE);
    private final Uri WIND_WAVE_URI = new MagicSeaWeed.UriBuilder().getUri(TIMESTAMP, SWELL_MAXIMUM, SWELL_MINIMUM, WINDSPEED);

    private final ResultReceiver WEATHER_RECEIVER = new DarkSkyParsingStrategy(new IParsingFinishedObserver() {
        @Override
        public void onDataParsed(Object data) {

            WeatherRepo.obtain(WeatherSurfController.this).renewCache(new WeatherDataProxy().addProvider(
                            new WeatherProvider().setAdapter(new DarkSKyDataPointAdapter((DarkSkyWeather)data))
                    ).getWeatherData());

            TimestampSaverUtils.saveTimestamp(WEATHER_URI.toString(),
                    getSharedPreferences(TimestampSaverUtils.NAME, Context.MODE_PRIVATE).edit());

        }
    });

    private final ResultReceiver TIDE_RECEIVER = new TideParsingStrategy(new IParsingFinishedObserver() {
        @Override
        public void onDataParsed(Object data) {
            TideRepo.obtain(WeatherSurfController.this)
            .renewCache( (Tide[])data );

            TimestampSaverUtils.saveTimestamp(TIDE_URI.toString(),
                    getSharedPreferences(TimestampSaverUtils.NAME, Context.MODE_PRIVATE).edit());

        }
    });

    private final ResultReceiver WIND_WAVE_RECEIVER = new WindSwellParsingStrategy(new IParsingFinishedObserver() {
        @Override
        public void onDataParsed(Object data) {
            WindSwellRepo.obtain(WeatherSurfController.this)
                    .renewCache( (WindAndSwell[]) data );

            TimestampSaverUtils.saveTimestamp(WIND_WAVE_URI.toString(),
                    getSharedPreferences(TimestampSaverUtils.NAME, Context.MODE_PRIVATE).edit());
        }
    });

    private DownloadIntent[] downloads;

    @Override
    public boolean onStartJob(JobParameters params) {

        if(downloads == null)
            prepareDownloadIntents();

        for(int a = 0; a  < downloads.length; a++) {

            final Uri download = downloads[a].getUri();

            final long lastUpdate = TimestampSaverUtils.getTimestamp(download.toString(), getUpdates());

            //Check if the update happened past our interval for the uri type.
            //If so then we shall update.

            //Check if current time is lastUpdate + interval ago

            long interval = Long.MAX_VALUE;

            if(download.compareTo(WEATHER_URI) == MATCH)
                interval = WEATHER_INTERVAL;

            if(download.compareTo(TIDE_URI) == MATCH)
                interval = TIDE_INTERVAL;

            if(download.compareTo(WIND_WAVE_URI) == MATCH)
                interval = WIND_WAVE_INTERVAL;

            if(System.currentTimeMillis() >= (lastUpdate + interval) )
                startService(downloads[a]);
        }

        start(this);  // Reschedules the Job.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("WEATHER_SURF_JOB", "JOB STOPPED");
        return false;
    }


    /**
     * Prepares the DownloadIntent array object
     * with the various Endpoints to download.
     */
    private void prepareDownloadIntents(){
        downloads = new DownloadIntent[]{
                fetchIntent(WEATHER_URI, WEATHER_RECEIVER),
                fetchIntent(TIDE_URI, TIDE_RECEIVER),
                fetchIntent(WIND_WAVE_URI, WIND_WAVE_RECEIVER)
        };
    }

    private DownloadIntent fetchIntent(@NonNull final Uri uri, @NonNull final ResultReceiver receiver){
        return new DownloadIntent(this, uri, receiver);
    }

    private SharedPreferences getUpdates(){
        return getSharedPreferences(TimestampSaverUtils.NAME, Context.MODE_PRIVATE);
    }

    public static void start(@NonNull final Context context){
        ((JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE))
                .schedule(makeJobInfo(context.getApplicationContext()));
    }

    private static JobInfo makeJobInfo(@NonNull final Context context){
        return new JobInfo.Builder(ID, new ComponentName(context, WeatherSurfController.class))
                .setMinimumLatency(MAXIMUM_XECUTE_DELAY)
                .setOverrideDeadline(INTERVAL)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
    }

}










