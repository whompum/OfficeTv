package com.californiadreamshostel.officetv.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.californiadreamshostel.officetv.R;

public class SlideTitleView extends LinearLayout {

    @IdRes
    public static final int ID = R.id.id_slide_title_container;

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_slide_title;

    @IdRes
    public static final int ID_CALI_TITLE = R.id.id_slide_title_cali;

    @IdRes
    public static final int ID_DREAMS_TITLE = R.id.id_slide_title_dreams;

    @IdRes
    public static final int ID_TITLE_PAGE = R.id.id_slide_title_page;


    public SlideTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(LAYOUT, this);

        init(context.obtainStyledAttributes(attrs, R.styleable.SlideTitleView));
    }

    public SlideTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(LAYOUT, this);


        init(context.obtainStyledAttributes(attrs, R.styleable.SlideTitleView));
    }

    private void init(final TypedArray array){
        setId(ID);

        String title = "";

        for(int i = 0; i < array.getIndexCount(); i++){

            final int index = array.getIndex(i);

            if(index == R.styleable.SlideTitleView_slidePageTitle)
                title = array.getString(index);
        }

        array.recycle();

        setPageTitle(title);
        setText(ID_CALI_TITLE, getResources().getString(R.string.string_slide_title_cali));
        setText(ID_DREAMS_TITLE, getResources().getString(R.string.string_slide_title_dreams));

    setOrientation(VERTICAL);
    }

    @Nullable
    private View getChild(final int id){

        for(int i =0; i < getChildCount(); i++)
            if(getChildAt(i).getId() == id)
                return getChildAt(i);


    return null;
    }


    public void setPageTitle(String title){
        setText(ID_TITLE_PAGE, title);
    }

    public void setText(final int id, final String text){

        final View child = getChild(id);

        if(child == null) {
            Log.i("SlIDE_TITLE", "CHILD IS NULL");
            return;
        }

        if( !(child instanceof TextView) ) {
            Log.i("SlIDE_TITLE", "CHILD IS NOT A TEXT VIEW");
            return;
        }

        ((TextView) child).setText(text);
        }

    /**
     *Replace every character with itself+spacings
     * @param spacingCount Cuantos spacing to add to each character
     * @param text obvious what this is
     * @return spaced text
     */
    private static String spaceText(final int spacingCount, final String text){

        if(text.length() == 0 |
                text.length() == 1)
            return text;

        String spacing = "";

        for(int i =0; i < spacingCount; i++)
            spacing+=" ";

    return text.replaceAll(".(?=.)", "$0"+spacing);
    }


}
