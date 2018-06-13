package com.californiadreamshostel.officetv.UNIT.CONVERTERTTASK.UNITSELECTORALGORITHMS;

import com.californiadreamshostel.officetv.UNIT.UNITS.SIZE.Ft;
import com.californiadreamshostel.officetv.UNIT.UNITS.SIZE.M;
import com.californiadreamshostel.officetv.UNIT.UNITS.Unit;

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
