package com.californiadreamshostel.officetv.SURF.MODELS;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class WindAndSwell {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long localTimestamp;

    @Embedded(prefix = "swell_")
    private Swell swell;

    @Embedded(prefix = "wind_")
    private Wind wind;

    public long getLocalTimestamp() {
        return localTimestamp;
    }

    public Swell getSwell() {
        return swell;
    }

    public Wind getWind() {
        return wind;
    }

    public int getId(){
        return id;
    }

    public void setLocalTimestamp(long localTimestamp) {
        this.localTimestamp = localTimestamp;
    }

    public void setSwell(Swell swell) {
        this.swell = swell;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setId(final int id){
        this.id = id;
    }



    public static class Swell{

        private double minBreakingHeight;
        private double maxBreakingHeight;

        public double getMinBreakingHeight() {
            return minBreakingHeight;
        }

        public double getMaxBreakingHeight() {
            return maxBreakingHeight;
        }

        public void setMinBreakingHeight(double minBreakingHeight) {
            this.minBreakingHeight = minBreakingHeight;
        }

        public void setMaxBreakingHeight(double maxBreakingHeight) {
            this.maxBreakingHeight = maxBreakingHeight;
        }
    }

    public static class Wind{
        private double speed;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }
    }

}
