package com.californiadreamshostel.officetv.NETWORKING.DOWNLOADING;

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
import java.net.URL;

public class DataDownloadService extends IntentService {


    public static final String TAG = "DataDownloadService";
    public static final String NAME = "DataDownloadService_Name";

    public static final String URI_KEY = "uri.ky";

    public static final String RESULT_RECEIVER_KEY = "resultReceiver.ky";

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

        /**
        if( !(intent instanceof DownloadIntent)){
            Log.i(TAG, "The intent MUST be an Instance of DownloadIntent");
            Log.i(TAG, "INTENT TYPE: " + intent.getClass().getSimpleName());
            return;
        }
        **/

        listener = intent.getParcelableExtra(RESULT_RECEIVER_KEY);
        final Uri path = intent.getParcelableExtra(URI_KEY);


        if(listener == null){
            Log.i(TAG, "There must be an active listener, the DataDownloadService can call");
            return;
        }

        if(path == null){
            Log.i(TAG, "There must be an active URI endpoint the DataDownloadService to connect to");
            return;
        }


        URL url = null;

        String data = NO_DATA;

        int resultCode = SUCCESS;

        try {
            url = new URL(path.toString());
        }catch (MalformedURLException e){
            Log.i(TAG, "MalformedUrlException: " + e.getLocalizedMessage());
            resultCode = BAD_REQUEST;
        }


        if(url != null) {
            try {
                data = DataDownloader.download(url);
            } catch (IOException e) {
                Log.i(TAG, "IOEXCEPTION: " + e.getMessage());
                e.printStackTrace();
                resultCode = TEAPOT;
                data = NO_DATA;
            }
        }

        final Bundle result = new Bundle();
        result.putString(DATA_KEY, data);
        result.putParcelable(URI_KEY, path);

        listener.send(resultCode,result);

    }

}


