package com.californiadreamshostel.officetv.WEATHER;

import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.NETWORKING.JSON.JsonDownloadTask;
import com.californiadreamshostel.officetv.NETWORKING.JSON.ResponseListener;
import com.californiadreamshostel.officetv.UTILITIES.TickTockerTasker;

import java.util.concurrent.TimeUnit;


public class WeatherUpdator extends TickTockerTasker {

    private TaskMetaInfo metaInfo;


    public WeatherUpdator(@NonNull TaskMetaInfo metaInfo, boolean startNow){
        super(TimeUnit.HOURS.toMillis(1));
        this.metaInfo = metaInfo;
        if (startNow)
            start();
    }

    @Override
    public void update() {
        new JsonDownloadTask(metaInfo.listener).execute(metaInfo.url);
    }

    public static class TaskMetaInfo{
        private ResponseListener listener;
        private String url;

        public TaskMetaInfo(@NonNull ResponseListener listener, @NonNull String url){
            this.listener = listener;
            this.url = url;
        }

    }

}
