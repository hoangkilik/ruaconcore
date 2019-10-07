package com.base.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by on 10/24/17.
 */

public class AutoBgImageView extends AppCompatImageView {

    public AutoBgImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        AutoBackgroundDrawable layer = new AutoBackgroundDrawable(drawable);
        super.setImageDrawable(layer);

    }
}