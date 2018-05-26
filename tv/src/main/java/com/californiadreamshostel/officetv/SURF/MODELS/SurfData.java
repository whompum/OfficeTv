package com.californiadreamshostel.officetv.SURF.MODELS;

import android.support.annotation.NonNull;

public class SurfData {

    private HighTide highTide;
    private WaterTemperature temperature;
    private WindSwellHeight windSwellHeight;

    private SurfData(final HighTide highTide, WaterTemperature waterTemperature, WindSwellHeight swellHeight){
        this.highTide = highTide;
        this.temperature = waterTemperature;
        this.windSwellHeight = swellHeight;
    }

    //Returns units in IMPERIAL
    public int getWaterTemperature(){
        return temperature.getTemperature();
    }

    public int getWindSpeed(){
        return windSwellHeight.getWind().getWindSpeed();
    }

    public int getWindSwellHeight(){
        return windSwellHeight.getSwell().getMinBreakingHeight();
    }

    public int getWindSwellHeightMax(){return windSwellHeight.getSwell().getMaxBreakingHeight();}

    public String getHighTideTime(){
        return highTide.getHour();
    }

    public static class Builder{

        private SurfData data;

        private HighTide highTide;
        private WaterTemperature waterTemperature;
        private WindSwellHeight swellHeight;

        public Builder setTideInfo(@NonNull final HighTide highTide){
            this.highTide = highTide;
            return this;
        }


        public Builder setWaterInfo(@NonNull final WaterTemperature waterTemperature){
            this.waterTemperature = waterTemperature;
            return this;
        }

        public Builder setSwellHeight(@NonNull final WindSwellHeight swellHeight){
            this.swellHeight = swellHeight;
            return this;
        }

        public boolean isReady(){
            return highTide != null && waterTemperature != null && swellHeight != null;
        }

        public SurfData create(){
            return new SurfData(highTide, waterTemperature, swellHeight);
        }

    }

}













