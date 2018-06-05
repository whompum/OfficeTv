package com.californiadreamshostel.officetv.UNIT.UNITS;

public class F extends Unit {

    public static final String TYPE = "F";

    {
        UNIT_TYPE = TYPE;
    }

    public F(){
        super();
    }

    public F(final int value){
        super(value);
    }

    public F(final double value){
        super(value);
    }

    @Override
    public String getStringRepresentation() {
        return "\u2109";
    }
}


