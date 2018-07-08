package com.californiadreamshostel.officetv.persistence;

import android.support.annotation.NonNull;
import android.util.Log;

public class CacheWriter<T> extends Thread {

    private DaoCacheContract<T> cacheContract;
    private T data;

    public void post(@NonNull final DaoCacheContract<T> cacheContract, @NonNull final T data){

        Log.i("WEATHER_SURF_JOB", "WRITING DATA USING DAO: " + cacheContract.getClass().getSimpleName());
        Log.i("WEATHER_SURF_JOB", "WRITING DATA USING OBJECT: " + data.getClass().getSimpleName());

        this.cacheContract = cacheContract;
        this.data = data;
        start();
    }

    @Override
    public void run() {
        //First remove the data that currently exists
        cacheContract.deleteAll();

        //Now add everything to the cache.
        cacheContract.insert(data);
    }

}
