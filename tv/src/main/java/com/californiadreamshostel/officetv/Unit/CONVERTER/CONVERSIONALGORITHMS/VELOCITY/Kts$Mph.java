package com.californiadreamshostel.officetv.Unit.CONVERTER.CONVERSIONALGORITHMS.VELOCITY;

import com.californiadreamshostel.officetv.Unit.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;

public class Kts$Mph implements ConversionAlgorithm {

    //Knots to Mph factor
    public static final double FACTOR = 1.15078D;

    @Override
    public double convert(double from) {
        return from * FACTOR;
    }
}
