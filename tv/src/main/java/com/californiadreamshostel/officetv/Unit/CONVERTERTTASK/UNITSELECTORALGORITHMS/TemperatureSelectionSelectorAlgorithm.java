package com.californiadreamshostel.officetv.Unit.CONVERTERTTASK.UNITSELECTORALGORITHMS;

import android.util.Log;

import com.californiadreamshostel.officetv.Unit.UNITS.TEMPERATURE.C;
import com.californiadreamshostel.officetv.Unit.UNITS.TEMPERATURE.F;
import com.californiadreamshostel.officetv.Unit.UNITS.Unit;

public class TemperatureSelectionSelectorAlgorithm implements UnitSelectorAlgorithm {

    @Override
    public Unit newUnit(Unit currentUnit) {

        Log.i("UNIT_CONVERSION", "Converting");
        Log.i("UNIT_CONVERSION", "Current Unit: " + currentUnit.getType());

        return (currentUnit.getType().equals(F.TYPE))
                ? new C()
                : new F();
    }
}
