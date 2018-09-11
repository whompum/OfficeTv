package com.californiadreamshostel.officetv.A;

import android.support.annotation.NonNull;

import com.californiadreamshostel.firebaseclient.DataSchema;

import static com.californiadreamshostel.officetv.A.Exchanger.DEFAULT_LIFECYCLE;

public class Subject {
    int position = -1;
    String id;
    long lifecycle;
    boolean isActive = false;

    public Subject(@NonNull final String id, long lifecycle){
        this.id = id;
        this.lifecycle = (lifecycle > 0) ? lifecycle : DEFAULT_LIFECYCLE;
    }

    public Subject(@NonNull final String id){
        this(id, 0);
    }

    public String getId() {
        return id;
    }

    public long getLifecycle() {
        return lifecycle;
    }

    /*
     * Returns a string value to be used with firebase storage.
     */
    public String isActive(){
        return (isActive) ? String.valueOf(DataSchema.SLIDE_ACTIVE_VALUE)
                          : String.valueOf(DataSchema.SLIDE_INACTIVE_VALUE);
    }
}
