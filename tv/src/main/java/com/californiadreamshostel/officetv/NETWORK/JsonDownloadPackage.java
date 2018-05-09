package com.californiadreamshostel.officetv.NETWORK;

import android.support.annotation.NonNull;

public class JsonDownloadPackage {
    public JsonResultListener listener;
    public String url;

    public JsonDownloadPackage(@NonNull final JsonResultListener listener, @NonNull final String url){
        this.listener = listener;
        this.url = url;
    }
}
