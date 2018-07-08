package com.californiadreamshostel.officetv.PARSING;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.californiadreamshostel.officetv.NETWORKING.ContentType;
import com.californiadreamshostel.officetv.NETWORKING.DOWNLOADING.DataDownloadService;


/**
 * Base strategy class for parsing strategies.
 * Subclasses are responsible for setting the generic type of this class
 * @param <T> The object type we're working with
 */
public abstract class AbstractParsingStrategy<T> extends ResultReceiver{

    private T data;
    private IParsingFinishedObserver observer;

    public AbstractParsingStrategy(){
        super(new Handler());
    }

    public AbstractParsingStrategy(@NonNull final IParsingFinishedObserver observer){
        this();

        this.observer = observer;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        Log.i("WEATHER_SURF_JOB", "RESULT CODE: " + resultCode);
        Log.i("WEATHER_SURF_JOB", "RESPONSE: " + resultData.getString(DataDownloadService.DATA_KEY));

        if(resultCode == DataDownloadService.SUCCESS){

            final String response = resultData.getString(DataDownloadService.DATA_KEY);

            if(!response.equals(DataDownloadService.NO_DATA))
                parseData(response, ContentType.TYPE_JSON);
            else
                Log.i("WEATHER_SURF_JOB", "The data returned as NO_DATA");
        }

    }


    protected abstract void parseData(@NonNull String response, @NonNull String contentType);

    protected void setData(@Nullable final T t){
        this.data = t;
        if(t != null)
            notifyObserver();
    }

    protected void notifyObserver(){
        if(observer != null)
            observer.onDataParsed(getData());

    }

    @Nullable
    public T getData() {
        return data;
    }
}
