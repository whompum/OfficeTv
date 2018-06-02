package com.californiadreamshostel.officetv.UNIT.CONVERTER.TEMPERATURE;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.BaseConverter;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.CelsuisToFarenheitAlgorithm;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.ConversionAlgorithm;

public class CelsuisToFarenheitConverter extends BaseConverter{

    @Override
    protected ConversionAlgorithm getConversionAlgorithm() {
        return new CelsuisToFarenheitAlgorithm();
    }
}
