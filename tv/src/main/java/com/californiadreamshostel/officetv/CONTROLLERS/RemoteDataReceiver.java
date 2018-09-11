package com.californiadreamshostel.officetv.CONTROLLERS;


import android.support.annotation.NonNull;

import com.californiadreamshostel.firebaseclient.ReferenceItem;

public interface RemoteDataReceiver {

    void onDataReceived(final ReferenceItem item,final int changeType);

}
