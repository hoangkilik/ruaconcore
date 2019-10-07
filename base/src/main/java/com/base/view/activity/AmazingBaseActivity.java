package com.base.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.base.R;

/**
 * Created by Nguyen Tien Hoang on 30/12/2015.
 * <p>
 * Design skeleton for base activity
 */
public abstract class AmazingBaseActivity extends AppCompatActivity {

    public static final int ANIM_BOTTOM_TO_TOP = 1;
    public static final int ANIM_TOP_TO_BOTTOM = 2;
    public static final int ANIM_RIGHT_TO_LEFT = 3;
    public static final int ANIM_LEFT_TO_RIGHT = 4;
    public static final int ANIM_FADE_IN_FADE_OUT = 5;

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void initData();

    protected abstract void initListeners();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }

        initViews();

        initData();

        initListeners();
    }

    public void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    public void startActivity(Class<?> cls, int animationType) {
        startActivity(cls);
        startTransition(animationType);
    }

    public void finish(int animationType) {
        finish();
        startTransition(animationType);
    }

    public void startActivity(Intent intent, int animationType) {
        super.startActivity(intent);
        startTransition(animationType);
    }

    public void startActivityForResult(Intent intent, int requestCode, int animationType) {
        super.startActivityForResult(intent, requestCode);
        startTransition(animationType);
    }

    public void startTransition(int animationType) {
        switch (animationType) {
            case ANIM_BOTTOM_TO_TOP:
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                break;
            case ANIM_TOP_TO_BOTTOM:
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            case ANIM_RIGHT_TO_LEFT:
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
            case ANIM_LEFT_TO_RIGHT:
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case ANIM_FADE_IN_FADE_OUT:
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    /**
     * Finish activity with Go to bottom animation.
     */
    public void finishGoToBottom() {
        finish(ANIM_TOP_TO_BOTTOM);
    }

    /**
     * Finish activity with Go to right animation.
     */
    public void finishGoToRight() {
        finish(ANIM_LEFT_TO_RIGHT);
    }

    /**
     * Finish activity with Go to right animation with result code
     */
    public void finishGoToRight(int resultCode) {
        setResult(resultCode);
        finishGoToRight();
    }

    /**
     * Finish activity with Go to right animation with result code and data
     */
    public void finishGoToRight(int resultCode, Intent data) {
        setResult(resultCode, data);
        finishGoToRight();
    }

    /**
     * Start activity with Go to top animation.
     *
     * @param intent Intent start activity.
     */
    public void startActivityFromBottom(Intent intent) {
        startActivity(intent, ANIM_BOTTOM_TO_TOP);
    }

    public void startActivityFromRight(Class<?> cls) {
        startActivity(new Intent(this, cls), ANIM_RIGHT_TO_LEFT);
    }
}