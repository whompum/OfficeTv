package com.californiadreamshostel.officetv.Utilities.ANIMATIONS;

import android.animation.Animator;

public abstract class OnAnimationEnd implements Animator.AnimatorListener {

    @Override
    public void onAnimationStart(Animator animation) {

    }

    public abstract void onAnimationEnd(Animator animation);

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
