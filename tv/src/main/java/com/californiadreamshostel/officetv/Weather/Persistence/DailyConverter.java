package com.californiadreamshostel.officetv.Weather.Persistence;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.Weather.model.WeatherData.Daily.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DailyConverter {

    @TypeConverter
    public static String convert(@NonNull final List<DataPoints> data){
        return new Gson().toJson(data, new TypeToken<List<DataPoints>>(){}.getType());
    }

    @TypeConverter
    public static List<DataPoints> unConvert(String json){

        final DataPoints[] dataPoints =
                new Gson().fromJson(json, DataPoints[].class);

        final List<DataPoints> dataList = new ArrayList<>(
                dataPoints.length
        );

        for(DataPoints d: dataPoints)
            dataList.add(d);

        return dataList;

    }

}
