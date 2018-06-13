package com.californiadreamshostel.officetv.CONTROLLERS;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.californiadreamshostel.officetv.R;
import com.californiadreamshostel.officetv.SLIDES.RentalData;
import com.californiadreamshostel.officetv.VIEWS.RTV;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RentalSlideFragment extends Fragment {

    @LayoutRes
    public static final int LAYOUT = R.layout.slides_base_rental;


    public static Fragment newInstance(){
        return new RentalSlideFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View content = inflater.inflate(LAYOUT, container, false);


        final Iterator<RentalData> dataIterator = TEMP_RENTAL_DATA().iterator();

       final LinearLayout contentContainer = content.findViewById(R.id.rental_slide_content_container);

        contentContainer.setWeightSum(TEMP_RENTAL_DATA().size());

        while(dataIterator.hasNext()){

            final RentalData data = dataIterator.next();

            final View itemView = inflater.inflate(R.layout.slides_rental_content_item, null);

            contentContainer.addView(itemView);

            itemView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0,
                    1
            ));


            ((RTV)itemView.findViewById(R.id.rental_slide_title)).setText(data.getTitle());


            final String threeHourPrice = (data.getThreeHourCost()>0) ?"$"+String.valueOf(data.getThreeHourCost())
                    : "";

            final String fullDayPrice = "$"+String.valueOf(data.getFullDayCost());

            ((RTV)itemView.findViewById(R.id.rental_slide_three_hour_price)).setText(threeHourPrice);
            ((RTV)itemView.findViewById(R.id.rental_slide_full_day_price)).setText(fullDayPrice);
        }

        return content;
    }

    private List<RentalData> TEMP_RENTAL_DATA(){

        final List<RentalData> data = new ArrayList<>(6);

        data.add(new RentalData("Surfboard (foam)", 15L, 25L));
        data.add(new RentalData("Surfboard (fiber)", 20L, 25L));
        data.add(new RentalData("Wetsuit", 15L, 20L));
        data.add(new RentalData("Bikes", 8L, 12L));
        data.add(new RentalData("Beach Towel", 0L, 5L));
        data.add(new RentalData("Beach Chair", 0L, 5L));
        data.add(new RentalData("Umbrella", 0L, 5L));

        return data;
    }

}
