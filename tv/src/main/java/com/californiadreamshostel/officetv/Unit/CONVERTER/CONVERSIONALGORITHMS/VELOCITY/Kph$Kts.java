package com.californiadreamshostel.officetv.Unit.CONVERTER.CONVERSIONALGORITHMS.VELOCITY;

import com.californiadreamshostel.officetv.Unit.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;

public class Kph$Kts implements ConversionAlgorithm{

    public static final double FACTOR = 1.852D;

    @Override
    public double convert(double from) {
        return from/FACTOR;
    }
}
