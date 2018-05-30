package com.californiadreamshostel.officetv.UNIT;

import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.UNIT.UNITS.Unit;

public class UnitCache {

    private int id;
    private Unit unit;

    public void update(final int id,@NonNull final Unit unit){
        this.id = id;
        this.unit = unit;
    }

    public int getId(){
        return id;
    }

    public Unit getUnit(){
        return unit;
    }

}
