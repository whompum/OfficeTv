package com.californiadreamshostel.officetv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.californiadreamshostel.officetv.MODELS.ContactInformation.ContactInformation;
import com.californiadreamshostel.officetv.MODELS.RentalInformation.RentalInformation;
import com.californiadreamshostel.officetv.NETWORK.JsonDownloadPackage;
import com.californiadreamshostel.officetv.NETWORK.JsonDownloaderTask;
import com.californiadreamshostel.officetv.NETWORK.JsonResultListener;
import com.californiadreamshostel.officetv.NETWORK.JsonTimerTask;


/**
 * Should also connect to some kind of back-end to allow dynamic changing of data values
 * */
public class ShelfFragment extends Fragment {


     public static Fragment newInstance(@Nullable final Bundle args){

        final Fragment frag = new ShelfFragment();

        if(args != null)
            frag.setArguments(args);

    return frag;
    }

    public static final int LAYOUT = R.layout.layout_shelf;


    //Contact Information
    private ContactInformation contactInformation;

    //Contact Information TextViews
    private RTV locationDisplay, phoneNumberDisplay, emailDisplay,
                socialMediaDisplayOne, socialMediaDisplayTwo, socialMediaDisplayThree;


    //Weather Information
    private RTV todayWeatherDisplay, tomorrowWeatherLabel, tomorrowWeatherDisplay,
                tomorrowTomorrowWeatherLabel, tomorrowTomorrowWeatherDisplay,
                tomorrowTomorrowTomorrowWeatherLabel, tomorrowTomorrowTomorrowWeatherDisplay;

    //Weather Condition Image
    private ImageView todayWeatherImage;

    //Surf Information
    private RTV waveHeight, waterTemp, hightTide, windSpeed;

    //RentalInformation
    private RentalInformation rentalInformation;


    private JsonTimerTask weatherTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View content = inflater.inflate(LAYOUT, container, false);

    return content;
    }
    /**
     * HELP METHODS TO BIND EACH VIEW BY CATEGORY.
     */

    @Override
    public void onDetach() {
        super.onDetach();
        if(weatherTask != null)
            weatherTask.cancel();
    }
}











