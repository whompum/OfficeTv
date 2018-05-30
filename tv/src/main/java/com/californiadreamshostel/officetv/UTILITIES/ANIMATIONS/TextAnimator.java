package com.californiadreamshostel.officetv.UTILITIES.ANIMATIONS;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;

//Animates TextViews between two texts
public class TextAnimator{

    private TextView sbj;
    private long duration;

    //Just default
    private Animator.AnimatorListener listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public TextAnimator(@NonNull TextView textView, final long duration, Animator.AnimatorListener listener){
        this.sbj = textView;
        this.duration = duration;
        if(listener != null)
        this.listener = listener;
    }


    public void changeText(@NonNull final String newText){
        this.spinX(newText);
    }

    public void spinX(@NonNull final String newText){

        final ValueAnimator animator = ValueAnimator.ofInt(0, 360);

        animator.setDuration(duration);
        animator.setInterpolator(new AnticipateOvershootInterpolator());

        animator.addListener(listener);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                final float fraction = valueAnimator.getAnimatedFraction();

                final int newRotation = (int)(360 * fraction);

                sbj.setRotationY(newRotation);

                if(fraction > 0.5F &
                        !sbj.getText().toString().equals(newText))
                    sbj.setText(newText);

            }
        });
        animator.start();
    }



}
