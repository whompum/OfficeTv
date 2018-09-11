package com.californiadreamshostel.officetv.CONTROLLERS;

public interface WeatherDataReceiver {
    void onCurrentWeatherData(final double currentValue, final String condition);
    void onForecastWeatherData(final int projection, final double value, final String day);
}
