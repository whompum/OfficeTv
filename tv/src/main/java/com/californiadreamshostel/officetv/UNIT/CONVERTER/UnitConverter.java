package com.californiadreamshostel.officetv.UNIT.CONVERTER;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;

public abstract class BaseConverter implements Convertor {


   protected double from = -1D;

   protected ConversionAlgorithm algorithm;

   public BaseConverter(){
       this.algorithm = getConversionAlgorithm();
   }

    @Override
    public double getResult() {
       return algorithm.convert(from);
    }

    @Override
    public Convertor bind(double from) {
       this.from = from;
       return this;
    }

    protected abstract ConversionAlgorithm getConversionAlgorithm();


}
