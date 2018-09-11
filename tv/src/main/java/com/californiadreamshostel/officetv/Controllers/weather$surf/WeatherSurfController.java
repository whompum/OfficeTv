package com.californiadreamshostel.officetv.Controllers.weather$surf;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.californiadreamshostel.officetv.Networking.Downloading.DownloadIntent;
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
 *
 */
public class WeatherSurfController extends JobService{

    public static final int ID = 0;
    public static final long LAUNCH_INTERVAL = 1800000L; //Thirty Minutes to re-run this service

    public static final long DAILY_INTERVAL = 86400000L; //One Day
    public static final long THIRTY_MINUTES_INTERVAL = LAUNCH_INTERVAL  ; //Thirty Minutes interval

    public static final long MAXIMUM_XECUTE_DELAY = 5000L; //5 Seconds

    public static final long THIRTY_MIN_GRACE_PERIOD = TimeUnit.MINUTES.toMillis(5);
    public static final long DAILY_GRACE_PERIOD = TimeUnit.MINUTES.toMillis(10);

    public static final int URI_MATCH = 0;

    private final Uri BULK_WEATHER = new DarkSky.UriBuilder().getQueryUri("32.80, -117.24", FLAGS_EXCLUDE, ALERTS_EXCLUDE, MINUTELY_EXCLUDE);
    private final Uri TIDE_URI = new Spitcast.UriBuilder().getUri(PATH_TIDE);
    private final Uri WIND_WAVE_URI = new MagicSeaWeed.UriBuilder().getUri(TIMESTAMP, SWELL_MAXIMUM, SWELL_MINIMUM, WINDSPEED);

    private final ResultReceiver WEATHER_RECEIVER = new DarkSkyParsingStrategy(new IParsingFinishedObserver() {
        @Override
        public void onDataParsed(Object data) {

            WeatherRepo.obtain(WeatherSurfController.this).renewCache(new WeatherDataProxy().addProvider(
                            new WeatherProvider().setAdapter(new DarkSKyDataPointAdapter((DarkSkyWeather)data))
                    ).getWeatherData());

            TimestampSaverUtils.saveTimestamp(BULK_WEATHER.toString(),
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

        for(int a = 0; a  < downloads.length; a++)
            if(shouldUpdate(downloads[a].getUri()))
                startService(downloads[a]);

        start(this);  // Reschedules the Job.
        return false;
    }

    /**
     * Determines if we should renew the data the key represents.
     * This method also takes a grace period into account. Meaning
     * if we're N minutes away from need to update something, then we'll just update it.
     *
     * NOTE: Grace period serves to decrease the volume of the interval.
     * This should help us with regression bugs caused by slow time-leaks.
     * (Time leaks occur because we're trying to run our updates around a clock, but
     * are NOT gauranteed they're ran that way)
     *
     * @param key The value we want to check to update for
     * @return si o no
     */
    private boolean shouldUpdate(@NonNull final Uri key){

        long lastUpdate = TimestampSaverUtils.getTimestamp(key.toString(), getUpdates());
        long grace = DAILY_GRACE_PERIOD;
        long interval = DAILY_INTERVAL;

        //Current weather is updated more frequently that once daily.
        if(key.compareTo(BULK_WEATHER) == URI_MATCH){
            grace = THIRTY_MIN_GRACE_PERIOD;
            interval = THIRTY_MINUTES_INTERVAL;
        }


        //If the current time is greater or equal to the lastupdate plus its interval minus the grace
        final boolean shouldUpdate = System.currentTimeMillis() >= ( (lastUpdate+interval) - grace);

        Log.i("API_TIMING_FIX", "SHOULD UPDATE: " + key.toString() + " : " + String.valueOf(shouldUpdate));
        return shouldUpdate;
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
                fetchIntent(BULK_WEATHER, WEATHER_RECEIVER),
                fetchIntent(TIDE_URI, TIDE_RECEIVER),
                fetchIntent(WIND_WAVE_URI, WIND_WAVE_RECEIVER),
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
                .setOverrideDeadline(LAUNCH_INTERVAL)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
    }

}










