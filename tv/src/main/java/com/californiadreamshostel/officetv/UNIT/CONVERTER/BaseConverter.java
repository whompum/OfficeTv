package com.californiadreamshostel.officetv.UNIT.CONVERTER;

import android.util.Log;

public abstract class BaseConverter implements Convertor {


   protected double from = -0.9999999D;
   protected double to = -0.9999999D;

   protected ConversionAlgorithm algorithm;

   public BaseConverter(){
       this.algorithm = getConversionAlgorithm();
   }

    @Override
    public double getResult() {

       if( (from == -0.9999999D) | (to == -0.9999999D)) {
           Log.w("UNIT_CONVERSION", "Calling getResult() before bind()");
           return -0.9999999D;
       }

        return algorithm.convert(from, to);
    }

    @Override
    public Convertor bind(double from, double to) {

       this.from = from;
       this.to = to;

        return this;
    }

    protected abstract ConversionAlgorithm getConversionAlgorithm();


}
