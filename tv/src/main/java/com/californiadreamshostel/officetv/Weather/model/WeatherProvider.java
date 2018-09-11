package com.californiadreamshostel.officetv.WEATHER.model;

import android.support.annotation.NonNull;

import static com.californiadreamshostel.officetv.WEATHER.model.WeatherDataProxy.*;

import java.util.HashSet;
import java.util.Set;

public class WeatherProvider implements Provider {

    protected static final String F_TEMPERATURE = FIELD_TEMPERATURE;
    protected static final String F_CONDITION = FIELD_CONDITION;
    protected static final String F_WIND_SPEED = FIELD_WINDSPEED;
    protected static final String F_TIMESTAMP = FIELD_TIMESTAMP;

    private static String[] fields = {F_TEMPERATURE, F_CONDITION, F_WIND_SPEED, F_TIMESTAMP};

    private DataPointAdapter adapter;

    @Override
    public Set<Initializer> fetchInitializers() {

        /*
         * The logic below creates a new Initializer object
         * for each DataPoint given to us via the adapter.
         * Currently is its own data point, and hourly/daily
         * are collections of DataPoints
         */
        final Set<Initializer> initializers = new HashSet<>();

        if(adapter == null)
            return initializers;

        if(adapter.getCurrent() !=null)
            initializers.addAll(resolveInitializer(adapter.getCurrent(), DATA_BLOCK.CURRENT));


        if(adapter.getHourly() != null)
            for(int i =0; i < adapter.getHourly().size(); i++)
                initializers.addAll(resolveInitializer((DataPoint)adapter.getHourly().get(i), DATA_BLOCK.HOUR ));

        if(adapter.getDaily() != null)
            for(int i =0; i < adapter.getDaily().size(); i++)
              initializers.addAll(resolveInitializer((DataPoint)adapter.getDaily().get(i), DATA_BLOCK.DAY));

        return initializers;
    }

    @NonNull
    private Set<Initializer> resolveInitializer(@NonNull DataPoint point, @NonNull final DATA_BLOCK type){

        final Set<Initializer> data = new HashSet<>();

        for(String n: fields){
            final Object value = point.getDataFor(n);

            if(value != null){
                final Initializer in = Utils.initializeInitializer(type, n, value, point.getMillis());
                if(in != null) data.add(in);
            }
        }
        return data;
    }

    public WeatherProvider setAdapter(@NonNull DataPointAdapter adapter){
        this.adapter = adapter;
        return this;
    }

}






