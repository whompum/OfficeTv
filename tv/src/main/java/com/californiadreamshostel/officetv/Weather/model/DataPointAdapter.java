package com.californiadreamshostel.officetv.Weather.model;

import android.support.annotation.Nullable;

import com.californiadreamshostel.officetv.Weather.model.DataPoint;

import java.util.List;

public abstract class DataPointAdapter<T> {

    protected T data;

    @Nullable private DataPoint current;
    @Nullable private List<DataPoint> hourly;
    @Nullable private List<DataPoint> daily;

    public DataPointAdapter(@Nullable T t){
        if(t != null)
            setData(t);
    }

    protected abstract DataPoint setCurrent();
    protected abstract List<DataPoint> setHourly();
    protected abstract List<DataPoint> setDaily();

    private void setData(T t){
        this.data = t;

        current = setCurrent();
        hourly = setHourly();
        daily = setDaily();

    }

    public DataPoint getCurrent() {
        return current;
    }

    public List<DataPoint> getHourly() {
        return hourly;
    }

    public List<DataPoint> getDaily() {
        return daily;
    }

}


