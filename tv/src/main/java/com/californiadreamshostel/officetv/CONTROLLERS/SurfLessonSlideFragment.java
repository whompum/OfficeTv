package com.californiadreamshostel.officetv.CONTROLLERS;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.californiadreamshostel.officetv.R;
import com.californiadreamshostel.officetv.VIEWS.RTV;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SurfLessonSlideFragment extends Slide {

    public static final String DEFAULT_TITLE = "SurfLessonsSlideFragment.class";

    @LayoutRes
    public static final int LAYOUT = R.layout.slides_surf_lessons;

    @BindView(R.id.local_include_zero_label) protected RTV zeroLabelDisplay;
    @BindView(R.id.local_include_zero_desc) protected RTV zeroDescDisplay;
    @BindView(R.id.local_include_one_label) protected RTV oneLabelDisplay;
    @BindView(R.id.local_include_one_desc) protected RTV oneDescDisplay;
    @BindView(R.id.local_include_two_label) protected RTV twoLabelDisplay;
    @BindView(R.id.local_include_two_desc) protected RTV twoDescDisplay;
    @BindView(R.id.local_include_three_label) protected RTV threeLabelDisplay;
    @BindView(R.id.local_include_three_desc) protected RTV threeDescDisplay;
    @BindView(R.id.local_surf_lesson_price) protected RTV surfPriceDisplay;
    @BindView(R.id.local_surf_lesson_times) protected RTV surfTimesDisplay;

    private Unbinder unbinder;

    public static final Fragment newInstance(){
        final SurfLessonSlideFragment slide = new SurfLessonSlideFragment();
        return slide;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View content = inflater.inflate(LAYOUT, container, false);

        this.unbinder = ButterKnife.bind(this, content);

        return content;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unbinder.unbind();
    }

    @Override
    String getDefaultTitle() {
        return "Surf Lessons";
    }

    @Override
    String getSlideTitle() {
        return getString(R.string.string_surf_lesson);
    }
}
