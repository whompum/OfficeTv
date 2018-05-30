package com.californiadreamshostel.officetv.UNIT.CONVERTER.TEMPERATURE;

import com.californiadreamshostel.officetv.UNIT.CONVERTER.BaseConverter;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.CONVERSIONALGORITHMS.FarenheitToCelsuisAlgorithm;
import com.californiadreamshostel.officetv.UNIT.CONVERTER.ConversionAlgorithm;

public class FarenheitToCelsuisConverter extends BaseConverter {

    @Override
    protected ConversionAlgorithm getConversionAlgorithm() {
        return new FarenheitToCelsuisAlgorithm();
    }

}
