package com.californiadreamshostel.officetv.UNIT.CONVERTER;

import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;

public class UnitConverter{

   private ConversionAlgorithm algorithm;

   public UnitConverter(@NonNull final ConversionAlgorithm algorithm){
       this.algorithm = algorithm;
   }

   public double convert(final double value){
       return algorithm.convert(value);
   }


}
