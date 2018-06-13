package com.californiadreamshostel.officetv.SURF.MODELS;

public final class HighTide {

    //Returns a Textual-Based string representation of the Current Day
    private String date = "";
    //Returns the Hour i can expect the tide. Either x[AM] | x[PM]
    private String hour = "N/A";
    private double tide = 0.0D;



    public String getHour(){
        return hour;
    }

    public double getTide(){
        return tide;
    }

}
