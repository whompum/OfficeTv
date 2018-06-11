package com.californiadreamshostel.officetv.VIEWS;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.californiadreamshostel.officetv.R;

public class WeatherFrame extends FrameLayout {
    private final Path stencilPath = new Path();
    private float cornerRadius = 0;

    public WeatherFrame(Context context) {
        this(context, null);
    }
    public WeatherFrame(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public WeatherFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        cornerRadius = getResources().getDimensionPixelSize(R.dimen.corner_radius_large);
    }

    @TargetApi(21)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        final float[] radii = {
                0,0,  //TOP-LEFT
                0,0,  //TOP-RIGHT
                cornerRadius, cornerRadius, //BOTTOM-RIGHT
                cornerRadius, cornerRadius //BOTTOM-LEFT
        };

        // compute the path
        stencilPath.reset();
        stencilPath.addRoundRect(0, 0, w, h, radii, Path.Direction.CW);
        stencilPath.close();
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(stencilPath);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }

}
