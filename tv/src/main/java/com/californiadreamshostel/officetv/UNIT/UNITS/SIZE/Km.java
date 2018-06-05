package com.californiadreamshostel.officetv.UNIT.UNITS.SIZE;

import com.californiadreamshostel.officetv.UNIT.UNITS.Unit;

public class Km extends Unit {
    public static final String TYPE = "KM";

    {
        UNIT_TYPE = TYPE;
    }

    public Km(){
        super();
    }

    public Km(final int value){
        super(value);
    }

    public Km(final double value){
        super(value);
    }

}
