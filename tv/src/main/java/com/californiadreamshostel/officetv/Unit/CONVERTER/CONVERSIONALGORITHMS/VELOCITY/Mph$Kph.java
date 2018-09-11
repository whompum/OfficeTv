package com.californiadreamshostel.officetv.Unit.CONVERTER.CONVERSIONALGORITHMS.VELOCITY;

import com.californiadreamshostel.officetv.Unit.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;

public class Mph$Kph implements ConversionAlgorithm{

    public static final double FACTOR = 1.60934D;

    @Override
    public double convert(double from) {
        return from*FACTOR;
    }
}
