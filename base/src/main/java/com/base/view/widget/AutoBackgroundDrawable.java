package com.base.view.widget;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

/**
 * Created by on 2/17/2017.
 * <p>
 * The stateful LayerDrawable used for press layout.
 */

public class AutoBackgroundDrawable extends LayerDrawable {
    // The color filter to apply when the button is pressed
    private ColorFilter mPressedFilter = new LightingColorFilter(Color.LTGRAY, 1);

    public AutoBackgroundDrawable(Drawable d) {
        super(new Drawable[]{d});
    }

    @Override
    protected boolean onStateChange(int[] states) {
        boolean enabled = false;
        boolean pressed = false;

        for (int state : states) {
            if (state == android.R.attr.state_enabled)
                enabled = true;
            else if (state == android.R.attr.state_pressed)
                pressed = true;
        }

        mutate();
        if (enabled && pressed) {
            setColorFilter(mPressedFilter);
        } else {
            setColorFilter(null);
            setAlpha(enabled ? 255 : 100);//disabledAlpha = 100, fullAlpha = 255
        }

        invalidateSelf();

        return super.onStateChange(states);
    }

    @Override
    public boolean isStateful() {
        return true;
    }
}