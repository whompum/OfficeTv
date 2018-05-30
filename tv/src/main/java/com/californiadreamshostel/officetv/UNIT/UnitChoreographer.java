package com.californiadreamshostel.officetv.UNIT;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.UnitConvertorFactory;
import com.californiadreamshostel.officetv.UNIT.UNITS.Unit;
import com.californiadreamshostel.officetv.UTILITIES.ANIMATIONS.OnAnimationEnd;
import com.californiadreamshostel.officetv.UTILITIES.ANIMATIONS.TextAnimator;

public class UnitChoreographer {

    private TextView subject;

    private TextAnimator animator;

    private UnitCache unitTracker;

    private Unit tempTo;

    public UnitChoreographer(){
        this.unitTracker = new UnitCache();
    }


    /**
     * Bind the TextView, and the unit into this choreographer object
     * @param subject The textview to operate on
     * @param unit The unit value the TextView is currently at.
     */
    public void bind(@NonNull final TextView subject, @NonNull final Unit unit){
        animator = new TextAnimator(subject, 750L, this.animationEndListener);
        unitTracker.update(subject.getId(), unit);
    }


    /**
     * Changes the unit type of the data
     * @param to the new unit type
     */
    public void changeUnit(@NonNull final Unit to){
        //Fetch conversion value from la converter
        //Pass to the updateValue method
        //then append the Units type display to it (F, C, mph, kph, K, Kts, etc)
        updateValue(to, UnitConvertorFactory.convert(unitTracker.getUnit(), to));
    }


    /**
     * Changes the  Value of the data, whilst preserving the same Unit type
     * @param toValue The new value (assuming the same unit type)
     */
    public void updateValue(@NonNull final double toValue){
        updateValue(unitTracker.getUnit(), toValue);
    }


    /**
     * Returns the Current Unit the data is at
     * @return The current Tracked unit
     */
    public Unit getCurrentUnit(){
        return unitTracker.getUnit();
    }

    /**
     * Return the Animator we're using
     * @return Our Animator Object
     */
    public TextAnimator getAnimator() {
        return animator;
    }


    private void updateValue(@NonNull final Unit to, final double value){
        animator.changeText(String.valueOf(value) + to.getType());
    }

    private void updateWatcher(@NonNull final Unit to){
        unitTracker.update(subject.getId(), to);
    }

    private OnAnimationEnd animationEndListener = new OnAnimationEnd() {
        @Override
        public void onAnimationEnd(Animator animation) {
            updateWatcher(tempTo);
            tempTo = null;
        }
    };

}
