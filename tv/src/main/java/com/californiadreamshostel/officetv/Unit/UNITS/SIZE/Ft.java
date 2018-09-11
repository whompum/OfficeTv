package com.californiadreamshostel.officetv.Unit.UNITS.SIZE;

import com.californiadreamshostel.officetv.Unit.UNITS.Unit;

public class Ft extends Unit {
    public static final String TYPE = "FT";

    {
        UNIT_TYPE = TYPE;
    }

    public Ft(){
        super();
    }

    public Ft(final int value){
        super(value);
    }

    public Ft(final double value){
        super(value);
    }

}
