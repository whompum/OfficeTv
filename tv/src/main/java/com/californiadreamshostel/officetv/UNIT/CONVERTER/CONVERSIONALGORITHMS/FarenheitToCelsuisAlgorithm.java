package com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS;

import android.util.Log;

public class FarenheitToCelsuisAlgorithm implements ConversionAlgorithm {

    public static final double BASE_SUBTRACT = 32D;

    public static final double FACTOR = 0.5556D;

    @Override
    public double convert(double from) {
        Log.i("CONVERTING", "Farenheit to Celsuis from: " + from);
        return toCelsius(from);
    }

    private double toCelsius(final double from){
        return (from-BASE_SUBTRACT) * FACTOR;
    }

}
