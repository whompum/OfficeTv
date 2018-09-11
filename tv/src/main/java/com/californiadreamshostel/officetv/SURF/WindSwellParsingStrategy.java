package com.californiadreamshostel.officetv.SURF;

import android.support.annotation.NonNull;
import android.util.Log;

import com.californiadreamshostel.officetv.Parsing.AbstractParsingStrategy;
import com.californiadreamshostel.officetv.Parsing.IParsingFinishedObserver;
import com.californiadreamshostel.officetv.SURF.MODELS.WindAndSwell;
import com.google.gson.Gson;

/**
 * MAGIC SEA WEED RETURNS DATA AS A JSON-ARRAY
 */
public class WindSwellParsingStrategy extends AbstractParsingStrategy<WindAndSwell[]>{

    public WindSwellParsingStrategy(){

    }

    public WindSwellParsingStrategy(@NonNull final IParsingFinishedObserver observer){
        super(observer);
    }

    @Override
    public void parseData(@NonNull String response, @NonNull String contentType) {

        Log.i("WIND_SWELL_FIX", "RESPONSE: " + response);

        setData(new Gson().fromJson(response, WindAndSwell[].class));

    }
}
