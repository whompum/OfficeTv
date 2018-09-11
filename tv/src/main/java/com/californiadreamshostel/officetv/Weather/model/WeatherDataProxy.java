package com.californiadreamshostel.officetv.Weather.model;

import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.List;
import java.util.Set;

import static com.californiadreamshostel.officetv.Weather.model.WeatherData.Currently;
import static com.californiadreamshostel.officetv.Weather.model.WeatherData.Hourly;
import static com.californiadreamshostel.officetv.Weather.model.WeatherData.Daily;

public class WeatherDataProxy {

    public enum DATA_BLOCK {CURRENT, HOUR, DAY}

    public static final String FIELD_TEMPERATURE = "temperature";
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String FIELD_CONDITION = "condition";
    public static final String FIELD_WINDSPEED = "windSpeed";

    //Weather Data Objects
    private WeatherData weatherData = new WeatherData();
    private Currently current;
    private Daily daily;
    private Hourly hourly;

    /**
     *
     * Initializes the WeatherData using lazy instantiation
     * creating the data objects on demand. All communication
     * between the Provider and the Proxy object is done
     * via Server-Client only, using Initializer objects.
     * @param provider The provider object that provides us the data we want
     */
    public WeatherDataProxy addProvider(@NonNull final Provider provider){ //Synchronize the Method

        /*
         * Fetches the initializer object the provider wants to set, then initializes itself
         *
         * NOTE: The reason the Provider object is only allowed to set fields,
         *       and not objects is because a provider may only want to set some of the fields
         *       of an object, while another Provider wants to set other fields of the same object.
         *       If each provider did a sweeping generalized initialization then 2 providers could
         *       overwrite each other when they weren't meant to.
         *
         */


            final Set<Initializer> initializers = provider.fetchInitializers();

            if(initializers != null && initializers.size() > 0)
                for(Initializer i: initializers)
                    populate(i);

            if(hourly != null & hourly.getData() != null && hourly.getData().size() > 0)
                orderHourly(hourly.getData());


            update();

        return this;
    }

    private void populate(@NonNull final Initializer i){

        final DATA_BLOCK type = i.getType();

        if(type.equals(DATA_BLOCK.CURRENT)){

            if(current == null)
                current = new Currently();

            populate(Currently.class, current, i.getValue(), i.getFieldName());
        }

        if(type.equals(DATA_BLOCK.HOUR)){
            if(hourly == null)
                hourly = new Hourly();

            if(hourly.getData() == null)
                hourly.setData(new ArrayList<Hourly.DataPoints>());


            List<Hourly.DataPoints> dataPoints = hourly.getData();

            Hourly.DataPoints dataPoint = null;

            for(Hourly.DataPoints d: dataPoints){ //Find the DataPoint object we want to set states on
                if(d.getTimestamp() == i.timestamp){
                    dataPoint = d;
                    break;
                }
            }

            if(dataPoint == null) {
                dataPoint = new Hourly.DataPoints();
                dataPoint.setTimestamp(i.timestamp);
                dataPoints.add(dataPoint);
            }

            populate(Hourly.DataPoints.class, dataPoint, i.getValue(), i.getFieldName());
        }

        if(type.equals(DATA_BLOCK.DAY)){
            if(daily == null)
                daily = new Daily();

            if(daily.getData() == null)
                daily.setData(new ArrayList<Daily.DataPoints>());


            List<Daily.DataPoints> dataPoints = daily.getData();

            Daily.DataPoints dataPoint = null;

            for(Daily.DataPoints d: dataPoints){ //Find the DataPoint object we want to set states on
                if(d.getTimestamp() == i.timestamp){
                    dataPoint = d;
                    break;
                }
            }

            if(dataPoint == null) {
                dataPoint = new Daily.DataPoints();
                dataPoint.setTimestamp(i.timestamp);
                dataPoints.add(dataPoint);
            }

            populate(Daily.DataPoints.class, dataPoint, i.getValue(), i.getFieldName());
        }


    }


    private void populate(@NonNull Class sbj, @NonNull final Object subject, @NonNull final Object value, @NonNull final String fieldName){
        try{

            final Field field = resolveField(sbj, fieldName);

            if(field != null)
                field.set(subject, value);

            Log.i("WEATHER_DATA_PROXY", "Setting field of class: " + sbj.getSimpleName() +
            " For the Field: " + fieldName);

        }catch (IllegalAccessException a){
            Log.i("WEATHER_DATA_PROXY", "CAN'T ACCESS FIELD: " + fieldName);
        }
    }


    private Field resolveField(Class c, String n){

        Field field = null;

        try{
            field = c.getDeclaredField(n);
            field.setAccessible(true);
        }catch (NoSuchFieldException e){
            Log.i("WEATHER_DATA_PROXY", "No Such Field: " + n);
        }
        return field;
    }

    private void orderHourly(List<Hourly.DataPoints> data){

        final List<Hourly.DataPoints> orderedList = new ArrayList<>();

        long orderValue = Long.MAX_VALUE; //current smallest
        int smallestIndex = -1;
        Hourly.DataPoints orderHolder = null;

        for(int i = 0; i < data.size(); i++) {
            for (int a = 0; a < data.size(); a++) {
                if (data.get(a).getTimestamp() < orderValue)
                    orderHolder = data.get(a);
                    smallestIndex = a;
            }
            orderedList.add(orderHolder);
            data.remove(smallestIndex);
        }

        if(orderedList.size() == data.size())
            if(hourly!=null) hourly.setData(orderedList);

    }


    private void update(){

        weatherData.setCurrently(current);

        weatherData.setDaily(daily);
        weatherData.setHourly(hourly);

    }

    public WeatherData getWeatherData() {
        return weatherData;
    }




}
