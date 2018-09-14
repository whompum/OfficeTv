package com.californiadreamshostel.officetv.Controllers;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.californiadreamshostel.officetv.Controllers.TvSlides.Slide;
import com.californiadreamshostel.officetv.Controllers.TvSlides.SlideFragment;
import com.californiadreamshostel.officetv.Controllers.WeatherSurf.WeatherSurfController;
import com.californiadreamshostel.officetv.R;
import com.californiadreamshostel.officetv.Controllers.TvSlides.Exchanger;
import com.californiadreamshostel.officetv.Controllers.TvSlides.OnExchangeListener;

/******
 *MainActivity class that loads {@link RentalSlideFragment} and {@link ShelfFragment}.
 ******/
public class MainActivity extends Activity implements OnExchangeListener {

    private Exchanger exchanger;

    private SlideFragment current;

    private RentalSlideFragment rentalSlideFragment;
    private SurfLessonSlideFragmentFragment surfLessons;

    private RentalsDataController rentalsController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.display_root_wireframe);

        getFragmentManager().beginTransaction()
                .add(R.id.id_shelf_container, ShelfFragment.newInstance(null)).commit();

        rentalSlideFragment = (RentalSlideFragment) RentalSlideFragment.newInstance();

        surfLessons = (SurfLessonSlideFragmentFragment) SurfLessonSlideFragmentFragment.newInstance();

        //Initialize the Weather Surf job service
        WeatherSurfController.start(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.exchanger = Exchanger.obtain(this);
        this.rentalsController = RentalsDataController.obtain();

        final Slide rentalSlide = new Slide(rentalSlideFragment.getDefaultTitle());
        rentalSlide.setDuration(15000L); //Real durations resolved from remote server.

        final Slide surfSlide = new Slide(surfLessons.getDefaultTitle());
        surfSlide.setDuration(15000L);

        //Run @ default timing / positioning's.
        this.exchanger.addSubject(rentalSlide);
        this.exchanger.addSubject(surfSlide);

        rentalsController.register();
        exchanger.start();
    }

    @Override
    public boolean onExchange(@NonNull Slide s, @Nullable final Slide oldSlide) {

        if(s.getReference().equals(rentalSlideFragment.getDefaultTitle()))
            current = rentalSlideFragment;

        if(s.getReference().equals(surfLessons.getDefaultTitle()))
            current = surfLessons;

        if(current != null){
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.id_slides_container, current)
                    .commit();

            ((TextView)findViewById(R.id.local_slide_title))
                    .setText(current.getTitle());

        }

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.exchanger.stop();
        rentalsController.unregister();
    }

}
