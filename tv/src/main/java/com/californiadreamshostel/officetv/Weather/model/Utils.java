package com.californiadreamshostel.officetv.WEATHER.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import static com.californiadreamshostel.officetv.WEATHER.model.WeatherProvider.F_CONDITION;
import static com.californiadreamshostel.officetv.WEATHER.model.WeatherProvider.F_TEMPERATURE;
import static com.californiadreamshostel.officetv.WEATHER.model.WeatherProvider.F_TIMESTAMP;
import static com.californiadreamshostel.officetv.WEATHER.model.WeatherProvider.F_WIND_SPEED;

public class Utils {

    @Nullable
    protected static Initializer initializeInitializer(@NonNull WeatherDataProxy.DATA_BLOCK type,
                                                       String field,
                                                       @Nullable final Object value,
                                                       final long millis) {

        if (value == null)
            return null;

        Initializer initializer = null;

        switch (field) {
            case F_TEMPERATURE:
                initializer = new Initializer<Double>();
                break;

            case F_CONDITION:
                initializer = new Initializer<String>();
                break;

            case F_TIMESTAMP:
                initializer = new Initializer<Long>();
                break;

            case F_WIND_SPEED:
                initializer = new Initializer<Double>();
                break;

        }

        if (initializer != null) {
            initializer.type = type;
            initializer.value = value;
            initializer.fieldName = field;
            initializer.timestamp = millis;
        }

        return initializer;
    }
}