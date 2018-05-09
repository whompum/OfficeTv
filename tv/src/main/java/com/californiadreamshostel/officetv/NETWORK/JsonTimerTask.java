package com.californiadreamshostel.officetv.NETWORK;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

public class JsonTimerTask {

    public static final long COUNT_DOWN = TimeUnit.HOURS.toMillis(1);

    private Timer timer;

    private JsonDownloadPackage metaData;

    public static JsonTimerTask begin(@NonNull final JsonDownloadPackage data){
        return new JsonTimerTask(data);
    }

    private JsonTimerTask(@NonNull final JsonDownloadPackage metaData){
        timer = new Timer(COUNT_DOWN, COUNT_DOWN);
        this.metaData = metaData;
    }

    public void cancel(){
        if(timer != null)
            timer.cancel();
    }

    public void onFinished(){
        beginDownload();
    }

    private void start(){
        if(timer != null)
            timer.start();
    }

    private void beginDownload(){
        new JsonDownloaderTask(metaData.listener).execute(metaData.url);
        start();
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
            onFinished();
        }
    }

}
