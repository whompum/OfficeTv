package com.californiadreamshostel.officetv.WEATHER;

import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.Parsing.IParsingFinishedObserver;
import com.californiadreamshostel.officetv.Parsing.AbstractParsingStrategy;
import com.californiadreamshostel.officetv.Networking.ContentType;
import com.californiadreamshostel.officetv.WEATHER.model.DarkSkyWeather;
import com.google.gson.Gson;

public class DarkSkyParsingStrategy extends AbstractParsingStrategy<DarkSkyWeather> {

    public DarkSkyParsingStrategy(){

    }

    public DarkSkyParsingStrategy(@NonNull final IParsingFinishedObserver observer){
        super(observer);
    }

    /**
     * Parses the data using GSON if the mimeType is json
     * @param jsonData the Text type of data to parse
     */
    @Override
    public void parseData(@NonNull String jsonData, @NonNull final String contentType) {
        if(contentType.equals(ContentType.TYPE_JSON))
             setData(new Gson().fromJson(jsonData, DarkSkyWeather.class));
    }

}






