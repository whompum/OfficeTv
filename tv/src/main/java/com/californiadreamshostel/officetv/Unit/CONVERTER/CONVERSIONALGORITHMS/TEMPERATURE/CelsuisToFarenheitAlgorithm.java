package com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.TEMPERATURE;

import android.util.Log;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;

public class CelsuisToFarenheitAlgorithm implements ConversionAlgorithm {

    public static final double BASE_SUBTRACT = 32D;

    public static final double FACTOR = 0.5556D;

    @Override
    public double convert(double from) {
        return toFarenheit(from);
    }

    private double toFarenheit(final double from){

        Log.i("CONVERTING", "Celsuis to Farenheit: " + from);

        return (from / FACTOR) + BASE_SUBTRACT;
    }

}
