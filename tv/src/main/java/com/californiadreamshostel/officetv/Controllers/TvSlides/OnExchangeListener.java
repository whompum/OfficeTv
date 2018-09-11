package com.californiadreamshostel.officetv.Controllers.TvSlides;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface OnExchangeListener {
    boolean onExchange(@NonNull final Slide s, @Nullable final Slide oldSlide);
}
