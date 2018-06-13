package com.californiadreamshostel.officetv.SURF.MODELS;


public class WindSwellHeight {

    private long localTimestamp = 0L;
    private Swell swell = new Swell(); //Default Values
    private Wind wind = new Wind(); //Default Values

    public Wind getWind(){
        return wind;
    }

    public Swell getSwell(){
        return swell;
    }




    public long getLocalTimestamp(){
        return localTimestamp;
    }

    public class Swell{
        private int minBreakingHeight = 0;
        private int maxBreakingHeight = 0;

        public int getMinBreakingHeight(){
            return minBreakingHeight;
        }

        public int getMaxBreakingHeight(){
            return maxBreakingHeight;
        }

    }

    public class Wind{
        private int speed = 0;

        public int getWindSpeed(){
            return speed;
        }
    }

}
