package com.californiadreamshostel.officetv.Controllers.TvSlides;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.californiadreamshostel.officetv.Controllers.OnHeadChange;

import java.util.ArrayList;
import java.util.Collections;

public class SlideList extends ArrayList<Slide> {
    public static final String UNINITIALIZED = "UNINITIALIZED";
    private String HEAD = UNINITIALIZED;

    private OnHeadChange listener;

    public SlideList(@NonNull final OnHeadChange listener){
        this.listener = listener;
    }

    boolean appendSlide(@NonNull final Slide item){

        if(HEAD.equals(UNINITIALIZED)) {
            HEAD = item.getReference();
            item.setIsActive(true);
        }

        item.setPosition(size());  //after add operation position will be size()-1 i.e. lastIndex
        return add(item);
    }

    boolean traverseForward(){
        Slide s;
        if( (s = findNextSlide()) != null) {
            setCurrentSlide(s.getReference(), null);
            return true;
        }
        return false;
    }

    void setCurrentSlide(int pos, @Nullable final OnHeadChange l){
        setCurrentSlide(get(pos).getReference(), l);
    }

    void setCurrentSlide(final String slideId, @Nullable final OnHeadChange l){

        final Slide tempOldHead = fetchItem(HEAD);

        HEAD = slideId;

        final Slide newHead = fetchItem(HEAD);

        for(Slide s: this)
            s.setIsActive(s.getReference().equals(HEAD)); //Turn on / off appropriately.

        if(l != null) //Use a custom listener
            l.onNewHead(newHead, tempOldHead);
        else //Use provided listener
            listener.onNewHead(fetchItem(HEAD), tempOldHead);
    }


    /*
     * Returns the next slide
     *
     * Cycles back to the first slide, if at the last slide.
     *
     */
    @Nullable
    Slide findNextSlide(){

        int headPos = findSlidePosition(HEAD);

        return (size() > 0) ? get(((headPos + 1) >= size()) ? 0 : (headPos+1)) : null;
    }

    /*
     * Returns the previous slide
     */
    @Nullable
    Slide findPreviousSlide(){
        int headPos = findSlidePosition(HEAD);

        return (size() > 0) ? get( ((headPos - 1) < 0) ? size()-1 : (headPos-1)) : null;
    }

    /*
     * Returns the slide position
     */
    int findSlidePosition(String slideId){

        for(int a = 0; a < size(); a++)
            if(get(a).getReference().equals(slideId))
                return a;

        return -1;
    }

    void order(){
        Collections.sort(this, (s1, s2) -> s1.getPosition() - s2.getPosition());
    }

    /*
     * Returns the position of HEAD
     */
    String getHEAD(){
        return HEAD;
    }

    int getHEADPos(){
        return findSlidePosition(HEAD);
    }

    @Nullable
    Slide fetchItem(String slideId){
        return get(findSlidePosition(slideId));
    }

    long getSlideDuration(int pos){
        return get(pos).getDuration();
    }

    long getSlideDuration(String slideId){
        return getSlideDuration(findSlidePosition(slideId));
    }

    void setSlideDuration(int pos, final long duration){
        get(pos).setDuration(duration);
    }

    void setSlideDuration(String slideId, final long duration){
        setSlideDuration(findSlidePosition(slideId), duration);
    }
    boolean traverseBackwards(){
        Slide s;
        if( (s = findPreviousSlide()) != null) {
            setCurrentSlide(s.getReference(), null);
            return true;
        }
        return false;
    }

    /*
     * Returns whether the data is in order by position or not.
     * E.G. each items position value matches its index in the List.
     */
    boolean isInOrder(){
        return true;
    }

}
