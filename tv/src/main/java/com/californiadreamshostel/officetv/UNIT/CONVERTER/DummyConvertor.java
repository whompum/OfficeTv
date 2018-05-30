package com.californiadreamshostel.officetv.UNIT.CONVERTER;

public class DummyConvertor extends BaseConverter {

    @Override
    protected ConversionAlgorithm getConversionAlgorithm() {
        return new DummyAlgo();
    }

    private static class DummyAlgo implements ConversionAlgorithm{
        @Override
        public double convert(double from, double to) {
            return -0.99999999D;
        }
    }
}
