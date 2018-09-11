package com.californiadreamshostel.officetv.Unit.CONVERTER.CONVERSIONALGORITHMS.SIZE;

import com.californiadreamshostel.officetv.Unit.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;

public class Ft$M implements ConversionAlgorithm{

    //Factor of feet to meters
    public static final double FACTOR = 0.3048D;

    @Override
    public double convert(double from) {
        return from*FACTOR;
    }
}
