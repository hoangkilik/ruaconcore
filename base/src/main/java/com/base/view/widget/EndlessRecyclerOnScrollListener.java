package com.base.view.widget;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by on 11/2/2016.
 * <p>
 * Listener the view scroll to bottom.
 */

abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    public abstract void onLoadMore();

    private static final int VISIBLE_THRESHOLD = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int previousTotal = 0; // The total number of items in the data set after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private boolean mDisable = false;

    private RecyclerView.LayoutManager mLayoutManager;

    EndlessRecyclerOnScrollListener(RecyclerView.LayoutManager linearLayoutManager) {
        this.mLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();
        if (mLayoutManager instanceof LinearLayoutManager) {
            firstVisibleItem = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        } else if (mLayoutManager instanceof GridLayoutManager) {
            firstVisibleItem = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        }

        if (loading) {
            if (totalItemCount > previousTotal + 1) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!mDisable && !loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
            // End has been reached. Do something
            onLoadMore();
            loading = true;
        }
    }

    void reset() {
        previousTotal = 0;
        loading = true;
        firstVisibleItem = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        mDisable = false;
    }

    void disable() {
        mDisable = true;
    }
}