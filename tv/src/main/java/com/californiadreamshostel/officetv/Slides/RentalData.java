package com.californiadreamshostel.officetv.Slides;

import android.support.annotation.NonNull;

public class RentalData {

    private String title;
    private long threeHourCost;
    private long fullDayCost;

    public RentalData(@NonNull final String title,
                      final long threeHourCost,
                      final long fullDayCost){

        this.title = title;
        this.threeHourCost = threeHourCost;
        this.fullDayCost = fullDayCost;
    }

    public String getTitle() {
        return title;
    }

    public long getThreeHourCost() {
        return threeHourCost;
    }

    public long getFullDayCost() {
        return fullDayCost;
    }
}
