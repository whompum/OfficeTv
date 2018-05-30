package com.californiadreamshostel.officetv;

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

import com.californiadreamshostel.officetv.NETWORKING.JSON.ResponseListener;
import com.californiadreamshostel.officetv.WEATHER.WEATHER_CONSTANTS;
import com.californiadreamshostel.officetv.WEATHER.WeatherUpdator;

import com.californiadreamshostel.officetv.SURF.MODELS.SurfData;
import com.californiadreamshostel.officetv.SURF.SURF_CONSTANTS;
import com.californiadreamshostel.officetv.SURF.SurfUpdator;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 * Should also connect to some kind of back-end to allow dynamic changing of data values
 * */
public final class ShelfFragment extends Fragment implements SurfUpdator.OnSurfData, ResponseListener {


    public static final String WEATHER_QUERY = WEATHER_CONSTANTS.getQuery(new WEATHER_CONSTANTS.Location(32.80D, -117.24D),
            WEATHER_CONSTANTS.DAILY_CURRENTLY_URL);

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


        final WeatherUpdator.TaskMetaInfo taskMetaInfo =
                new WeatherUpdator.TaskMetaInfo(this, WEATHER_QUERY);

        this.weatherUpdator = new WeatherUpdator(taskMetaInfo, false);

        //URLS FOR THE SURF DATA AGGREGATION
        final Map<String, String> urls = new HashMap<>(3);

        urls.put(SurfUpdator.HIGH_TIDE_KEY, SURF_CONSTANTS.SPITCAST.getQuery(SURF_CONSTANTS.SPITCAST.SPIT_CAST_TIDE));
        urls.put(SurfUpdator.WATER_INFO_KEY, SURF_CONSTANTS.SPITCAST.getQuery(SURF_CONSTANTS.SPITCAST.SPIT_CAST_WATER_TEMP));
        urls.put(SurfUpdator.WIND_SWELL_HEIGHT_KEY,
                SURF_CONSTANTS.MAGICSEAWEED.getQuery(SURF_CONSTANTS.MAGICSEAWEED.MSW_TIMESTAMP,
                        SURF_CONSTANTS.MAGICSEAWEED.MSW_SWELL_MIN,
                        SURF_CONSTANTS.MAGICSEAWEED.MSW_SWELL_MAX,
                        SURF_CONSTANTS.MAGICSEAWEED.MSW_WIND_SPEED));

        this.surfUpdator = new SurfUpdator(TimeUnit.HOURS.toMillis(1), urls, this);
    }

    //Contact Information TextViews
    protected @BindView(R.id.id_location_display) RTV locationDisplay;
    protected @BindView(R.id.id_phone_number_display) RTV phoneNumberDisplay;
    protected @BindView(R.id.id_email_display) RTV emailDisplay;
    protected @BindView(R.id.id_social_media_one_display) RTV socialMediaOneDisplay;
    protected @BindView(R.id.id_social_media_two_display) RTV socialMediaTwoDisplay;
    protected @BindView(R.id.id_social_media_three_display) RTV socialMediaThreeDisplay;

    //Weather Information Display
    protected @BindView(R.id.id_today_weather_value) RTV todayWeatherDisplay;
    protected @BindView(R.id.id_today_weather_image) ImageView todayWeatherImage;
    protected @BindView(R.id.id_tomorrow_weather_label) RTV t_WeatherLabel;
    protected @BindView(R.id.id_tomorrow_weather_value) RTV t_WeatherDisplay;
    protected @BindView(R.id.id_tomorrow_tomorrow_weather_label) RTV t_T_WeatherLabel;
    protected @BindView(R.id.id_tomorrow_tomorrow_weather_value) RTV t_T_WeatherDisplay;
    protected @BindView(R.id.id_tomorrow_tomorrow_tomorrow_weather_label) RTV t_T_TWeatherLabel;
    protected @BindView(R.id.id_tomorrow_tomorrow_tomorrow_weather_value) RTV t_T_TWeatherDisplay;

    //Surf Information Displays
    protected @BindView(R.id.id_surf_wave_height) RTV waveHeightDisplay;
    protected @BindView(R.id.id_surf_water_temp) RTV waterTemperatureDisplay;
    protected @BindView(R.id.id_surf_height_tide) RTV highTideDisplay;
    protected @BindView(R.id.id_surf_wind_speed) RTV windSpeedDisplay;

    private Unbinder unbinder;

    private WeatherUpdator weatherUpdator;
    private SurfUpdator surfUpdator;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View content = inflater.inflate(LAYOUT, container, false);

        //Bind Butterknife View Injection to our Heirarchy
        unbinder = ButterKnife.bind(this, content);

        /**
         * Views are ready; Launch the timed tasks to update
         * weather and the SURF
         */
        weatherUpdator.start();
        surfUpdator.start();

        return content;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    @Override
    public void onSurfDataFetched(@NonNull SurfData surfingData) {
        Log.i("BLAH", "SETTING WATER TEMP: " + getFormattedTemp(String.valueOf(surfingData.getWaterTemperature()), false));

        final int waterTemp = surfingData.getWaterTemperature();
        waterTemperatureDisplay.setText(getFormattedTemp(String.valueOf(waterTemp), false));


        final String wind = String.valueOf(surfingData.getWindSpeed()) +
                " " + getString(R.string.string_knots);

        windSpeedDisplay.setText(wind);

        String swellHeight = String.valueOf(surfingData.getWindSwellHeightMax()) + " " + getString(R.string.string_ft);


        waveHeightDisplay.setText(swellHeight);
        highTideDisplay.setText(surfingData.getHighTideTime());
    }


    @Override
    public void onResponse(@NonNull String data) {

        Log.i("FUCK_OFF", data);

        try {

            final JSONArray dataPoints =
                    new JSONObject(data).getJSONObject("daily").getJSONArray("data");

            final JSONObject currentDataPoint = new JSONObject(data).getJSONObject("currently");


            final double currentTemperature = currentDataPoint.getDouble("temperature");

            todayWeatherDisplay.setText(getFormattedTemp(String.valueOf((int)currentTemperature), false));

            Log.i("FUCK_OFF", "FETCHED DATA");


            final int dayCnt = 4; //Number days we want data for

            for(int i = 1; i < dayCnt; i++){

                if(dataPoints.length() < i )
                    break;

                final JSONObject forecastedDataPoint = dataPoints.getJSONObject(i);

                final double forecastedTemperature = forecastedDataPoint.getDouble("apparentTemperatureHigh");

                RTV valueDisplay = null;
                RTV valueLabel = null;

                if(i == 1){
                    valueLabel = t_WeatherLabel;
                    valueDisplay = t_WeatherDisplay;
                }
                else if(i == 2){
                    valueLabel = t_T_WeatherLabel;
                    valueDisplay = t_T_WeatherDisplay;
                }
                else if(i == 3 ){
                    valueLabel = t_T_TWeatherLabel;
                    valueDisplay = t_T_TWeatherDisplay;
                }

                if(valueDisplay != null)
                    valueDisplay.setText(getFormattedTemp( String.valueOf((int)forecastedTemperature), false));

                if(valueLabel != null){

                    final java.util.Calendar now = java.util.Calendar.getInstance();

                    now.setTimeInMillis(
                        TimeUnit.SECONDS.toMillis(forecastedDataPoint.getLong("time"))
                    );

                    final int dayOfWeek = now.get(java.util.Calendar.DAY_OF_WEEK);

                    if(dayOfWeek == java.util.Calendar.MONDAY)
                        valueLabel.setText("Monday");
                    if(dayOfWeek == java.util.Calendar.TUESDAY)
                        valueLabel.setText("Tuesday");
                    if(dayOfWeek == java.util.Calendar.WEDNESDAY)
                        valueLabel.setText("Wednesday");
                    if(dayOfWeek == java.util.Calendar.THURSDAY)
                        valueLabel.setText("Thursday");
                    if(dayOfWeek == java.util.Calendar.FRIDAY)
                        valueLabel.setText("Friday");
                    if(dayOfWeek == java.util.Calendar.SATURDAY)
                        valueLabel.setText("Saturday");
                    if(dayOfWeek == java.util.Calendar.SUNDAY)
                        valueLabel.setText("Sunday");
                }


            }


        }catch (JSONException e){
            e.printStackTrace();
            Log.i("FUCK_OFF", e.getLocalizedMessage());
        }


    }

    private String getFormattedTemp(@NonNull String temp, final boolean useMetric){
        return (useMetric) ? temp + celsius : temp + farenheight;
    }


}











