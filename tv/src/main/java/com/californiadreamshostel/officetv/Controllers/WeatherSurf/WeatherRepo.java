package com.californiadreamshostel.officetv.Controllers.WeatherSurf;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.Weather.Persistence.WeatherDaoInterface;
import com.californiadreamshostel.officetv.Weather.Persistence.WeatherRoom;
import com.californiadreamshostel.officetv.Weather.model.WeatherData;

import java.util.List;

public class WeatherRepo {

    private static WeatherRepo instance;

    private WeatherDaoInterface weatherAccessor;

    private LiveData<List<WeatherData>> weatherData;

    private WeatherRepo(@NonNull final Context c){

        this.weatherAccessor = WeatherRoom.obtain(c).getDao();

        this.weatherData = weatherAccessor.fetchAll();
    }

    public static synchronized WeatherRepo obtain(@NonNull final Context c){

        if(instance == null)
            instance = new WeatherRepo(c);

        return instance;
    }

    public LiveData<List<WeatherData>> getWeatherData(){
        return weatherData;
    }

    public synchronized void renewCache(@NonNull final WeatherData... data){

        final boolean singleInsert = data.length == 1;

        new Thread(){
            @Override
            public void run() {

                weatherAccessor.deleteAll();

                if(singleInsert)
                    weatherAccessor.insert(data[0]);

                else
                    weatherAccessor.insert(data);

            }
        }.start();

    }

}
