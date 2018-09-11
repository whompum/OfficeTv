package com.californiadreamshostel.officetv.Unit;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.californiadreamshostel.officetv.Unit.CONVERTER.ConversionType;
import com.californiadreamshostel.officetv.Unit.CONVERTER.UnitConvertorFactory;
import com.californiadreamshostel.officetv.Unit.UNITS.Unit;
import com.californiadreamshostel.officetv.Utilities.ANIMATIONS.OnAnimationEnd;
import com.californiadreamshostel.officetv.Utilities.ANIMATIONS.TextAnimator;

import java.util.Locale;


//TODO Refractor a views id out of the UnitCache mapping.
public class UnitChoreographer {

    public static final String TAG = "UnitChoreographer";


    private TextView subject;

    private TextAnimator animator;

    private UnitCache unitCache;

    private Unit tempTo;

    private ConversionType conversionType;

    private boolean usePrettyDecimals = false;

    public UnitChoreographer(@NonNull final TextView subject, @NonNull final ConversionType conversion_type){
        this(subject, conversion_type, false);
    }


    public UnitChoreographer(@NonNull final TextView sbj, @NonNull ConversionType conversionType,
                             final boolean usePrettyDecimals){
        this.unitCache = new UnitCache();
        this.subject = sbj;
        this.animator = new TextAnimator(subject, 750L, this.animationEndListener);
        this.conversionType = conversionType;

        usePrettyDecimals(usePrettyDecimals);
    }


    /**
     * Called when a value is given within the unit object
     * @param unit the new Unit type of this object
     */
    public void bind(@NonNull final Unit unit){
        this.bind(unit, unit.value());
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

        if(!isBounded())
            throw new IllegalStateException("Failure to call bind() before changeUnit()");


        Log.i(TAG, "FROM TYPE: " + unitCache.getUnit().getType());
        Log.i(TAG, "TO TYPE: " + to.getType());

        //Fetch conversion value from la converter
        //Pass to the updateValue method
        //then append the Units type display to it (F, C, mph, kph, K, Kts, etc)

        final Unit current = unitCache.getUnit();

        final double convertedValue = UnitConvertorFactory
                .convert(current.getType(), to.getType(), current.value());


        updateValue(to, convertedValue);
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

    public ConversionType getConversionType() {
        return conversionType;
    }

    public void usePrettyDecimals(final boolean usePrettyDecimals){
        this.usePrettyDecimals = usePrettyDecimals;
    }

    public boolean areWeBeingPretty(){
        return usePrettyDecimals;
    }

    private void updateValue(@NonNull final Unit to, final double value){
        Log.i(TAG, "value for updateValue: " + String.valueOf(value));

        //Assigning the next unit value so we can add it to the cache.
        tempTo = to;
        tempTo.setValue(value);

        String format;

        //Maybe remove the responsibility of text formatting from this object?
        if(!usePrettyDecimals)
            format = "%.0f";
        else
            format = "%.1f";

        final String formattedValue = String.format(Locale.getDefault(),
                format, value);


        animator.changeText(formattedValue + to.getStringRepresentation());
    }

    /**
     *
     * @return whether or not this choreographer has data bounded to it
     */
    public boolean isBounded(){
        return unitCache.getUnit() != null;
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
