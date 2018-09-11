package com.californiadreamshostel.officetv.Controllers;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.californiadreamshostel.firebaseclient.DataSchema;
import com.californiadreamshostel.firebaseclient.ReferenceItem;
import com.californiadreamshostel.officetv.Controllers.weather$surf.TideRepo;
import com.californiadreamshostel.officetv.Controllers.weather$surf.WindSwellRepo;
import com.californiadreamshostel.officetv.R;
import com.californiadreamshostel.officetv.Surf.MODELS.Tide;
import com.californiadreamshostel.officetv.Surf.MODELS.WindAndSwell;
import com.californiadreamshostel.officetv.Surf.TideUtils;
import com.californiadreamshostel.officetv.Surf.WindSwellUtils;
import com.californiadreamshostel.officetv.Unit.CONVERTERTTASK.UnitUpdator;
import com.californiadreamshostel.officetv.Unit.CONVERTER.ConversionType;
import com.californiadreamshostel.officetv.Unit.UNITS.SIZE.Ft;
import com.californiadreamshostel.officetv.Unit.UNITS.TEMPERATURE.F;
import com.californiadreamshostel.officetv.Unit.UNITS.VELOCITY.Kts;
import com.californiadreamshostel.officetv.Unit.UnitChoreographer;
import com.californiadreamshostel.officetv.Views.RTV;
import com.californiadreamshostel.officetv.Views.RentalView;
import com.californiadreamshostel.officetv.Weather.ConditionUiUtility;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Desc: This class filters the data received for Weather$Surf when it shouldn't even have that responsibility.
 */
public final class ShelfFragment extends Fragment implements LifecycleOwner,
        WeatherDataReceiver, RemoteDataReceiver{

    //Default Interval for Unit conversions para weather, and such
    public static final long DEFAULT_UNIT_CONVERT_INTERVAL = TimeUnit. SECONDS.toMillis(10);

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

    protected @BindView(R.id.local_rentals_one) RentalView rentalsOne;
    protected @BindView(R.id.local_rentals_two) RentalView rentalsTwo;
    protected @BindView(R.id.local_rentals_three) RentalView rentalsThree;

    //Contact Information Displays
    protected @BindView(R.id.id_location_display) RTV locationDisplay;
    protected @BindView(R.id.id_phone_number_display) RTV phoneNumberDisplay;
    protected @BindView(R.id.id_email_display) RTV emailDisplay;
    protected @BindView(R.id.id_social_media_one_display) RTV socialMediaOneDisplay;
    protected @BindView(R.id.id_social_media_two_display) RTV socialMediaTwoDisplay;
    protected @BindView(R.id.id_social_media_three_display) RTV socialMediaThreeDisplay;

    //Extras Information Displays
    protected @BindView(R.id.id_extras_breakfast) RTV breakfastDisplay;
    protected @BindView(R.id.id_extras_checkin) RTV checkinDisplay;
    protected @BindView(R.id.id_extras_checkout) RTV checkoutDisplay;

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
    //protected @BindView(R.id.id_surf_water_temp) RTV waterTemperatureDisplay;
    protected @BindView(R.id.id_surf_height_tide) RTV highTideDisplay;
    protected @BindView(R.id.id_surf_wind_speed) RTV windSpeedDisplay;

    private ShelfDataController controller;

    private Unbinder unbinder;

    private UnitUpdator unitUpdator;

    //Store a reference to all UnitChoreographer objects. Array-Key is the view ID
    private Map<Integer, UnitChoreographer> unitChoreographers = new HashMap<>();

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registry.markState(Lifecycle.State.CREATED);
        controller = new ShelfDataController(getActivity(), this, this);
        controller.register();
        controller.bindWeatherClient(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unitUpdator.cancel();
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
        controller.register();
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

        registerSurfListeners();
        return content;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCurrentWeatherData(double currentValue, String condition) {
        fetchChoreographer(todayWeatherDisplay.getId())
                .bind(new F(currentValue));

        todayWeatherImage.setImageResource(ConditionUiUtility.getRepresentation(condition));
    }

    @Override
    public void onForecastWeatherData(int projection, double value, String day) {

        UnitChoreographer c = null;
        RTV dayDisplay = null;

        if(projection == 1){
            c = fetchChoreographer(t_WeatherDisplay.getId());  //Tomorrow
            dayDisplay = t_WeatherLabel;
        }
        if(projection == 2){
            c = fetchChoreographer(t_T_WeatherDisplay.getId()); //Tomorrow Tomorrow
            dayDisplay = t_T_WeatherLabel;
        }
        if(projection == 3){
            c = fetchChoreographer(t_T_TWeatherDisplay.getId()); //Tomorrow Tomorrow Tomorrow
            dayDisplay = t_T_TWeatherLabel;
        }
        if(c != null)
            c.bind(new F(value));

        if(dayDisplay != null && day != null)
            dayDisplay.setText(day);

    }

    @Override
    public void onDataReceived(ReferenceItem item, @NonNull int changeType) {
        final String k = item.getReference();
        final String v = item.getValue();

        if(k.equals(DataSchema.ANCILLIARY_ONE))
            rentalsOne.setPrice(v);

        if(k.equals(DataSchema.ANCILLIARY_TWO))
            rentalsTwo.setPrice(v);

        if(k.equals(DataSchema.ANCILLIARY_THREE))
            rentalsThree.setPrice(v);

        if(k.equals(DataSchema.CONTACT_ONE))
            locationDisplay.setText(v);

        if(k.equals(DataSchema.CONTACT_TWO))
            phoneNumberDisplay.setText(v);

        if(k.equals(DataSchema.CONTACT_THREE))
            emailDisplay.setText(v);

        if(k.equals(DataSchema.EXTRAS_BREAKFAST))
            breakfastDisplay.setText(v);

        if(k.equals(DataSchema.EXTRAS_CHECKIN))
            checkinDisplay.setText(v);

        if(k.equals(DataSchema.EXTRAS_CHECKOUT))
            checkoutDisplay.setText(v);

        if(k.equals(DataSchema.SOCIAL_ONE))
            socialMediaOneDisplay.setText(v);

        if(k.equals(DataSchema.SOCIAL_TWO))
            socialMediaTwoDisplay.setText(v);

        if(k.equals(DataSchema.SOCIAL_THREE))
            socialMediaThreeDisplay.setText(v);
    }

    private void registerSurfListeners(){
        new CountDownTimer(Long.MAX_VALUE, TimeUnit.MINUTES.toMillis(10)){
            @Override
            public void onTick(long millisUntilFinished) {
                registerTideDataListener();
                registerWindSwellListener();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void registerTideDataListener(){
        TideRepo.obtain(getActivity())
                .getData().observe(this, new Observer<List<Tide>>() {
            @Override
            public void onChanged(@Nullable List<Tide> tides) {

                Tide highTide = null;

                if(tides != null)
                    if( (highTide = TideUtils.highestTideFrom(tides)) != null)
                        highTideDisplay.setText(highTide.getHour());

            }
        });
    }
    private void registerWindSwellListener(){

        WindSwellRepo.obtain(getActivity())
                .getData().observe(this, new Observer<List<WindAndSwell>>() {
            @Override
            public void onChanged(@Nullable List<WindAndSwell> windAndSwells) {
                //Bind to views.

                if(windAndSwells != null && windAndSwells.size() > 0){

                    WindAndSwell was;

                    if( ( was = WindSwellUtils.findClosest(windAndSwells)) != null){
                        fetchChoreographer(waveHeightDisplay.getId())
                                .bind(new Ft(was.getSwell().getMaxBreakingHeight()));

                        fetchChoreographer(windSpeedDisplay.getId())
                                .bind(new Kts(was.getWind().getSpeed()));

                        Log.i("API_TIMING_FIX", "WIND SWELL TS: " + was.getLocalTimestamp());

                    }

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

        //unitChoreographers.put(waterTemperatureDisplay.getId(),
        //      newChoreographer(waterTemperatureDisplay, ConversionType.TEMPERATURE));


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












