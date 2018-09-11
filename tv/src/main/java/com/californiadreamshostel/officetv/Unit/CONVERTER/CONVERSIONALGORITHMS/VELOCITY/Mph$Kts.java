package com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.VELOCITY;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;

public class Mph$Kts implements ConversionAlgorithm {

    //Conversion Factor for Miles to Knots
    public static final double FACTOR = 1.15078D;

    @Override
    public double convert(double from) {
        return from / FACTOR;
    }
}
