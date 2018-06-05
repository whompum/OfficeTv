package com.californiadreamshostel.officetv.UNIT.CONVERTER;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.SIZE.Ft$M;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.SIZE.M$Ft;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.TEMPERATURE.CelsuisToFarenheitAlgorithm;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.TEMPERATURE.FarenheitToCelsuisAlgorithm;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.VELOCITY.Kph$Kts;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.VELOCITY.Kph$Mph;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.VELOCITY.Kts$Kph;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.VELOCITY.Kts$Mph;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.VELOCITY.Mph$Kph;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.VELOCITY.Mph$Kts;
import com.californiadreamshostel.officetv.UNIT.UNITS.TEMPERATURE.C;
import com.californiadreamshostel.officetv.UNIT.UNITS.TEMPERATURE.F;
import com.californiadreamshostel.officetv.UNIT.UNITS.SIZE.Ft;
import com.californiadreamshostel.officetv.UNIT.UNITS.SIZE.Km;
import com.californiadreamshostel.officetv.UNIT.UNITS.VELOCITY.Kph;
import com.californiadreamshostel.officetv.UNIT.UNITS.VELOCITY.Kts;
import com.californiadreamshostel.officetv.UNIT.UNITS.SIZE.M;
import com.californiadreamshostel.officetv.UNIT.UNITS.VELOCITY.Mph;
import com.californiadreamshostel.officetv.UNIT.UNITS.Unit;

import java.util.HashSet;
import java.util.Set;

//Converts a unit from one type to another. Also holds on to the set of units
public class UnitConvertorFactory {

    //Mirrored Sets of ConversionType
    public static final Set<String> MASS = new HashSet<>(3);
    public static final Set<String> VELOCITY = new HashSet<>(3);
    public static final Set<String> TEMPERATURE = new HashSet<>(2);

    static{
        MASS.add(Ft.TYPE);
        MASS.add(M.TYPE);
        MASS.add(Km.TYPE);

        VELOCITY.add(Kph.TYPE);
        VELOCITY.add(Mph.TYPE);
        VELOCITY.add(Kts.TYPE);

        TEMPERATURE.add(F.TYPE);
        TEMPERATURE.add(C.TYPE);
    }

    @Deprecated
    public static double convert(@NonNull final Unit from, @NonNull final Unit to){

        if( !(isTemperature(from.getType()) & isTemperature(to.getType())) ) //If isn't  Temper
            if( !(isMass(from.getType()) & isMass(to.getType())) ) //Check if isn't Distance
                if( !(isVelocity(from.getType()) & isVelocity(to.getType())) ) //If isn't Distance, or speed, or temp, return bad number
                    return -2D;

        final String classFrom = getUnitType(from);
        final String classTo = getUnitType(to);

        //Are the same unit type, so nothing to convert.
        if(classFrom.equals(classTo))
            return from.value();

    return convert(from.getType(), to.getType(), from.value());
    }

    public static double convert(@NonNull final String from, @NonNull final String to, final double value){
        if( !(isTemperature(from) & isTemperature(to)) ) //If isn't  Temper
            if( !(isMass(from) & isMass(to)) ) //Check if isn't Distance
                if( !(isVelocity(from) & isVelocity(to)) ) //If isn't Distance, or speed, or temp, return bad number
                    return -2D;

        if(from.equals(to))
            return value;

        final ConversionAlgorithm algorithm = resolveConversionAlgorithm(from, to);

        if(algorithm == null)
            return Double.MIN_VALUE;

        return new UnitConverter(algorithm).convert(value);
    }


    private static boolean isVelocity(@NonNull String type){
        return VELOCITY.contains(type);
    }

    private static boolean isMass(@NonNull String type){
        return MASS.contains(type);
    }

    private static boolean isTemperature(@NonNull String type){
        return TEMPERATURE.contains(type);
    }

    private static String getUnitType(@NonNull final Unit unit){
        return unit.getType();
    }


    //Resolves the Type of convertor we want
    @Nullable
    private static ConversionAlgorithm resolveConversionAlgorithm(@NonNull final String from,
                                                                  @NonNull final String to){

        ConversionAlgorithm algo = null;

        if(isTemperature(from))
            algo = resolveTemperatureAlgorithm(from, to);
        else if(isMass(from))
            algo = resolveMassAlgorithm(from, to);
        else if(isVelocity(from))
            algo = resolveVelocityAlgorithm(from, to);

        return algo;
    }

    @Nullable
    private static ConversionAlgorithm resolveTemperatureAlgorithm(@NonNull final String from,
                                                                   @NonNull final String to){
        if(from.equals(F.TYPE) && to.equals(C.TYPE))
            return new FarenheitToCelsuisAlgorithm();

        if(from.equals(C.TYPE) && to.equals(F.TYPE))
            return new CelsuisToFarenheitAlgorithm();

        return null;
    }

    @Nullable
    private static ConversionAlgorithm resolveMassAlgorithm(@NonNull final String from,
                                                            @NonNull final String to){

        ConversionAlgorithm algo = null;


        if(from.equals(Ft.TYPE)) {
            if (to.equals(M.TYPE))
                algo = new Ft$M();
        }

        if(from.equals(M.TYPE))
            if(to.equals(Ft.TYPE))
                algo = new M$Ft();


     return algo;
    }

    @Nullable
    private static ConversionAlgorithm resolveVelocityAlgorithm(@NonNull final String from,
                                                                @NonNull final String to){

        ConversionAlgorithm algo = null;

        if(from.equals(Kts.TYPE)) {
            if (to.equals(Mph.TYPE))
                algo = new Kts$Mph();
            else if (to.equals(Kph.TYPE))
                algo = new Kts$Kph();
        }

        if(from.equals(Mph.TYPE)) {
            if (to.equals(Kts.TYPE))
                algo = new Mph$Kts();
            else if (to.equals(Kph.TYPE))
                algo = new Mph$Kph();
        }

        if(from.equals(Kph.TYPE)) {
            if (to.equals(Mph.TYPE))
                algo = new Kph$Mph();
            else if (to.equals(Kts.TYPE))
                algo = new Kph$Kts();
        }

        return algo;
    }

}






