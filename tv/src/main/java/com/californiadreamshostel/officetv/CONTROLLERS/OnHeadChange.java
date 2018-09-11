package com.californiadreamshostel.officetv.CONTROLLERS;

import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.A.Slide;

public interface OnHeadChange {
    void onNewHead(@NonNull final Slide newSlide, @NonNull final Slide oldSlide);
}
