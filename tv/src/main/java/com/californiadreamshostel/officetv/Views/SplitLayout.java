package com.californiadreamshostel.officetv.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.californiadreamshostel.officetv.R;

import static com.californiadreamshostel.officetv.Views.SplitLayout.ATTRS.*;

/**
 * Splits two views between a Spacing object
 * that is positioned according to the client.
 * The views therein, will take on ALL the bounds they can
 * while respecting this viewgroups size, and the available
 * area on their side of the Spacing object
 */
public class SplitLayout extends ViewGroup {

    public static final float DEFAULT_SPACING_POS = 0.5F;
    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;

    private float spacingPos = DEFAULT_SPACING_POS;

    @AttrRes
    private int spacingSize;

    @AttrRes
    private int orientation = ORIENTATION_VERTICAL;

    private Rect bounds = new Rect();

    public SplitLayout(@NonNull final Context context,
                       @NonNull final AttributeSet set){

        super(context, set);

        final TypedArray attrs = context.obtainStyledAttributes(set, R.styleable.SplitLayout);

        try {
            for (int i = 0; i < attrs.length(); i++) {

                Log.i("SplitLayout", "Accessing Index: " + String.valueOf(i));

                final int resIndex = attrs.getIndex(i);

                if (resIndex == ORIENTATION)
                    orientation = resolveOrientation(attrs.getInt(resIndex, 0));

                else if (resIndex == SPACING_SIZE)
                    spacingSize = attrs.getDimensionPixelSize(resIndex, 0);

                else if (resIndex == SPACING_POS)
                    spacingPos = attrs.getFloat(resIndex, -1F);

            }
        }catch (ArrayIndexOutOfBoundsException e){
            //Why in the hell is this needed. Seriously what the F***
            Log.i("SplitLayout", "Caught the exception!");
        }

        Log.i("SplitLayout", "Fetched xml information");

        attrs.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        measureChildOne(widthMeasureSpec, heightMeasureSpec);
        measureChildtwo(widthMeasureSpec, heightMeasureSpec);

        Log.i("SplitLayout", "Measured children");

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final View childOne = getChildAt(0);
        final View childTwo = getChildAt(1);

        final int childOneWidth = childOne.getMeasuredWidth();
        final int childTwoWidth = childTwo.getMeasuredWidth();
        final int childOneHeight = childOne.getMeasuredHeight();
        final int childTwoHeight = childTwo.getMeasuredHeight();

        makeBounds(l, t, r, b);


        if(orientation == ORIENTATION_VERTICAL){

            childOne
                    .layout(getPaddingStart(),
                            getPaddingTop(),
                            getMeasuredWidth()-getPaddingEnd(),
                            childOneHeight);

            childTwo
                    .layout(getPaddingStart(),
                            getMeasuredHeight() - getPaddingBottom()-childTwoHeight,
                            getMeasuredWidth()-getPaddingEnd(),
                            getMeasuredHeight()-getPaddingBottom());

        }

        if(orientation == ORIENTATION_HORIZONTAL) {
            childOne
                    .layout(getPaddingStart(),
                            getPaddingTop(),
                            getPaddingStart() + childOneWidth,
                            getMeasuredHeight()-getPaddingBottom());

            childTwo
                    .layout(getMeasuredWidth() - getPaddingEnd() - childTwoWidth,
                            getPaddingTop(),
                            getMeasuredWidth() - getPaddingEnd(),
                            getMeasuredHeight() - getPaddingBottom());
        }

    }

    /**
     * Measures the first child via simple math
     */
    private void measureChildOne(final int widthSpec, final int heightSpec){

        int childWidthMode = 0;
        int childWidthSize = 0;

        int childHeightMode = 0;
        int childHeightSize = 0;


        /**
         * Now we resolve sizes.
         *
         * CHILD ONE
         * Size is w/h Times the spacing, minus a half of the spacing size.
         *
         * E.G. if the total width is 100, and we're in horitzonal orientation, and the pos is .5F, and
         * the size is 11
         *
         * Child ones width is: (100*.5) - (11/2)
         * E.G: (50 - 5.5)
         * E.G: 44.5, or 44
         *
         */

        final float spacingAdjustment = spacingSize * 0.5F;

        if(orientation == ORIENTATION_VERTICAL){
            childWidthMode = MeasureSpec.AT_MOST;
            childHeightMode = MeasureSpec.EXACTLY;

            final float height = MeasureSpec.getSize(heightSpec)
                    *spacingPos;

            childHeightSize = Math.round(height - spacingAdjustment);

            childWidthSize = MeasureSpec.getSize(widthSpec);

        }

        if(orientation == ORIENTATION_HORIZONTAL){
            childWidthMode = MeasureSpec.EXACTLY;
            childHeightMode = MeasureSpec.AT_MOST;


            final float width = MeasureSpec.getSize(widthSpec)
                    *spacingPos;

            childWidthSize = Math.round(width - spacingAdjustment);
            childHeightSize = MeasureSpec.getSize(heightSpec);
        }


        final int childWidthSpec = MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode);
        final int childHeightSpec = MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode);


        getChildAt(0)
                .measure(childWidthSpec, childHeightSpec);
    }

    private void measureChildtwo(final int widthSpec, final int heightSpec){

        int childWidthMode =0;
        int childWidth = 0;

        int childHeightMode = 0;
        int childHeight = 0;

        final float spacingAdjustment = spacingSize * 0.5F;

        if(orientation == ORIENTATION_VERTICAL){

            childHeightMode = MeasureSpec.EXACTLY;
            childWidthMode = MeasureSpec.AT_MOST;

            childWidth = MeasureSpec.getSize(widthSpec);

            final int totalHeight = MeasureSpec.getSize(heightSpec);

            final float viewHeight = (totalHeight - (totalHeight*spacingPos)) -
                    spacingAdjustment;

            childHeight = Math.round(viewHeight);
        }

        if(orientation == ORIENTATION_HORIZONTAL){

            childWidthMode = MeasureSpec.EXACTLY;
            childHeightMode = MeasureSpec.AT_MOST;

            childHeight = MeasureSpec.getSize(heightSpec);

            final int totalWidth = MeasureSpec.getSize(widthSpec);

            final float viewWidth = (totalWidth - (totalWidth*spacingPos) ) -
                    spacingAdjustment;

            childWidth = Math.round(viewWidth);
        }


        getChildAt(1)
                .measure(MeasureSpec.makeMeasureSpec(childWidth, childWidthMode),
                        MeasureSpec.makeMeasureSpec(childHeight, childHeightMode));

    }

    private int resolveOrientation(@IntRange(from = 0, to = 1) final int resEnum){
        return (resEnum == 0) ? ORIENTATION_VERTICAL : ORIENTATION_HORIZONTAL;
    }

    private void makeBounds(final int left, final int top, final int right, final int bottom){
        bounds.set(left + getPaddingStart(),
                top + getPaddingTop(),
                right - getPaddingEnd(),
                bottom -
                        getPaddingBottom());



    }

    public static class ATTRS{
        static final int ORIENTATION = R.styleable.SplitLayout_layout_orientation;
        static final int SPACING_SIZE = R.styleable.SplitLayout_view_spacing_size;
        static final int SPACING_POS = R.styleable.SplitLayout_view_spacing_position;
    }

}


/**
 *  REQUIREMENTS
 *
 *  Can include a spacing between two children
 *  that is 'N' dp wide / tall, that can be explicitly
 *  chosen where to go based on a value percentage of the Width / height
 *  of the viewgroup
 *
 *  The two children will then be sized based on the remainding space.
 *  E.g. a space at 70% will make view 1 70%-spacing big, and layout 2
 *  30%-spacing big.
 *
 *
 *  -Can include a reference to two views based on integer xml attributes:
 *   --layout_one
 *   --layout_two
 *
 * -Can specify Orientation (Defines if spacing will be wide/tall, and will help resolve children sizes)
 *
 */
