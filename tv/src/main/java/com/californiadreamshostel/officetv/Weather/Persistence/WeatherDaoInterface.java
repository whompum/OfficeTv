package com.californiadreamshostel.officetv.Weather.Persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.Weather.model.WeatherData;
import com.californiadreamshostel.officetv.persistence.DaoCacheContract;

import java.util.List;

@Dao
public interface WeatherDaoInterface extends DaoCacheContract<WeatherData> {

    @Override
    @Insert
    void insert(@NonNull WeatherData weatherData);

    @Override
    @Insert
    void insert(WeatherData... t);

    @Override
    @Query("DELETE from WeatherData")
    void deleteAll();

    @Override
    @Query("SELECT * From WeatherData")
    LiveData<List<WeatherData>> fetchAll();

}
