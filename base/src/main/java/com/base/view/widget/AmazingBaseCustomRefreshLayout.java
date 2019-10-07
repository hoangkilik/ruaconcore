package com.base.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

/**
 * Created by on 13/07/2016.
 */
public abstract class AmazingBaseCustomRefreshLayout extends SwipeRefreshLayout {

    public AmazingBaseCustomRefreshLayout(Context context) {
        super(context);
        setLayout();
        initCompoundView();
        initData();
        initListener();
    }

    public AmazingBaseCustomRefreshLayout(Context context, AttributeSet attrs) {
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
            LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(getLayoutId(), this, true);
        }
    }
}