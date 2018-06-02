package com.californiadreamshostel.officetv.UNIT.UNITS;

public class C extends Unit {

    public static final String TYPE = "C";

    {
        UNIT_TYPE = TYPE;
    }

    public C(){

    }

    public C(final double value){
        super(value);
    }

    @Override
    public String getStringRepresentation() {
        return "\u00b0";
    }
}
