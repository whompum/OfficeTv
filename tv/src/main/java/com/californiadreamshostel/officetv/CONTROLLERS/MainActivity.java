package com.californiadreamshostel.officetv.CONTROLLERS;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.californiadreamshostel.officetv.CONTROLLERS.weather$surf.WeatherSurfController;
import com.californiadreamshostel.officetv.R;
import com.californiadreamshostel.officetv.Slides.Exchange.Exchanger;
import com.californiadreamshostel.officetv.Slides.Exchange.OnExchangeListener;

/******
 *MainActivity class that loads {@link RentalSlideFragment} and {@link ShelfFragment}.
 ******/
public class MainActivity extends Activity implements OnExchangeListener {

    private Exchanger exchanger;

    private Slide current;

    private Slide rentals;
    private Slide surfLessons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_root_wireframe);

        getFragmentManager().beginTransaction()
                .add(R.id.id_shelf_container, ShelfFragment.newInstance(null)).commit();

        rentals = (RentalSlideFragment) RentalSlideFragment.newInstance();
        surfLessons = (SurfLessonSlideFragment) SurfLessonSlideFragment.newInstance();

        //Initialize the Weather Surf job service
        WeatherSurfController.start(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        this.exchanger = Exchanger.obtain(this);

        this.exchanger.addSubject(new Exchanger.Subject(RentalSlideFragment.DEFAULT_TITLE, 15000));
        this.exchanger.addSubject(new Exchanger.Subject(SurfLessonSlideFragment.DEFAULT_TITLE, 15000));

        exchanger.start();
    }

    @Override
    public boolean onExchange(@NonNull String newId, int newPos) {

        if(newId.equals(RentalSlideFragment.DEFAULT_TITLE))
            current = rentals;

        if(newId.equals(SurfLessonSlideFragment.DEFAULT_TITLE))
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
    }

}
