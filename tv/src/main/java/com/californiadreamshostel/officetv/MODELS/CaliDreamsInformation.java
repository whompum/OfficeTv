package com.californiadreamshostel.officetv.MODELS;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

public class CaliDreamsInformation {


    private HashMap<String, String> information;

    private FirebaseDataChangeObserver observer;

    protected CaliDreamsInformation(){}

    @Nullable
    public String fetch(@NonNull final String key){
        if(information != null)
            if(information.containsKey(key))
                return information.get(key);

        return null;
    }

    public void registerObserver(@NonNull final FirebaseDataChangeObserver observer){
        this.observer = observer;
    }

    private void notifyDataChange(final String key){
        observer.onDataChange(key);
    }

}
