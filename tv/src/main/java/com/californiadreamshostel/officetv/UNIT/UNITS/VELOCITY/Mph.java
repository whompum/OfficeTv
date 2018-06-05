package com.californiadreamshostel.officetv.UNIT.UNITS.VELOCITY;

import com.californiadreamshostel.officetv.UNIT.UNITS.Unit;

public class Mph extends Unit {
    public static final String TYPE = "MPH";

    {
        UNIT_TYPE = TYPE;
    }

    public Mph(){
        super();
    }

    public Mph(final int value){
        super(value);
    }

    public Mph(final double value){
        super(value);
    }

}
