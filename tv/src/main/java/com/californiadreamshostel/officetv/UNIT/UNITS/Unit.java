package com.californiadreamshostel.officetv.UNIT.UNITS;

public abstract class Unit implements UnitType {

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

    @Override
    public boolean equals(Object obj) {
        return getType().equals( ((Unit)obj).getType() );
    }
}
