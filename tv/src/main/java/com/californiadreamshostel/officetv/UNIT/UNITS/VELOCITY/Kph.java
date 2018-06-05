package com.californiadreamshostel.officetv.UNIT.UNITS.VELOCITY;

import com.californiadreamshostel.officetv.UNIT.UNITS.Unit;

public class Kph extends Unit {
    public static final String TYPE = "KPH";
    {
        UNIT_TYPE = TYPE;
    }

    public Kph(){
        super();
    }

    public Kph(final int value){
        super(value);
    }

    public Kph(final double value){
        super(value);
    }

}
