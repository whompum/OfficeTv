package com.californiadreamshostel.officetv.NETWORKING.DOWNLOADING;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DownloadIntent extends Intent {

    public DownloadIntent(@NonNull final Context context){
        super(context, DataDownloadService.class);
    }

    public DownloadIntent(@NonNull final Context context,
                          @NonNull final Uri uri,
                          @NonNull final ResultReceiver receiver){
        super(context, DataDownloadService.class);
        setUri(uri);
        setResponseListener(receiver);
    }

    public void setUri(@NonNull final Uri uri){
        putExtra(DataDownloadService.URI_KEY, uri);
    }

    public void setResponseListener(@NonNull final ResultReceiver responseListener){
       putExtra(DataDownloadService.RESULT_RECEIVER_KEY, responseListener);
    }

    public Uri getUri(){
        return getParcelableExtra(DataDownloadService.URI_KEY);
    }

    public ResultReceiver getResponseListener(){
        return getParcelableExtra(DataDownloadService.RESULT_RECEIVER_KEY);
    }

}
