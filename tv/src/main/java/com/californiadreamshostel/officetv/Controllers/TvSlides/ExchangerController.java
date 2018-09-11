package com.californiadreamshostel.officetv.Controllers.TvSlides;

import android.support.annotation.NonNull;

import com.californiadreamshostel.firebaseclient.DataSchema;
import com.californiadreamshostel.firebaseclient.FirebaseController;
import com.californiadreamshostel.firebaseclient.SimpleEventListener;
import com.californiadreamshostel.officetv.Controllers.OnHeadChange;

public class ExchangerController implements OnHeadChange{

    private FirebaseController controller;
    private SlideList slides = new SlideList(this);

    private OnExchangeListener client;

    ExchangerController(@NonNull final OnExchangeListener client){

        this.client = client;

        controller = new FirebaseController(DataSchema.SLIDES, (item, type) -> {

            if(type == SimpleEventListener.ADDED) onInitialize( ((Slide)item) );
            else if(type == SimpleEventListener.CHANGE) onChange( ((Slide)item) );
            return null;
        });

        controller.setAdapterReferenceFactory((parentId, itemRef, value) -> new Slide(parentId, itemRef, value) );
    }

    /*
     * Invoked After the head has changed
     */
    @Override
    public void onNewHead(@NonNull final Slide s, @NonNull final Slide oldSlide) {
        //Head change happened locally, so update server
        notifyClientOfHeadChange(s, oldSlide);
    }

    public boolean traverseForward(){
        return slides.traverseForward();
    }

    public boolean appendSlide(@NonNull final Slide s){
        return slides.appendSlide(s);
    }

    public void register(){
        controller.register();
    }

    public void unRegister(){
        controller.unRegister();
    }

    public Slide fetchHead(){
        return slides.fetchItem(slides.getHEAD());
    }

    /*
     * Called when the slides are to be initialized.
     * This method is called after a SimpleEventListener.CHANGE
     * value is given in the firebasecontroller callback.
     */
    private void onInitialize(@NonNull final Slide item){
        //Replace each `LIST items childrens values` with `ITEMS childrens values`
        //If item isn't in the list add it. Then order, then hand to onChange

        boolean isInList = false;

        for(int a = 0; a < slides.size(); a++)
            if(item.getReference().equals(slides.get(a).getReference()))
                isInList = true;

        if(isInList)
            slides.appendSlide(item);

        onChange(item);
    }

    /*
     * Called when the remote data has changed.
     * This could be one of 3 things
     * 1) SlideFragment Positions
     * 2) SlideFragment Durations
     * 3) SlideFragment isActive state
     */
    private void onChange(@NonNull final Slide item){

        //Step One, fetch the List item that matches our item.
        final Slide listItem = slides.fetchItem(item.getReference());

        //Step Two, set duration of `listItem` to `item` duration. Shouldn't be null;
        listItem.setDuration(item.getDuration());

        //Step three, Attempt to set positioning. Then order if needed.
        listItem.setPosition(item.getPosition());

        //Order.
        if(!slides.isInOrder())
            slides.order();

        //Set currentslide if its not alredy set
        if(item.isActive() && !slides.getHEAD().equals(item.getReference()))
            slides.setCurrentSlide(listItem.getReference(),
                                  (newSlide, oldSlide) -> notifyClientOfHeadChange(newSlide, oldSlide));
    }

    private void notifyClientOfHeadChange(@NonNull final Slide s, @NonNull final Slide oldSlide){
        client.onExchange(s, oldSlide);
    }

}
