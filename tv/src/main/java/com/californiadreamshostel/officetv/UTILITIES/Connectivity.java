package com.californiadreamshostel.officetv.UTILITIES;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

public class Connectivity {

    public static boolean hasNetwork(@NonNull final Context context){

        final ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo network = connectivityManager.getActiveNetworkInfo();

    return network != null && network.isConnected();
    }

}
