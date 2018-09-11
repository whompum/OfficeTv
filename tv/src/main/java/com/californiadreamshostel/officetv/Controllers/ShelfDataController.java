package com.californiadreamshostel.officetv.Controllers;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.californiadreamshostel.firebaseclient.DataSchema;
import com.californiadreamshostel.firebaseclient.FirebaseController;
import com.californiadreamshostel.officetv.Controllers.weather$surf.WeatherRepo;
import com.californiadreamshostel.officetv.WEATHER.DateUtils;
import com.californiadreamshostel.officetv.WEATHER.model.WeatherData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShelfDataController {

    private WeatherDataReceiver weatherReceiver;

    private WeatherData data;

    private String[] references = new String[]{DataSchema.ANCILLIARY_GROUP,
                                               DataSchema.CONTACT_GROUP,
                                               DataSchema.SOCIAL_GROUP,
                                               DataSchema.EXTRAS_GROUP};

    private Set<FirebaseController> controllers = new HashSet<>();

    public ShelfDataController(@NonNull final Context context,
                               @NonNull final LifecycleOwner owner,
                               @NonNull final RemoteDataReceiver remoteDataReceiver){

        WeatherRepo.obtain(context).getWeatherData().observe(owner, weatherObserver);

        for(String ref: references)
            controllers.add(new FirebaseController(ref, (a, b) -> {
                remoteDataReceiver.onDataReceived(a, b);
                return null;
            }));

    }

    public void register(){
        for(FirebaseController c: controllers)
            c.register();
    }

    public void bindWeatherClient(@NonNull final WeatherDataReceiver receiver){
        this.weatherReceiver = receiver;
    }

    private void notifyWeatherChange(){
        if(data == null || weatherReceiver == null) return;

        final WeatherData.Currently currently;
        final WeatherData.Daily daily;

        boolean hasCurrent = (currently = data.getCurrently()) != null;
        boolean hasDaily = (daily = data.getDaily()) !=null;

        double current = 0.0D;
        String currentCondition = "";


        if(hasCurrent) { //En futuro, if currently is null, try to pluck value from hourly
            current = currently.getTemperature();
            currentCondition = currently.getCondition();
        }

        weatherReceiver.onCurrentWeatherData(current, currentCondition);

        if(hasDaily && daily.getData() != null)
            for(int projection = 1; projection <= daily.getData().size(); projection++){
                WeatherData.Daily.DataPoints dP;
                if((dP = DateUtils.dailyFromProjection(daily.getData(), projection)) == null) continue;

                final String day = DateUtils.getDayRep(dP.getTimestamp());
                final double temperature = dP.getTemperature();

                weatherReceiver.onForecastWeatherData(projection, temperature, day);
            }

    }


    private Observer<List<WeatherData>> weatherObserver = new Observer<List<WeatherData>>() {
        @Override
        public void onChanged(@Nullable List<WeatherData> weatherData) {
            if(weatherData == null || weatherData.size() == 0) return;
            data = weatherData.get(0);
            notifyWeatherChange();
        }
    };


}
