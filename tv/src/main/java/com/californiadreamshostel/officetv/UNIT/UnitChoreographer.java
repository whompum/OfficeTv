package com.californiadreamshostel.officetv.UNIT;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.UnitConvertorFactory;
import com.californiadreamshostel.officetv.UNIT.UNITS.Unit;
import com.californiadreamshostel.officetv.UTILITIES.ANIMATIONS.OnAnimationEnd;
import com.californiadreamshostel.officetv.UTILITIES.ANIMATIONS.TextAnimator;


//TODO Refractor a views id out of the UnitCache mapping.
public class UnitChoreographer {

    public static final String TAG = "UnitChoreographer";

    private TextView subject;

    private TextAnimator animator;

    private UnitCache unitCache;

    private Unit tempTo;

    public UnitChoreographer(@NonNull final TextView subject){
        this.unitCache = new UnitCache();
        this.subject = subject;
        this.animator = new TextAnimator(subject, 750L, this.animationEndListener);
    }


    /**
     * Bind the TextView, and the unit into this choreographer object
     * This method is the entry point for this object
     * @param unit The unit value the TextView is currently at.
     * @param value Not necessary, as it can be coupled into the Unit, however needed so a client
     *              doesn't forget to include the value
     */
    public void bind(@NonNull final Unit unit, final double value){
        updateUnitCache(unit);
        updateValue(unit, value);
    }


    /**
     * Changes the unit type of the data
     * @param to the new unit type
     */
    public void changeUnit(@NonNull final Unit to){

        if(unitCache.getUnit() == null)
            throw new IllegalStateException("Failure to call bind() before change unit");


        Log.i(TAG, "FROM TYPE: " + unitCache.getUnit().getType());
        Log.i(TAG, "TO TYPE: " + to.getType());

        //Fetch conversion value from la converter
        //Pass to the updateValue method
        //then append the Units type display to it (F, C, mph, kph, K, Kts, etc)
        updateValue(to, UnitConvertorFactory.convert(unitCache.getUnit(), to));
    }


    /**
     * Changes the  Value of the data, whilst preserving the same Unit type
     * @param toValue The new value (assuming the same unit type)

         public void updateValue(@NonNull final double toValue){
             updateValue(unitCache.getUnit(), toValue);
         }

     **/

    /**
     * Returns the Current Unit the data is at
     * @return The current Tracked unit
     */
    public Unit getCurrentUnit(){
        return unitCache.getUnit();
    }

    /**
     * Return the Animator we're using
     * @return Our Animator Object
     */
    public TextAnimator getAnimator() {
        return animator;
    }


    private void updateValue(@NonNull final Unit to, final double value){
        Log.i(TAG, "value for updateValue: " + String.valueOf(value));

        //Assigning the next unit value so we can add it to the cache.
        tempTo = to;
        tempTo.setValue(value);
        animator.changeText(String.valueOf((int)value) + to.getStringRepresentation());
    }

    private void updateUnitCache(@NonNull final Unit to){
        unitCache.update(subject.getId(), to);
    }

    private OnAnimationEnd animationEndListener = new OnAnimationEnd() {
        @Override
        public void onAnimationEnd(Animator animation) {
            updateUnitCache(tempTo);
        }
    };

}
