package com.californiadreamshostel.officetv.UNIT.UNITS;

public abstract class Unit implements UnitType {

    protected String UNIT_TYPE = "NA";
    protected double value;

    public Unit(){
        this(-1D);
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

    @Override
    public String getType() {
        return UNIT_TYPE;
    }

    public String getStringRepresentation(){
        return UNIT_TYPE;
    }

}
