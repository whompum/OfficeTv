package com.californiadreamshostel.officetv.UNIT.CONVERTER;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;

public class DummyConvertor extends BaseConverter {

    @Override
    protected ConversionAlgorithm getConversionAlgorithm() {
        return new DummyAlgo();
    }

    private static class DummyAlgo implements ConversionAlgorithm{
        @Override
        public double convert(double from) {
            return -0.99999999D;
        }
    }
}
