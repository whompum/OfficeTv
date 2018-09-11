package com.californiadreamshostel.officetv.Unit.UNITS;

public abstract class Unit{

    protected String UNIT_TYPE = "NA";
    protected double value;

    public Unit(){
        this(-1D);
    }

    public Unit(final int value){
        this(Double.valueOf(value));
    }

    public Unit(final double value){
        this.value = value;
    }

    public double value(){
        return value;
    }

    public void setValue(final double value){
        this.value = value;
    }

    public String getType() {
        return UNIT_TYPE;
    }

    public String getStringRepresentation(){
        return UNIT_TYPE;
    }

}
