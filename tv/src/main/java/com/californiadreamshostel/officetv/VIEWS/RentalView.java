package com.californiadreamshostel.officetv.VIEWS;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.californiadreamshostel.officetv.R;

public class RentalView extends LinearLayout {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_rental_viewgroup;

    public static final int WIDTH = R.dimen.size_rental_view_width;

    @IdRes
    public static final int ID_ICON = R.id.id_rental_icon;

    @IdRes
    public static final int ID_PRICE = R.id.id_rental_price;

    @IdRes
    public static final int ID_TITLE = R.id.id_rental_title;

    public RentalView(final Context context){
        super(context);
        init();
    }

    public RentalView(final Context context, final AttributeSet set){
        super(context, set);
        init();
        initWithResources(context.obtainStyledAttributes(set, R.styleable.RentalView));
    }


    private void init(){
        LayoutInflater.from(getContext()).inflate(LAYOUT, this);
    }

    private void initWithResources(@NonNull final TypedArray array){

        for(int i = 0; i < array.length(); i++){
            final int index = array.getIndex(i);

            if(index == R.styleable.RentalView_rentalIcon)
              setIcon(array.getDrawable(index));

             if(index ==  R.styleable.RentalView_rentalPrice)
                setPrice(array.getString(index));

            if(index == R.styleable.RentalView_rentalTitle)
                setTitle(array.getString(index));

        }

    }

    public void setIcon(@DrawableRes final int drawableRes){
        setIcon(getResources().getDrawable(drawableRes, null));
    }

    public void setIcon(final Drawable drawable){

        final View child = getChildFromId(ID_ICON);

        if(child != null) {
            ((ImageView) child).setImageDrawable(drawable);
            invalidate();
        }
    }

    public void setPrice(final int price, @Nullable String symbol, boolean wholeDigitsOnly){

        //TODO allow the use of Decimal Digits
        if(symbol == null)
            symbol = "$";

        setPrice(symbol + String.valueOf(price));
    }

    public void setPrice(@Nullable String total){

        if(total == null)
            total = "$0";

        final View priceRTV = getChildFromId(ID_PRICE);

        if(priceRTV != null) {
            ((RTV) priceRTV).setText(total);
            invalidate();
        }
    }

    public void setTitle(@Nullable String title){

        if(title == null)
            title = "LOREM";

        final View child = getChildFromId(ID_TITLE);

        if(child != null) {
            ((RTV) child).setText(title);
            invalidate();
        }
    }

    @Nullable
    private View getChildFromId(@IdRes final int id){

        for(int i= 0; i < getChildCount(); i++){
            if(getChildAt(i).getId() == id)
                return getChildAt(i);
        }

        return null;
    }


    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(getResources().getDimensionPixelSize(WIDTH), MeasureSpec.EXACTLY), heightMeasureSpec);
    }
}
