package com.californiadreamshostel.officetv.VIEWS;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.californiadreamshostel.officetv.R;

public class ClipFrame extends FrameLayout {

    public static int TOP_LEFT = R.styleable.ClipFrame_TOP_LEFT_RADII;
    public static int TOP_RIGHT = R.styleable.ClipFrame_TOP_RIGHT_RADII;
    public static int BOTTOM_LEFT = R.styleable.ClipFrame_BOTTOM_LEFT_RADII;
    public static int BOTTOM_RIGHT = R.styleable.ClipFrame_BOTTOM_RIGHT_RADII;



    private final Path stencilPath = new Path();

    private int topLeftRadii = 0;
    private int topRightRadii = 0;
    private int bottomLeftRadii = 0;
    private int bottomRightRadii = 0;

    public ClipFrame(Context context) {
        this(context, null);
    }
    public ClipFrame(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        initialize(context.obtainStyledAttributes(attrs, R.styleable.ClipFrame));
    }
    public ClipFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context.obtainStyledAttributes(attrs, R.styleable.ClipFrame));
    }

    private void initialize(final TypedArray array){

        for(int i =0; i < array.length(); i++){

            final int a = array.getIndex(i);

            if(a == R.styleable.ClipFrame_TOP_LEFT_RADII)
                topLeftRadii = array.getDimensionPixelSize(a, 0);

            if(a == TOP_RIGHT)
                topRightRadii = array.getDimensionPixelSize(a, 0);

            if(a == BOTTOM_LEFT)
                bottomLeftRadii = array.getDimensionPixelSize(a, 0);

            if(a == BOTTOM_RIGHT)
                bottomRightRadii = array.getDimensionPixelSize(a, 0);
        }

        Log.i("CLIPFRAME", "Fetched all potential corner Radii: \n" +
        "TOP_LEFT: " + String.valueOf(topLeftRadii) + "\n" +
        "TOP_RIGHT: " + String.valueOf(topRightRadii));

        array.recycle();
    }

    @TargetApi(21)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        final float[] radii = {
                topLeftRadii,topLeftRadii,  //TOP-LEFT
                topRightRadii,topRightRadii,  //TOP-RIGHT
                bottomRightRadii, bottomRightRadii, //BOTTOM-RIGHT
                bottomLeftRadii, bottomLeftRadii //BOTTOM-LEFT
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
