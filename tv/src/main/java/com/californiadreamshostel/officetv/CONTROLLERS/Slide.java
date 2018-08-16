package com.californiadreamshostel.officetv.CONTROLLERS;

import android.app.Fragment;

public abstract class Slide extends Fragment{

    public final String getTitle(){

        if(!isAdded())
            return getDefaultTitle();

        return getSlideTitle();
    }

    abstract String getDefaultTitle();
    abstract String getSlideTitle();
}
