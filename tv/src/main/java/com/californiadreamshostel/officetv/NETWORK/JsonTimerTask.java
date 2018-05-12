package com.californiadreamshostel.officetv.NETWORK;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class JsonTimerTask {

    public static final long COUNT_DOWN = TimeUnit.HOURS.toMillis(1);

    private Timer timer;

    private JsonDownloadPackage metaData;

    private int id;

    public static JsonTimerTask begin(@NonNull final JsonDownloadPackage data, final int id){
        Log.i("CaliDreamsTest", "Beginning the JsonTimerTask");
        return new JsonTimerTask(data, id);
    }

    private JsonTimerTask(@NonNull final JsonDownloadPackage metaData, final int id){
        timer = new Timer(COUNT_DOWN, COUNT_DOWN);
        this.metaData = metaData;
        this.id = id;
        Log.i("CaliDreamsTest", "Comienza un nuevo JsonTimerTask");
        beginDownload();
        start();
    }

    public void cancel(){
        if(timer != null)
            timer.cancel();
    }

    private void onFinished(){
        beginDownload();
    }

    private void start(){
        Log.i("CaliDreamsTest", "Starting the JsonTimerTask");

        if(timer != null)
            timer.start();
    }

    private void beginDownload(){
        new JsonDownloaderTask(metaData, id).execute(metaData.url);
        start();
    }

    public int getId(){
        return id;
    }

    private class Timer extends CountDownTimer{

        public Timer(final long time, final long tickTime){
            super(time, tickTime);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            Log.i("CaliDreamsTest", "Timed Task has Finished");

            onFinished();
        }
    }

}
