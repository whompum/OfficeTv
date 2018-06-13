package com.californiadreamshostel.officetv.UNIT.CONVERTERTTASK;

import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.ConversionType;
import com.californiadreamshostel.officetv.UNIT.CONVERTERTTASK.UNITSELECTORALGORITHMS.SizeSelectorAlgorithm;
import com.californiadreamshostel.officetv.UNIT.CONVERTERTTASK.UNITSELECTORALGORITHMS.TemperatureSelectionSelectorAlgorithm;
import com.californiadreamshostel.officetv.UNIT.CONVERTERTTASK.UNITSELECTORALGORITHMS.UnitSelectorAlgorithm;
import com.californiadreamshostel.officetv.UNIT.CONVERTERTTASK.UNITSELECTORALGORITHMS.VelocitySelectorAlgorithm;
import com.californiadreamshostel.officetv.UNIT.UNITS.Unit;
import com.californiadreamshostel.officetv.UNIT.UnitChoreographer;
import com.californiadreamshostel.officetv.UTILITIES.TickTockerTasker;

import java.util.HashSet;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class UnitUpdator extends TickTockerTasker {

    //One Minute Default conversion time
    public static final long TASK_INTERVAL_DEFAULT = TimeUnit.MINUTES.toMillis(1);

    final Set<InternalStateHolder> subjects = new HashSet<>();

    //Locks the update from happening at instantiation
    private boolean firingLock = false;

    public UnitUpdator(/**Set of UnitChoreographers*/){
        this(TASK_INTERVAL_DEFAULT);
    }


    public UnitUpdator(final long time){
        super(time);
    }

    @Override
    public void update() {

        if(!firingLock) {
            firingLock = true;
            return;
        }

        final Iterator<InternalStateHolder> subjectIterator = subjects.iterator();

        while(subjectIterator.hasNext()) {
            final InternalStateHolder holder = subjectIterator.next();

            final UnitChoreographer choreographer = holder.subject;

            if(!choreographer.isBounded())
                continue;

            final Unit newUnit = holder.newUnitAlgorithm.newUnit(choreographer.getCurrentUnit());

            //Assign a new  unit to the choreographer
            choreographer.changeUnit(newUnit);
        }

    }

    /**
     * Used from the client to add a new UnitChoreographer object,
     * as well as a conversion type that will specify the type of conversion algorithm to use
     * @param choreographer The choreographer
     * @param conversionType The object that chooses the new unit @ runtime to convert to
     */
    public void bind(@NonNull UnitChoreographer choreographer, @NonNull ConversionType conversionType){

        UnitSelectorAlgorithm newUnitAlgorithm = null;

        if(conversionType == ConversionType.TEMPERATURE)
            newUnitAlgorithm = new TemperatureSelectionSelectorAlgorithm();

        else if(conversionType == ConversionType.SIZE)
            newUnitAlgorithm = new SizeSelectorAlgorithm();

        else if(conversionType == ConversionType.VELOCITY)
            newUnitAlgorithm = new VelocitySelectorAlgorithm();


        this.bind(choreographer, newUnitAlgorithm);
    }


    /**
     * Used by a client to bind a choreographer, and their own UnitSelectionAlgorithm
     * @param choreographer The choreographer unit conversion object
     * @param unitSelectorAlgorithm The client-implemented unitSelectionAlgorithm
     */
    public void bind(@NonNull UnitChoreographer choreographer, @NonNull UnitSelectorAlgorithm unitSelectorAlgorithm){
        final InternalStateHolder holder = new InternalStateHolder();
        holder.subject = choreographer;
        holder.newUnitAlgorithm = unitSelectorAlgorithm;

        subjects.add(holder);
    }


    private static class InternalStateHolder{
        UnitSelectorAlgorithm newUnitAlgorithm;
        UnitChoreographer subject;
    }

}
