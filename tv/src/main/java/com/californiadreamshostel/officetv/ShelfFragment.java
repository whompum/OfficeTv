package com.californiadreamshostel.officetv;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.zetterstrom.com.forecast.Constants;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.ForecastConfiguration;
import android.zetterstrom.com.forecast.models.Forecast;

import com.californiadreamshostel.officetv.MODELS.ContactInformation.ContactInformation;
import com.californiadreamshostel.officetv.MODELS.RentalInformation.RentalInformation;
import com.californiadreamshostel.officetv.MODELS.Weather.CurrentWeatherModel;
import com.californiadreamshostel.officetv.MODELS.Weather.JsonToModelConvertor;
import com.californiadreamshostel.officetv.MODELS.Weather.SSSuperSecretAPIKey;
import com.californiadreamshostel.officetv.MODELS.Weather.WeatherSchema;
import com.californiadreamshostel.officetv.NETWORK.JsonDownloadPackage;
import com.californiadreamshostel.officetv.NETWORK.JsonResultListener;
import com.californiadreamshostel.officetv.NETWORK.JsonTimerTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Should also connect to some kind of back-end to allow dynamic changing of data values
 * */
public final class ShelfFragment extends Fragment {


    //CURRENT WEATHER API
    public static final String WEATHER_URL =
            WeatherSchema.BASE_CURRENT + "zip=92109,US&units=imperial&appid=" + SSSuperSecretAPIKey.API_KEY_WEATHER_OWM;

    //FORECAST WEATHER API
    public static final String FORECAST_WEATHER_URL =
            WeatherSchema.BASE_FORECAST + "zip=92109,US&units=imperial&appid=" + SSSuperSecretAPIKey.API_KEY_WEATHER_DARK_SKY;

    //SURF FORECAST API
    public static final String SURF_FORECAST_URL = "";

    public @BindInt(R.integer.currentWeather) int CURRENT_WEATHER;
    public @BindInt(R.integer.forecastedWeather) int FORECASTED_WEATHER;
    public @BindInt(R.integer.surfForecast) int SURF_REPORT;

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_shelf;

    protected @BindString(R.string.string_farenheit) String farenheight;
    protected @BindString(R.string.string_celsius) String celsius;


    public static Fragment newInstance(@Nullable final Bundle args){

        final Fragment frag = new ShelfFragment();

        if(args != null)
            frag.setArguments(args);

        return frag;
    }

    public ShelfFragment(){


        final JsonDownloadPackage weatherPackage = new JsonDownloadPackage(jsonDataListener, WEATHER_URL);
        //final JsonDownloadPackage forecastWeatherPackage = new JsonDownloadPackage(jsonDataListener, FORECAST_WEATHER_URL);
        //final JsonDownloadPackage surfForecastPackage = new JsonDownloadPackage(jsonDataListener, SURF_FORECAST_URL);

        this.weatherTask = JsonTimerTask.begin(weatherPackage, CURRENT_WEATHER);
       // this.forecastTask = JsonTimerTask.begin(forecastWeatherPackage, FORECASTED_WEATHER);
       // this.surfForecastTask = JsonTimerTask.begin(surfForecastPackage, SURF_REPORT);
    }


    //Contact Information TextViews
    protected @BindView(R.id.id_location_display) RTV locationDisplay;
    protected @BindView(R.id.id_phone_number_display) RTV phoneNumberDisplay;
    protected @BindView(R.id.id_email_display) RTV emailDisplay;
    protected @BindView(R.id.id_social_media_one_display) RTV socialMediaOneDisplay;
    protected @BindView(R.id.id_social_media_two_display) RTV socialMediaTwoDisplay;
    protected @BindView(R.id.id_social_media_three_display) RTV socialMediaThreeDisplay;


    //Weather Information
    protected @BindView(R.id.id_today_weather_value) RTV todayWeatherDisplay;
    protected @BindView(R.id.id_today_weather_image) ImageView todayWeatherImage;
    protected @BindView(R.id.id_tomorrow_weather_label) RTV t_WeatherLabel;
    protected @BindView(R.id.id_tomorrow_weather_value) RTV t_WeatherDisplay;
    protected @BindView(R.id.id_tomorrow_tomorrow_weather_label) RTV t_T_WeatherLabel;
    protected @BindView(R.id.id_tomorrow_tomorrow_weather_value) RTV t_T_WeatherDisplay;
    protected @BindView(R.id.id_tomorrow_tomorrow_tomorrow_weather_label) RTV t_T_TWeatherLabel;
    protected @BindView(R.id.id_tomorrow_tomorrow_tomorrow_weather_value) RTV t_T_TWeatherDisplay;


    //todo 5/10/18 @ 10:44 pm: INITIALIZE VIA BUTTERKNIFE VIEW INJECTION
    //Surf Information
    private RTV waveHeight, waterTemp, hightTide, windSpeed;


    private Unbinder unbinder;

    //Contact Information
    private ContactInformation contactInformation;

    //RentalInformation
    private RentalInformation rentalInformation;

    private JsonTimerTask weatherTask;
    private JsonTimerTask forecastTask;
    private JsonTimerTask surfForecastTask;

    private UpdateCache downloadedJsonCache = new UpdateCache();

    private JsonResultListener jsonDataListener = new JsonResultListener() {
        @Override
        public void onResult(String json, int id) {
            downloadedJsonCache.add(new Data(json, id));
        }
    };

    private CacheChangeListener cacheChangeListener = new CacheChangeListener() {
        @Override
        public void onDataChanged(int type) {
            if(downloadedJsonCache.hasData(type)){
                if(type == CURRENT_WEATHER)
                    updateCurrentWeather(JsonToModelConvertor.convertToCurrentWeather(downloadedJsonCache.fetchAndRemove(type)));
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final List<String> excludeList = new ArrayList<>();

        excludeList.add(Constants.OPTIONS_EXCLUDE_ALERTS);
        excludeList.add(Constants.OPTIONS_EXCLUDE_CURRENLY);
        excludeList.add(Constants.OPTIONS_EXCLUDE_FLAGS);
        excludeList.add(Constants.OPTIONS_EXCLUDE_HOURLY);
        excludeList.add(Constants.OPTIONS_EXCLUDE_MINUTELY);

        final ForecastConfiguration configuration = new ForecastConfiguration.Builder(FORECAST_WEATHER_URL)
                .setCacheDirectory(context.getCacheDir())
                .setDefaultExcludeList(excludeList)
                .build();

        ForecastClient.create(configuration);

        ForecastClient.getInstance().getForecast(0.0D, 0.0D, new Callback<Forecast>(){
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                downloadedJsonCache.add(new Data(response.body().getDaily().));
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {

            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(weatherTask != null)
            weatherTask.cancel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View content = inflater.inflate(LAYOUT, container, false);

        unbinder = ButterKnife.bind(this, content);

        downloadedJsonCache.bindListener(cacheChangeListener); //Bind CacheChangeListener now that we're ready to populate our UI

        if(downloadedJsonCache.hasData(CURRENT_WEATHER))
            updateCurrentWeather(JsonToModelConvertor.convertToCurrentWeather(downloadedJsonCache.fetchAndRemove(CURRENT_WEATHER)));


    return content;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void updateCurrentWeather(@NonNull final CurrentWeatherModel weatherModel){
        Log.i("CaliDreamsTest", "Updating Current  Weather. TEMP: " + String.valueOf(weatherModel.getTemp()));
        todayWeatherDisplay.setText(getFormattedTemp(String.valueOf(weatherModel.getTemp()), false));
    }

    private String getFormattedTemp(@NonNull String temp, final boolean useMetric){
        return (useMetric) ? temp + celsius : temp + farenheight;
    }



    private class UpdateCache{

        private Map<Integer, String> cache = new HashMap<>();

        private CacheChangeListener listener;

        public UpdateCache(){

        }


        public void bindListener(@NonNull CacheChangeListener listener){
            this.listener = listener;
        }

        public void add(@NonNull final Data data){
            Log.i("CaliDreamsTest", "ADDING NEW ENTRY TO UPDATE CACHE");
            if(!cache.containsKey(data.type)) {
                cache.put(data.type, data.data);
                if(listener != null)
                    listener.onDataChanged(data.type);
            }
        }

        public boolean hasData(final int type){
            return cache.containsKey(type);
        }

        @Nullable
        public String fetchAndRemove(final int type){
            if(hasData(type)) {
                final String data = cache.remove(type);

                if(listener != null)
                    listener.onDataChanged(type);

                return data;
            }

        return null;
        }


        @Nullable
        public String peekData(final int type){
            if(hasData(type))
                return cache.get(type);

         return null;
        }

    }

    private class Data{
        String data;
        int type;
        public Data(final String data, final int type){
            this.data = data;
            this.type = type;
        }
    }

    private interface CacheChangeListener{
        void onDataChanged(final int type);
    }

}











