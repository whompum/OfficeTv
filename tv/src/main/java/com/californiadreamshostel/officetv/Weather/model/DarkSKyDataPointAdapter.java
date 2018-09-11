package com.californiadreamshostel.officetv.Weather.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DarkSKyDataPointAdapter extends DataPointAdapter<DarkSkyWeather> {

    public DarkSKyDataPointAdapter(@Nullable DarkSkyWeather weather) {
        super(weather);
    }

    @Override
    protected DataPoint setCurrent() {

        if (data.getCurrently() == null)
            return null;

        final DataPoint point = new DataPoint();

        point.setTemperature(data.getCurrently().getTemperature());
        point.setWindSpeed(data.getCurrently().getWindSpeed());
        point.setSummary(data.getCurrently().getIcon());
        point.setMillis(TimeUnit.SECONDS.toMillis(data.getCurrently().getTime()));

        return point;
    }

    @Override
    protected List<DataPoint> setHourly() {

        final List<DataPoint> dataPoints = new ArrayList<>();

        if (data.getHourly() != null) {
            if (data.getHourly().getData() != null && data.getHourly().getData().size() > 0) {
                for (DarkSkyWeather.Hourly.HourlyDataPoints d : data.getHourly().getData()) {

                    final DataPoint point = new DataPoint();
                    point.setSummary(d.getSummary());
                    point.setWindSpeed(d.getWindSpeed());
                    point.setTemperature(d.getTemperature());
                    point.setMillis(TimeUnit.SECONDS.toMillis(d.getTime()));

                    dataPoints.add(point);
                }
            } else return null;
        } else return null;

        return dataPoints;
    }

    @Override
    protected List<DataPoint> setDaily() {

        final List<DataPoint> dataPoints = new ArrayList<>();

        if (data.getDaily() != null) {
            if (data.getDaily().getData() != null && data.getDaily().getData().size() > 0) {
                for (DarkSkyWeather.Daily.DailyDataPoints d : data.getDaily().getData()) {

                    final DataPoint point = new DataPoint();
                    point.setSummary(d.getSummary());
                    point.setMillis(TimeUnit.SECONDS.toMillis(d.getTime()));
                    point.setWindSpeed(d.getWindSpeed());
                    point.setTemperature(d.getTemperatureHigh());

                    dataPoints.add(point);
                }
            } else return null;
        } else return null;

        return dataPoints;
    }
}