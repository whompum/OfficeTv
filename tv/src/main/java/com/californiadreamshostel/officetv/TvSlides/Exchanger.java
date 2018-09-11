package com.californiadreamshostel.officetv.TvSlides;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;


/*
 * Is the jelly and peanut butter between the ExchangerController
 * and the client. Basically provides a timing abstraction that provides
 * Slides at appropriate times.
 */
public class Exchanger implements Handler.Callback, OnExchangeListener {

    //30 Seconds
    public static final long DEFAULT_LIFECYCLE = 30000L;
    static final int MESSAGE_DESC = 0Xff;

    private static Exchanger instance;

    private ExchangerController controller = new ExchangerController(this);

    private OnExchangeListener client;
    private Handler timer = new Handler(this);

    private Exchanger(@NonNull final OnExchangeListener client){
        this.client = client;
    }

    public static Exchanger obtain(@NonNull final OnExchangeListener client){
        if(instance == null)
            instance = new Exchanger(client);

        return instance;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return controller.traverseForward();
    }

    @Override
    public boolean onExchange(@NonNull final Slide s, @NonNull final Slide oldSlide) {

        final long delay = s.getDuration();

        if(client.onExchange(s, null))
            timer.sendEmptyMessageDelayed(MESSAGE_DESC, delay);

        return true;
    }

    public boolean addSubject(@NonNull final Slide s){
        return controller.appendSlide(s);
    }

    public void start(){
        controller.register();
        timer.sendEmptyMessageDelayed(MESSAGE_DESC, controller.fetchHead().getDuration());
    }

    public void stop(){
        timer.removeMessages(MESSAGE_DESC);
        controller.unRegister();
    }

}
