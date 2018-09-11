package com.californiadreamshostel.officetv.WEATHER.Persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.WEATHER.model.WeatherData;

@Database(entities = {WeatherData.class}, version = 1, exportSchema = false)
public abstract class WeatherRoom extends RoomDatabase {

    public static final String DB_NAME = "weather.db";

    public abstract WeatherDaoInterface  getDao();

    private static WeatherRoom instance;

    public static WeatherRoom obtain(@NonNull final Context context){

        if(instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), WeatherRoom.class, DB_NAME).build();

        return instance;
    }

}
