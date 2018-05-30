package com.californiadreamshostel.officetv.UNIT.CONVERTER;


import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.Convertor;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.TEMPERATURE.CelsuisToFarenheitConverter;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.TEMPERATURE.FarenheitToCelsuisConverter;
import com.californiadreamshostel.officetv.UNIT.UNITS.C;
import com.californiadreamshostel.officetv.UNIT.UNITS.F;
import com.californiadreamshostel.officetv.UNIT.UNITS.Ft;
import com.californiadreamshostel.officetv.UNIT.UNITS.Km;
import com.californiadreamshostel.officetv.UNIT.UNITS.Kph;
import com.californiadreamshostel.officetv.UNIT.UNITS.Kts;
import com.californiadreamshostel.officetv.UNIT.UNITS.M;
import com.californiadreamshostel.officetv.UNIT.UNITS.Mph;
import com.californiadreamshostel.officetv.UNIT.UNITS.Unit;

import java.util.HashSet;
import java.util.Set;

//Converts a unit from one type to another. Also holds on to the set of units
public class UnitConvertorFactory {

    public static final Set<Unit> SIZE = new HashSet<>(3);
    public static final Set<Unit> SPEED = new HashSet<>(3);
    public static final Set<Unit> TEMPERATURE = new HashSet(3);

    private static final double FEET_TO_METERS = 0.0D;
    private static final double METERS_TO_FEET = 0.0D;
    private static final double KILOMETERS_TO_FEET = 0.0D;
    private static final double FEET_TO_KILOMETERS = 0.0D;
    private static final double KILOMETERS_TO_METERS = 0.0D;
    private static final double METERS_TO_KILOMETERS = 0.0D;

    private static final double KPH_TO_KTS = 0.0D;
    private static final double KTS_TO_KPH = 0.0D;
    private static final double MPH_TO_KTS = 0.0D;
    private static final double KTS_TO_MPH = 0.0D;
    private static final double MPH_TO_KPH = 0.0D;
    private static final double KPH_TO_MPH = 0.0D;

    private static final double FARENHEIT_TO_CELSIUS = 0.0d;
    private static final double CELSIUS_TO_FARENHEIT = 0.0d;


    static{
        SIZE.add(new Ft());
        SIZE.add(new M());
        SIZE.add(new Km());

        SPEED.add(new Kph());
        SPEED.add(new Mph());
        SPEED.add(new Kts());
    }

    public static double convert(@NonNull final Unit from, @NonNull final Unit to){

        if( !(isTemperature(from) & isTemperature(to)) ) //If isn't  Temper
            if( !(isDistance(from) & isDistance(to)) ) //Check if isn't Distance
                if( !(isSpeed(from) & isSpeed(to)) ) //If isn't Distance, or speed, or temp, return bad number
                    return -0.09999999D;

        final Class classFrom = getUnitType(from);
        final Class classTo = getUnitType(to);

        //Are the same unit type, so nothing to convert.
        if(classFrom.equals(classTo))
            return from.value();

    return resolveConverter(classFrom, classTo).bind(from.value(), to.value()).getResult();
    }


    private static boolean isSpeed(@NonNull Unit unit){
        return SPEED.contains(unit);
    }

    private static boolean isDistance(@NonNull final Unit unit){
        return SIZE.contains(unit);
    }

    private static boolean isTemperature(@NonNull final Unit unit){
        return TEMPERATURE.contains(unit);
    }

    private static Class getUnitType(@NonNull final Unit unit){
        return unit.getClass();
    }


    //Resolves the Type of convertor we want
    private static Convertor resolveConverter(@NonNull final Class from, @NonNull final Class to){

        if(from == F.class && to == C.class)
            return new FarenheitToCelsuisConverter();

        if(from == C.class && to == F.class)
            return new CelsuisToFarenheitConverter();

        return new DummyConvertor();
    }

}






