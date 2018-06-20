package com.californiadreamshostel.officetv.NETWORKING;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class DataDownloadService extends IntentService {

    public static final String TAG = "DataDownloadService";
    public static final String NAME = "DataDownloadService_Name";

    public static final String URL_KEY = "url.ky";
    public static final String RESPONSE_KEY = "response.ky";
    public static final String DATA_KEY = "data.ky";

    public static final String NO_DATA = "N/A";

    public static final int SUCCESS = 200;

    public static final int BAD_REQUEST = 400;

    public static final int TEAPOT = 418;

    private ResultReceiver listener;

    public DataDownloadService(){
        super(NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        final Object obj = intent.getParcelableExtra(RESPONSE_KEY);

        if(obj == null){
            Log.i(TAG, "RESPONSE_KEY is null");
            return;
        }

        if( !(obj instanceof ResultReceiver) ){
            Log.i(TAG, "RESPONSE_KEY is not an instance of ResultReciever");
            return;
        }

        Log.i("URI_TEST", "tAGed");


        final Uri endpoint = intent.getParcelableExtra(URL_KEY);
        listener = (ResultReceiver) obj;

        URL url = null;

        String data = NO_DATA;

        int resultCode = SUCCESS;

        try {
            url = new URL(endpoint.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.i(TAG, "MalformedUrlException");
            resultCode = BAD_REQUEST;
        }


        if(url != null) {
            try {
                data = DataDownloader.download(url);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "IOException");
                resultCode = TEAPOT;
            }
        }

        final Bundle result = new Bundle();
        result.putString(DATA_KEY, data);

        listener.send(resultCode,result);

        Log.i("URI_TEST", "In DataDownloadService");
    }

}


