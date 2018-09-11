package com.californiadreamshostel.officetv.UNIT.UNITS.VELOCITY;

import com.californiadreamshostel.officetv.UNIT.UNITS.Unit;

public class Kts extends Unit {
    public static final String TYPE = "KTS";

    {
        UNIT_TYPE = TYPE;
    }

    public Kts(){super();}

    public Kts(final int value){super(value);}

    public Kts(final double value){
        super(value);
    }
}
