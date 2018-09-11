package com.californiadreamshostel.officetv.Unit.UNITS.VELOCITY;

import com.californiadreamshostel.officetv.Unit.UNITS.Unit;

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
