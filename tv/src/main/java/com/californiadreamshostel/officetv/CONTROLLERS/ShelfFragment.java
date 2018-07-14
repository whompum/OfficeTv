package com.californiadreamshostel.officetv.CONTROLLERS;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.californiadreamshostel.officetv.CONTROLLERS.weather$surf.WeatherRepo;
import com.californiadreamshostel.officetv.R;
import com.californiadreamshostel.officetv.UNIT.CONVERTERTTASK.UnitUpdator;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.ConversionType;
import com.californiadreamshostel.officetv.UNIT.UNITS.TEMPERATURE.F;
import com.californiadreamshostel.officetv.UNIT.UnitChoreographer;
import com.californiadreamshostel.officetv.VIEWS.RTV;
import com.californiadreamshostel.officetv.WEATHER.ConditionUiUtility;
import com.californiadreamshostel.officetv.WEATHER.DateUtils;
import com.californiadreamshostel.officetv.WEATHER.model.WeatherData;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 * Should also connect to some kind of back-end to allow dynamic changing of data values
 *
 * TODO Move Api fetching data into a service, save data to DB, and populate views with LiveData
 *
 * */
public final class ShelfFragment extends Fragment implements LifecycleOwner{

    //Default Interval for Unit conversions para weather, and such
    public static final long DEFAULT_UNIT_CONVERT_INTERVAL = TimeUnit.SECONDS.toMillis(10);

    @LayoutRes
    public static final int LAYOUT = R.layout.shelf_base;

    public static Fragment newInstance(@Nullable final Bundle args){

        final Fragment frag = new ShelfFragment();

        if(args != null)
            frag.setArguments(args);

        return frag;
    }

    public ShelfFragment(){
        this.unitUpdator = new UnitUpdator(DEFAULT_UNIT_CONVERT_INTERVAL);
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
    //protected @BindView(R.id.id_surf_height_tide) RTV highTideDisplay;
    protected @BindView(R.id.id_surf_wind_speed) RTV windSpeedDisplay;

    private Unbinder unbinder;

    private UnitUpdator unitUpdator;

    //Store a reference to all UnitChoreographer objects. Array-Key is the view ID
    private Map<Integer, UnitChoreographer> unitChoreographers = new HashMap<>();

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registry.markState(Lifecycle.State.CREATED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registry.markState(Lifecycle.State.DESTROYED);
    }

    @Override
    public void onResume() {
        super.onResume();
        registry.markState(Lifecycle.State.RESUMED);
    }

    @Override
    public void onStart() {
        super.onStart();
        registry.markState(Lifecycle.State.STARTED);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View content = inflater.inflate(LAYOUT, container, false);

        //Bind Butterknife View Injection to our Heirarchy
        unbinder = ButterKnife.bind(this, content);

        populateChoreographers();
        bindUnitChoreographers();

        unitUpdator.start();
        /**
         * Views are ready; Launch the timed tasks to update
         * weather and the SURF
         */

        registerWeatherListener();

        return content;
    }

    private void registerWeatherListener(){
        WeatherRepo.obtain(this.getActivity())
                .getWeatherData().observe(this, new Observer<List<WeatherData>>() {
            @Override
            public void onChanged(@Nullable List<WeatherData> weatherData) {

                if(weatherData == null || weatherData.size() == 0)
                    return;

                final WeatherData data = weatherData.get(0);

                final WeatherData.Currently currently;
                final WeatherData.Daily daily;
                final WeatherData.Hourly hourly;

                boolean hasCurrent = (currently = data.getCurrently()) != null;
                boolean hasDaily = (daily = data.getDaily()) !=null;
                boolean hasHourly = (hourly = data.getHourly()) != null;

                if(hasCurrent) {
                    fetchChoreographer(todayWeatherDisplay.getId())
                            .bind(new F(currently.getTemperature()));

                    bindConditionDisplay(currently.getCondition());
                }

                //If currently is null, try to pluck current data from hourly. Else do so from Daily.

                if(hasDaily)
                    if(daily.getData() != null)
                        for(int projection = 1; projection <= 3; projection++){
                            WeatherData.Daily.DataPoints dP;
                            if((dP = DateUtils.dailyFromProjection(daily.getData(), projection)) == null) continue;
                            UnitChoreographer c = null;
                            if(projection == 1) c = fetchChoreographer(t_WeatherDisplay.getId()); //Tomorrow
                            if(projection == 2) c = fetchChoreographer(t_T_WeatherDisplay.getId()); //Tomorrow Tomorrow
                            if(projection == 3) c = fetchChoreographer(t_T_TWeatherDisplay.getId()); //Tomorrow Tomorrow Tomorrow
                            if(c != null)
                                c.bind(new F(dP.getTemperature()));
                        }

            }
        });
    }

    @Override //Unbinding Butterknife BTDUBS
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return registry;
    }

    //Sorry future reader. This method is ugly but just populates all the choreographer objects
    private void populateChoreographers(){

        unitChoreographers.put(todayWeatherDisplay.getId(),
                newChoreographer(todayWeatherDisplay, ConversionType.TEMPERATURE));

        unitChoreographers.put(t_WeatherDisplay.getId(),
                newChoreographer(t_WeatherDisplay, ConversionType.TEMPERATURE));

        unitChoreographers.put(t_T_WeatherDisplay.getId(),
                newChoreographer(t_T_WeatherDisplay, ConversionType.TEMPERATURE));

        unitChoreographers.put(t_T_TWeatherDisplay.getId(),
                newChoreographer(t_T_TWeatherDisplay, ConversionType.TEMPERATURE));

        unitChoreographers.put(waveHeightDisplay.getId(),
                newChoreographer(waveHeightDisplay, ConversionType.SIZE, true));

        unitChoreographers.put(waterTemperatureDisplay.getId(),
                newChoreographer(waterTemperatureDisplay, ConversionType.TEMPERATURE));

        unitChoreographers.put(windSpeedDisplay.getId(),
                newChoreographer(windSpeedDisplay, ConversionType.VELOCITY, true));

    }

    private void bindUnitChoreographers(){

        final Iterator<Integer> unitChoreographerIterator = unitChoreographers.keySet().iterator();

        while(unitChoreographerIterator.hasNext()){
            final int id = unitChoreographerIterator.next();

            final UnitChoreographer subject = unitChoreographers.get(id);

            final ConversionType conversion_type = subject.getConversionType();

            //Conversion Selector shouldn't ever be null
            unitUpdator.bind(subject, conversion_type);
        }


    }

    private void bindConditionDisplay(@NonNull final String condition){
        //Condition is coming from darkSky

        final int conditionDrawable = ConditionUiUtility.getRepresentation(condition);

        if(conditionDrawable != ConditionUiUtility.DERP && conditionDrawable  != ConditionUiUtility.NOT_SUPPORTED)
                todayWeatherImage.setImageResource(conditionDrawable);
    }


    private UnitChoreographer newChoreographer(@NonNull TextView subject, ConversionType cT){
        return new UnitChoreographer(subject, cT);
    }
    private UnitChoreographer newChoreographer(@NonNull TextView subject, ConversionType cT,
                                               final boolean usePrettyPrinting){
        return new UnitChoreographer(subject, cT, usePrettyPrinting);
    }

    @Nullable
    private UnitChoreographer fetchChoreographer(final int id){
        if(unitChoreographers.containsKey(id))
            return unitChoreographers.get(id);

    return null;
    }

}











