package com.californiadreamshostel.officetv.MODELS.Weather;

public class CurrentWeatherModel {

    private int temp;
    private String condition;

    public CurrentWeatherModel(){
        this(0, "N/A");
    }

    public CurrentWeatherModel(final int temp, final String condition){
        this.temp = temp;
        this.condition = condition;
    }

    public int getTemp(){
        return temp;
    }

    public String getCondition(){
        return condition;
    }

}
