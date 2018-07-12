package com.californiadreamshostel.officetv.CONTROLLERS.weather$surf;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.californiadreamshostel.officetv.NETWORKING.DOWNLOADING.DownloadIntent;
import com.californiadreamshostel.officetv.PARSING.IParsingFinishedObserver;
import com.californiadreamshostel.officetv.SURF.ENDPOINTS.MagicSeaWeed;
import com.californiadreamshostel.officetv.SURF.ENDPOINTS.Spitcast;
import com.californiadreamshostel.officetv.SURF.MODELS.Tide;
import com.californiadreamshostel.officetv.SURF.MODELS.WaterTemperature;
import com.californiadreamshostel.officetv.SURF.MODELS.WindAndSwell;
import com.californiadreamshostel.officetv.SURF.TideParsingStrategy;
import com.californiadreamshostel.officetv.SURF.persistence.TideRoomDatabase;
import com.californiadreamshostel.officetv.SURF.persistence.WindAndSwellRoomDatabase;
import com.californiadreamshostel.officetv.SURF.WindSwellParsingStrategy;
import com.californiadreamshostel.officetv.WEATHER.model.DarkSKyDataPointAdapter;
import com.californiadreamshostel.officetv.WEATHER.DarkSkyParsingStrategy;
import com.californiadreamshostel.officetv.WEATHER.model.DataPointAdapter;
import com.californiadreamshostel.officetv.WEATHER.ENDPOINTS.DarkSky;
import com.californiadreamshostel.officetv.WEATHER.model.DarkSkyWeather;
import com.californiadreamshostel.officetv.WEATHER.Persistence.WeatherRoom;
import com.californiadreamshostel.officetv.WEATHER.model.WeatherData;
import com.californiadreamshostel.officetv.WEATHER.model.WeatherDataProxy;
import com.californiadreamshostel.officetv.WEATHER.model.WeatherProvider;
import com.californiadreamshostel.officetv.persistence.DaoCacheContract;

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
public class WeatherSurfController extends JobService implements IParsingFinishedObserver{

    private enum SOURCE_TYPE{ WEATHER, TIDE, WAVE_WIND, WATER_TEMPERATURE, NO_SABES_NADA }

    public static final long INTERVAL = TimeUnit.HOURS.toMillis(1);

    public static final long MAXIMUM_XECUTE_DELAY = TimeUnit.SECONDS.toMillis(5);

    public static final int ID = 0;

    private final Uri WEATHER_URI = new DarkSky.UriBuilder().getQueryUri("32.80, -117.24", FLAGS_EXCLUDE, ALERTS_EXCLUDE, MINUTELY_EXCLUDE);
    private final Uri TIDE_URI = new Spitcast.UriBuilder().getUri(PATH_TIDE);
    private final Uri WIND_WAVE_URI = new MagicSeaWeed.UriBuilder().getUri(TIMESTAMP, SWELL_MAXIMUM, SWELL_MINIMUM, WINDSPEED);

    private final ResultReceiver WEATHER_RECEIVER = new DarkSkyParsingStrategy(this);
    private final ResultReceiver TIDE_RECEIVER = new TideParsingStrategy(this);
    private final ResultReceiver WIND_WAVE_RECEIVER = new WindSwellParsingStrategy(this);

    private DownloadIntent[] downloads;

    @Override
    public boolean onStartJob(JobParameters params) {

        if(downloads == null)
            prepareDownloadIntents();

        for(int a = 0; a  < downloads.length; a++)
            startService(downloads[a]);


        //start(this);  //Reschedules the Job.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("WEATHER_SURF_JOB", "JOB STOPPED");
        return false;
    }

    /**
     * Resolve the @Dao sqlite abstraction by resolving the type of data we're using -> SOURCE_TYPE
     * Then pass the @Dao (contract), as well as the data to the saveData method
     * @param data The data i want to cache
     */
    @Override
    public void onDataParsed(Object data) {

        DaoCacheContract contract;

        if( (contract = resolveContract(resolveType(data))) != null)
            saveData(contract, data);
    }

    private void saveData(@NonNull final DaoCacheContract c, @NonNull final Object o){

        if(resolveType(o).equals(SOURCE_TYPE.WAVE_WIND))
            saveWindAndSwell(c, o);

        else if(resolveType(o).equals(SOURCE_TYPE.TIDE))
            saveTide(c, o);

        else if(resolveType(o).equals(SOURCE_TYPE.WEATHER))
            saveWeatherData(c, o);

    }

    /**
     * Resolves the specific @Dao interface we're using
     * @param type
     * @return
     */
    private DaoCacheContract resolveContract(@NonNull final SOURCE_TYPE type){

        DaoCacheContract contract = null;

        if(type.equals(SOURCE_TYPE.TIDE))
            contract = Room.databaseBuilder(this, TideRoomDatabase.class, "tide.db").build().getDao();

        if(type.equals(SOURCE_TYPE.WAVE_WIND))
            contract = Room.databaseBuilder(this, WindAndSwellRoomDatabase.class, "wind_swell.db").build().getDao();

        if(type.equals(SOURCE_TYPE.WEATHER))
            contract = Room.databaseBuilder(this, WeatherRoom.class, "weather.db").build().getDao();

        return contract;
    }

    /**
     * Resolves the Endpoint type of data we're working with.
     * @param o
     * @return
     */
    @NonNull
    private SOURCE_TYPE resolveType(@NonNull final Object o){
        if(o instanceof DarkSkyWeather)
            return SOURCE_TYPE.WEATHER;

        else if(o instanceof Tide[])
             return SOURCE_TYPE.TIDE;

        else if(o instanceof WaterTemperature)
            return SOURCE_TYPE.WATER_TEMPERATURE;

        else if(o instanceof WindAndSwell[])
            return SOURCE_TYPE.WAVE_WIND;

    return SOURCE_TYPE.NO_SABES_NADA;
    }

    private void saveTide(@NonNull final DaoCacheContract c, @NonNull final Object o){
        new Thread() {
            @Override
            public void run() {
                final Tide[] data = (Tide[]) o;

                c.deleteAll();

                for(Tide t: data)
                    c.insert(t);
            }
        }.start();
    }

    private void saveWindAndSwell(@NonNull final DaoCacheContract c, @NonNull final Object o){
        new Thread() {
            @Override
            public void run() {

                final WindAndSwell[] data = (WindAndSwell[]) o;

                c.deleteAll();

                for(WindAndSwell a : data)
                    c.insert(a);
            }
        }.start();
    }

    private void saveWeatherData(@NonNull final DaoCacheContract c, @NonNull final Object o){
        new Thread(){
            @Override
            public void run(){

                WeatherData data;

                DataPointAdapter adapter = null;

                if(o instanceof DarkSkyWeather)
                    adapter = new DarkSKyDataPointAdapter((DarkSkyWeather) o);


                if(adapter != null) {
                    data = new WeatherDataProxy()
                            .addProvider(new WeatherProvider().setAdapter(adapter))
                            .getWeatherData();

                    c.deleteAll();
                    c.insert(data);
                }
            }
        }.start();
    }

    /**
     * Prepares the DownloadIntent array object
     * with the various Endpoints to download.
     */
    private void prepareDownloadIntents(){
        downloads = new DownloadIntent[]{
                fetchIntent(WEATHER_URI, WEATHER_RECEIVER),
                fetchIntent(TIDE_URI, TIDE_RECEIVER),
                fetchIntent(WIND_WAVE_URI, WIND_WAVE_RECEIVER)};
    }

    private DownloadIntent fetchIntent(@NonNull final Uri uri, @NonNull final ResultReceiver receiver){
        return new DownloadIntent(this, uri, receiver);
    }

    public static void start(@NonNull final Context context){
        ((JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE))
                .schedule(makeJobInfo(context.getApplicationContext()));
    }

    private static JobInfo makeJobInfo(@NonNull final Context context){
        return new JobInfo.Builder(ID, new ComponentName(context, WeatherSurfController.class))
                .setMinimumLatency(TimeUnit.SECONDS.toMillis(90))
                .setOverrideDeadline(15000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
    }

}










