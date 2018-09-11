package com.californiadreamshostel.officetv.Controllers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.californiadreamshostel.firebaseclient.DataSchema;
import com.californiadreamshostel.firebaseclient.ReferenceItem;
import com.californiadreamshostel.firebaseclient.SimpleEventListener;
import com.californiadreamshostel.officetv.TvSlides.SlideFragment;
import com.californiadreamshostel.officetv.R;

import java.util.List;


public class RentalSlideFragmentFragment extends SlideFragment implements RemoteDataReceiver{

    public static final String DEFAULT_TITLE = "RentalsSlideFragment.class";

    @LayoutRes
    public static final int LAYOUT = R.layout.slide_rental_content;

    public static Fragment newInstance(){
        return new RentalSlideFragmentFragment();
    }

    private RentalsDataController controller;

    private LinearLayout contentContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = RentalsDataController.obtain();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View content = inflater.inflate(LAYOUT, container, false);

        contentContainer = content.findViewById(R.id.rental_slide_content_container);

        return content;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final LayoutInflater inflater = LayoutInflater.from(getActivity());

        final List<ReferenceItem> data = controller.fetchData();

        contentContainer.setWeightSum(data.size());

        for(ReferenceItem item: data){

            final View viewItem = inflateItemView(inflater);

            populateItemView(viewItem, item);

            viewItem.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0,
                    1
            ));

            contentContainer.addView(viewItem);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        controller.subscribe(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        controller.unsubscribe();
    }

    //Should always be called during onStart
    @Override
    public void onDataReceived(ReferenceItem item, int changeType) {

        if(changeType == SimpleEventListener.ADDED){

            final View viewItem = inflateItemView(LayoutInflater.from(getActivity()));

            populateItemView(viewItem, item);

            viewItem.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0,
                    1
            ));

            contentContainer.addView(viewItem);
        }

        if(changeType == SimpleEventListener.REMOVED){
            final View v = findDataViewItem(item.getReference());
            if(v != null) contentContainer.removeView(v);
        }

        if(changeType == SimpleEventListener.CHANGE) {
            populateItemView(findDataViewItem(item.getReference()), item);
        }
    }

    private View findDataViewItem(String reference){

        View itemView = null;

        for(int a = 0; a < contentContainer.getChildCount(); a++)
            if( (itemView = contentContainer.getChildAt(a).findViewById(R.id.rental_slide_title)) != null )
                if( ((TextView)itemView).getText().toString().equals(reference)) {
                    itemView = contentContainer.getChildAt(a);
                    break;
                }else itemView = null;

        return itemView;
    }

    private View inflateItemView(LayoutInflater inflater){
        return inflater.inflate(R.layout.slides_rental_content_item, null);
    }

    private void populateItemView(View itemView, ReferenceItem item){

        if(itemView == null || item == null) return;

        ((TextView)itemView.findViewById(R.id.rental_slide_title))
                .setText(item.getReference());

        ReferenceItem threeHourChild;
        ReferenceItem fullDayChild;

        if( (threeHourChild = item.findChildBy(DataSchema.RENTAL_THREE_HOURS)) != null)
            ((TextView)itemView.findViewById(R.id.rental_slide_three_hour_price)).setText(threeHourChild.getValue());

        if( (fullDayChild = item.findChildBy(DataSchema.RENTAL_FULL_DAY)) != null)
            ((TextView)itemView.findViewById(R.id.rental_slide_full_day_price)).setText(fullDayChild.getValue());

    }


    @Override
    public String getDefaultTitle() {
        return "Rentals";
    }

    @Override
    public String getSlideTitle() {
        return getString(R.string.string_slide_title_rentals);
    }
}
