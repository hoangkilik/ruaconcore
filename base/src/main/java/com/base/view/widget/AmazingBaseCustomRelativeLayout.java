package com.base.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Created by on 13/07/2016.
 * Base for all custom layout, auto add click sound for all clickable view
 */
public abstract class AmazingBaseCustomRelativeLayout extends RelativeLayout {

    public AmazingBaseCustomRelativeLayout(Context context) {
        super(context);
        setLayout();
        initCompoundView();
        initData();
        initListener();
    }

    public AmazingBaseCustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        setLayout();
        initCompoundView();
        initData();
        initListener();
    }

    protected int[] getStyleableId() {
        return null;
    }

    protected void initDataFromStyleable(TypedArray a) {
    }

    protected abstract int getLayoutId();

    protected abstract void initCompoundView();

    protected abstract void initData();

    protected abstract void initListener();

    private void init(AttributeSet attrs) {
        if (getStyleableId() != null && getStyleableId().length > 0) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, getStyleableId(), 0, 0);
            initDataFromStyleable(a);
            a.recycle();
        }
    }

    private void setLayout() {
        if (getLayoutId() != 0) {
            LayoutInflater.from(getContext()).inflate(getLayoutId(), this, true);
        }
    }
}