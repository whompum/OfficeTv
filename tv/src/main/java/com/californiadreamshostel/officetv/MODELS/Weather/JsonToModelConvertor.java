package com.californiadreamshostel.officetv.MODELS.Weather;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonToModelConvertor {


    //Simply fetches the Temperature / Condition from the Json String
    @NonNull
    public static CurrentWeatherModel convertToCurrentWeather(@NonNull final String json){

        JSONObject jsonObject;

        int temperature;

        String condition;

        try {

           jsonObject = new JSONObject(json);

           temperature = jsonObject.getJSONObject("main").getInt("temp");
           condition = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");

        } catch (JSONException e){
            e.printStackTrace();
            return new CurrentWeatherModel();
        }

        return new CurrentWeatherModel(temperature, condition);
    }


}
