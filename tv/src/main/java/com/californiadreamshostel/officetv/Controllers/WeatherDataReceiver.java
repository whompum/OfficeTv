package com.californiadreamshostel.officetv.Controllers;

public interface WeatherDataReceiver {
    void onCurrentWeatherData(final double currentValue, final String condition);
    void onForecastWeatherData(final int projection, final double value, final String day);
}
