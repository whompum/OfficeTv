package com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.VELOCITY;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;

public class Kph$Mph implements ConversionAlgorithm {

    public static final double FACTOR = 1.60934D;

    @Override
    public double convert(double from) {
        return from / FACTOR;
    }
}
