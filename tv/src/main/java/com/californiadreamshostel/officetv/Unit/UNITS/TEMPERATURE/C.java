package com.californiadreamshostel.officetv.Unit.UNITS.TEMPERATURE;

import com.californiadreamshostel.officetv.Unit.UNITS.Unit;

public class C extends Unit {

    public static final String TYPE = "C";

    {
        UNIT_TYPE = TYPE;
    }

    public C(){
        super();
    }

    public C(final double value){
        super(value);
    }

    public C(final int value){
        super(value);
    }

    @Override
    public String getStringRepresentation() {
        return "\u2103";
    }
}
