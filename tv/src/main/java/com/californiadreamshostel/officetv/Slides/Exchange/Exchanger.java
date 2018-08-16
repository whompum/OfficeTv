package com.californiadreamshostel.officetv.Slides.Exchange;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.Arrays;

public class Exchanger implements Handler.Callback {

    //3 Minutes
    public static final long DEFAULT_LIFECYCLE = 180000L;

    public static final int INITIALIZING_POSITION = -1;

    public static final int MESSAGE_DESC = 0Xff;

    private static Exchanger instance;

    private Subject[] subjects = new Subject[0];

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

        final int lastPos = msg.arg1;
        final int newPos = resolveNextPosition(lastPos);

        //Will remain Null if the client removed the subject from the backing array

        if( client.onExchange( subjects[newPos].id, newPos) )
            sendMessage(newPos, subjects[newPos].lifecycle);

        return true;
    }


    public boolean addSubject(@NonNull final Subject subject){

        //Will be index pos after increase by one
        subject.position = subjects.length;

        //Increase array size
        final Subject[] newSubjects = Arrays.copyOf(subjects, subjects.length+1);

        newSubjects[subject.position] = subject;

        this.subjects = newSubjects;

        return true;
    }

    /**
    public boolean removeSubject(@NonNull final String id){

        //If our Array is actually populated
        if(subjects.length > 0)
          for (int i = 0; i < subjects.length; i++)
            if(subjects[i].getId().equals(id))
              return removeAtIndex(i);

     return false;
    }

    private boolean removeAtIndex(final int index){

        final Subject[] newSubjects = new Subject[subjects.length];

        for(int i = 0; i < subjects.length; i++)
          if(i != index)
             newSubjects[i] = subjects[i];

        this.subjects = Arrays.copyOf(newSubjects, newSubjects.length-1);

     return true;
    }
    */

    public void start(){

        //If we don't have anything to show
        if(subjects.length == 0) return;

        sendMessage(INITIALIZING_POSITION, 0);
    }

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    private int resolveNextPosition(int lastPos){

        //The next position will always be one sequential position ahead.
        //IF we've reached the end of the array, we return the beginning.

        //We've Reached end of the array
        if(lastPos >= subjects.length -1 ||
                lastPos == INITIALIZING_POSITION)
            return 0;

        //If not initializing, or restarting
        return lastPos+1;
    }

    private void sendMessage(final int pos, long delay){
        final Message m = Message.obtain(timer);
        m.setTarget(timer);

        m.arg1 = pos;
        m.what = MESSAGE_DESC;

        timer.sendMessageDelayed(m, delay);
    }

    public void stop(){
        client = null;
        timer.removeMessages(MESSAGE_DESC);
        timer = null;
    }


    public static class Subject{
        Integer position = -1;
        private String id;
        private long lifecycle;

        public Subject(@NonNull final String id, long lifecycle){
            this.id = id;
            this.lifecycle = (lifecycle > 0) ? lifecycle : DEFAULT_LIFECYCLE;
        }

        public Subject(@NonNull final String id){
            this(id, 0);
        }

        public Integer getPosition() {
            return position;
        }

        public String getId() {
            return id;
        }

        public long getLifecycle() {
            return lifecycle;
        }
    }

}
