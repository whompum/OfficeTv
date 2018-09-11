package com.californiadreamshostel.officetv.Utilities;

import android.os.CountDownTimer;

public abstract class TickTockerTasker extends CountDownTimer {

    public TickTockerTasker(final long tickDur){
        super(Long.MAX_VALUE, tickDur);
    }

    public abstract void update();

    @Override
    public void onTick(long millisUntilFinished) {
        update();
    }

    @SuppressWarnings("unused")
    @Override
    public void onFinish() {}


}
