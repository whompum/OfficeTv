package com.californiadreamshostel.officetv.Unit.CONVERTER.CONVERSIONALGORITHMS.SIZE;

import com.californiadreamshostel.officetv.Unit.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;

public class M$Ft implements ConversionAlgorithm {

    public static final double FACTOR = 3.28084D;

    @Override
    public double convert(double from) {
        return from*FACTOR;
    }
}
