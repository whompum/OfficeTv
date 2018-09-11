package com.californiadreamshostel.officetv.Controllers.TvSlides;

import android.support.annotation.NonNull;
import android.util.Log;

import com.californiadreamshostel.firebaseclient.DataSchema;
import com.californiadreamshostel.firebaseclient.ReferenceItem;

import java.util.concurrent.TimeUnit;

public class Slide extends ReferenceItem {

    private static final String DUR_CHILD = DataSchema.SLIDES_DURATION;
    private static final String POS_CHILD = DataSchema.SLIDES_POSITION;
    private static final String IS_ACTIVE_CHILD = DataSchema.SLIDES_ACTIVE;

    //Used to create local copies
    public Slide(@NonNull final String itemReference){
        super(DataSchema.SLIDES, itemReference, "");

        addChild(new ReferenceItem(itemReference, DataSchema.SLIDES_DURATION, "10")); //10 seconds
        addChild(new ReferenceItem(itemReference, DataSchema.SLIDES_POSITION, "")); //empty.
        addChild(new ReferenceItem(itemReference, DataSchema.SLIDES_ACTIVE, "0")); //0 = false
    }

    //Used for server sided instantiation
    public Slide(@NonNull final String parentRef, @NonNull final String itemRef, @NonNull final String value){
        super(parentRef, itemRef, value);
    }

    /*
     * Converts its seconds to milliseconds
     */
    public long getDuration(){
        final ReferenceItem c = findChildBy(DUR_CHILD);

        if(c != null){
            final String duration = c.getValue();

            if(duration.isEmpty())
                return 15000;

            final long millis = TimeUnit.SECONDS.toMillis(Integer.valueOf(duration));

            Log.i("DURATION_FIX", "Duration for slide: " + getReference() + " is: " + String.valueOf(millis));


            return TimeUnit.SECONDS.toMillis(Integer.valueOf(duration));
        }

        return 0;
    }

    /*
     * Set via millis; Converted to Seconds;
     */
    public void setDuration(final long duration){

        final ReferenceItem c = findChildBy(DUR_CHILD);

        if(c != null){
            //Convert duration to seconds. Then to a string
            final int seconds = (int)TimeUnit.MILLISECONDS.toSeconds(duration);
            c.setValue(String.valueOf(seconds));
        }
    }

    public int getPosition(){

        final ReferenceItem c = findChildBy(POS_CHILD);

        if(c != null)
            return Integer.valueOf(c.getValue());

    return -1;
    }

    public void setPosition(final int position){

        final ReferenceItem c = findChildBy(POS_CHILD);

        if(c != null)
            c.setValue(String.valueOf(position));
    }

    public boolean isActive(){
        final ReferenceItem c = findChildBy(IS_ACTIVE_CHILD);

        if(c != null){
            return Integer.valueOf(c.getValue()) == DataSchema.SLIDE_ACTIVE_VALUE;
        }
    return false;
    }

    public void setIsActive(final boolean isActive){
        final ReferenceItem c = findChildBy(IS_ACTIVE_CHILD);

        if(c != null)
            c.setValue( (isActive) ? String.valueOf(DataSchema.SLIDE_ACTIVE_VALUE)
                                   : String.valueOf(DataSchema.SLIDE_INACTIVE_VALUE));
    }

}
