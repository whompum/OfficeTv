package com.californiadreamshostel.officetv.PARSING;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.californiadreamshostel.officetv.NETWORKING.DOWNLOADING.DataDownloadService;

/**
 * Uses the strategy design pattern to parse data received
 * by the DataDownloadService then call the observer
 * for when the data is parsed
 */
public class ParsingChoreographer extends ResultReceiver{

    private AbstractParsingStrategy strategy;

    private String contentType;

    public ParsingChoreographer(){
        this(new Handler());
    }

    public ParsingChoreographer(@NonNull final Handler handler){
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(resultCode == DataDownloadService.SUCCESS) {

            final String data = resultData.getString(DataDownloadService.DATA_KEY);

            Log.i("PARSE_TEST", "onReceiveResult()#ParsingChoreographer");
            Log.i("PARSE_TEST", "the data is: " + data);

            if(data != null)
                strategy.parseData(data, contentType);
        }
    }


    public void parseWith(@NonNull final AbstractParsingStrategy parser, final String contentType){
        this.contentType = contentType;
        this.strategy = parser;
    }

}
