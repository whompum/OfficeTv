package com.californiadreamshostel.officetv.UNIT.CONVERTERTTASK.UNITSELECTORALGORITHMS;

import com.californiadreamshostel.officetv.UNIT.UNITS.Unit;
import com.californiadreamshostel.officetv.UNIT.UNITS.VELOCITY.Kph;
import com.californiadreamshostel.officetv.UNIT.UNITS.VELOCITY.Kts;
import com.californiadreamshostel.officetv.UNIT.UNITS.VELOCITY.Mph;


//TODO Implement a differing selection rather than a sequential selection
public class VelocitySelectorAlgorithm implements UnitSelectorAlgorithm{

    @Override
    public Unit newUnit(Unit currentUnit) {

        if(currentUnit.getType().equals(Kts.TYPE))
            return new Mph();

        if(currentUnit.getType().equals(Mph.TYPE))
            return new Kph();

        if(currentUnit.getType().equals(Kph.TYPE))
            return new Mph();

       return currentUnit;
    }
}
