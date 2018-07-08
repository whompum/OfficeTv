package com.californiadreamshostel.officetv.WEATHER.Persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.californiadreamshostel.officetv.WEATHER.model.WeatherData;

@Database(entities = {WeatherData.class}, version = 1, exportSchema = false)
public abstract class WeatherRoom extends RoomDatabase {
    public abstract WeatherDaoInterface  getDao();
}
