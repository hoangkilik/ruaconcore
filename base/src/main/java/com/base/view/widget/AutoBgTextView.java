package com.base.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Nguyen Tien Hoang on 10/24/17.
 */

public class AutoBgTextView extends AppCompatTextView {

    public AutoBgTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        AutoBackgroundDrawable layer = new AutoBackgroundDrawable(background);
        super.setBackgroundDrawable(layer);
    }
}