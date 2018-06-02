package com.californiadreamshostel.officetv.UNIT.CONVERTER;


import android.support.annotation.NonNull;

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

    public static final Set<String> DISTANCE = new HashSet<>(3);
    public static final Set<String> SPEED = new HashSet<>(3);
    public static final Set<String> TEMPERATURE = new HashSet(2);

    static{
        DISTANCE.add(Ft.TYPE);
        DISTANCE.add(M.TYPE);
        DISTANCE.add(Km.TYPE);

        SPEED.add(Kph.TYPE);
        SPEED.add(Mph.TYPE);
        SPEED.add(Kts.TYPE);

        TEMPERATURE.add(F.TYPE);
        TEMPERATURE.add(C.TYPE);
    }

    public static double convert(@NonNull final Unit from, @NonNull final Unit to){

        if( !(isTemperature(from) & isTemperature(to)) ) //If isn't  Temper
            if( !(isDistance(from) & isDistance(to)) ) //Check if isn't Distance
                if( !(isSpeed(from) & isSpeed(to)) ) //If isn't Distance, or speed, or temp, return bad number
                    return -2D;

        final String classFrom = getUnitType(from);
        final String classTo = getUnitType(to);

        //Are the same unit type, so nothing to convert.
        if(classFrom.equals(classTo))
            return -100.0D;

    return resolveConverter(classFrom, classTo).bind(from.value()).getResult();
    }


    private static boolean isSpeed(@NonNull Unit unit){
        return SPEED.contains(unit.getType());
    }

    private static boolean isDistance(@NonNull final Unit unit){
        return DISTANCE.contains(unit.getType());
    }

    private static boolean isTemperature(@NonNull final Unit unit){
        return TEMPERATURE.contains(unit.getType());
    }

    private static String getUnitType(@NonNull final Unit unit){
        return unit.getType();
    }


    //Resolves the Type of convertor we want
    private static Convertor resolveConverter(@NonNull final String from, @NonNull final String to){

        if(from.equals(new F().getType()) && to.equals(new C().getType()))
            return new FarenheitToCelsuisConverter();

        if(from.equals(new C().getType()) && to.equals(new F().getType()))
            return new CelsuisToFarenheitConverter();

        return new DummyConvertor();
    }

}






