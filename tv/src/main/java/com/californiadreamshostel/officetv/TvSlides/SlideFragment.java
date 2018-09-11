package com.californiadreamshostel.officetv.TvSlides;

import android.app.Fragment;

public abstract class SlideFragment extends Fragment{
    public final String getTitle(){

        if(!isAdded())
            return getDefaultTitle();

        return getSlideTitle();
    }

    public abstract String getDefaultTitle();
    public abstract String getSlideTitle();
}
