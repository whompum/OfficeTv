package com.californiadreamshostel.officetv.VIEWS;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.californiadreamshostel.officetv.R;

public class RTV extends AppCompatTextView {


    //Resolves to Regular
    public static final int DEFAULT_FONT = 2;

    public static final String ROBOTO_THIN = "Roboto-Thin_0.ttf";
    public static final String LIGHT_PATH = "Roboto-Light.ttf";
    public static final String NORMAL_PATH = "Roboto-Regular.ttf";

    public RTV(final Context context){
        super(context);
    }

    public RTV(final Context context, final AttributeSet set){
        super(context, set);
        setTypeface(resolveTypeface(context, set));
    }

    protected final Typeface resolveTypeface(final Context c, final AttributeSet set){

        final TypedArray array = c.obtainStyledAttributes(set, R.styleable.RTV);

        final int type = array.getInteger(0, DEFAULT_FONT);

        array.recycle();

    return Typeface.createFromAsset(fetchAssetManager(c), resolvePath(type));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected final AssetManager fetchAssetManager(final Context context){
        return context.getAssets();
    }

    protected final String resolvePath(final int font){
        String path = NORMAL_PATH;
        switch(font){
            case 0: path = ROBOTO_THIN;
            break;
            case 1: path = LIGHT_PATH;
            break;
            case 2: path = NORMAL_PATH;
            break;
        }

    return path;
    }

}
