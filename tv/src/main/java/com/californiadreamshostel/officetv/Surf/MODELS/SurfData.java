package com.californiadreamshostel.officetv.Surf.MODELS;

import android.support.annotation.NonNull;

public class SurfData {

    private Tide tide;
    private WaterTemperature temperature;
    private WindAndSwell windAndSwell;

    private SurfData(final Tide tide, WaterTemperature waterTemperature, WindAndSwell swellHeight){
        this.tide = tide;
        this.temperature = waterTemperature;
        this.windAndSwell = swellHeight;
    }

    //Returns units in IMPERIAL
    public int getWaterTemperature(){
        return (int)temperature.getFahrenheit();
    }

    public int getWindSpeed(){
        return (int)windAndSwell.getWind().getSpeed();
    }

    public int getWindAndSwell(){
        return (int)windAndSwell.getSwell().getMinBreakingHeight();
    }

    public int getWindSwellHeightMax(){return (int)windAndSwell.getSwell().getMaxBreakingHeight();}

    public String getHighTideTime(){
        return tide.getHour();
    }

    public static class Builder{

        private SurfData data;

        private Tide tide;
        private WaterTemperature waterTemperature;
        private WindAndSwell swellHeight;

        public Builder setTideInfo(@NonNull final Tide tide){
            this.tide = tide;
            return this;
        }


        public Builder setWaterInfo(@NonNull final WaterTemperature waterTemperature){
            this.waterTemperature = waterTemperature;
            return this;
        }

        public Builder setSwellHeight(@NonNull final WindAndSwell swellHeight){
            this.swellHeight = swellHeight;
            return this;
        }

        public boolean isReady(){
            return tide != null && waterTemperature != null && swellHeight != null;
        }

        public SurfData create(){
            return new SurfData(tide, waterTemperature, swellHeight);
        }

    }

}













