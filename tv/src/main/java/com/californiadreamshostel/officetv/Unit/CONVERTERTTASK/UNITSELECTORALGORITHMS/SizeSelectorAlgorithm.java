package com.californiadreamshostel.officetv.Unit.CONVERTERTTASK.UNITSELECTORALGORITHMS;

import com.californiadreamshostel.officetv.Unit.UNITS.SIZE.Ft;
import com.californiadreamshostel.officetv.Unit.UNITS.SIZE.M;
import com.californiadreamshostel.officetv.Unit.UNITS.Unit;

public class SizeSelectorAlgorithm implements UnitSelectorAlgorithm {

    @Override
    public Unit newUnit(Unit currentUnit) {

        if(currentUnit.getType().equals(Ft.TYPE))
            return new M();

        else if(currentUnit.getType().equals(M.TYPE))
            return new Ft();

        return currentUnit;
    }
}
