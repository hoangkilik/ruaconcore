package com.base.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.util.LogUtil;

/**
 * Created by Nguyen Tien Hoang on 30/12/2015.
 */
public abstract class AmazingBaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutId() <= 0) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        LogUtil.i("Fragment: " + getClass().getSimpleName());
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initOtherViews(view);
        initData();
        initListeners();
    }

    /**
     * Define layout id for view of this fragment
     *
     * @return The layout id of this fragment
     */
    protected abstract int getLayoutId();

    /**
     * Init view and components of fragment
     *
     * @param view Root view
     */
    protected abstract void initViews(View view);

    /**
     * Init and process data fragment
     */
    protected abstract void initData();

    /**
     * Init listener for all view
     */
    protected abstract void initListeners();

    /**
     * Design observer pattern. This method using to other class can ping an update to this fragment
     *
     * @param updateCode using to tag the type update
     * @param intent     the data using to update to this fragment
     */
    public void update(int updateCode, Intent intent) {
    }

    /**
     * Init some views in Base fragment extend me. Example tool bar for all fragment...
     *
     * @param view Root view
     */
    public void initOtherViews(View view) {
    }

    public void startActivity(Class<?> cls) {
        startActivity(new Intent(getActivity(), cls));
    }
}