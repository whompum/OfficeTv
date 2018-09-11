package com.californiadreamshostel.officetv.Unit.UNITS.VELOCITY;

import com.californiadreamshostel.officetv.Unit.UNITS.Unit;

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
