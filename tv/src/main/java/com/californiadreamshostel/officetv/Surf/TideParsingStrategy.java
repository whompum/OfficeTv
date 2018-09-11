package com.californiadreamshostel.officetv.Surf;

import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.Networking.ContentType;
import com.californiadreamshostel.officetv.Parsing.AbstractParsingStrategy;
import com.californiadreamshostel.officetv.Parsing.IParsingFinishedObserver;
import com.californiadreamshostel.officetv.Surf.MODELS.Tide;
import com.google.gson.Gson;

/**
 * NOTE: This data comes solely from Spitcast.com
 */
public class TideParsingStrategy extends AbstractParsingStrategy<Tide[]> {

    public TideParsingStrategy(){
        super();
    }

    public TideParsingStrategy(@NonNull IParsingFinishedObserver o){
        super(o);
    }

    @Override
    public void parseData(@NonNull String response, @NonNull String contentType) {

        if(contentType.equals(ContentType.TYPE_JSON))
            setData(new Gson().fromJson(response, Tide[].class));
    }
}
