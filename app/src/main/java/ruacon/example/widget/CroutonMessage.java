package ruacon.example.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ruacon.example.R;

/**
 * Created by Nguyen Tien Hoang on 11/23/2016.
 * <p>
 * Show message from top to bottom.
 */
public class CroutonMessage {

    public interface OnClickCroutonListener {
        void onClickCrouton();
    }

    private static final int DISPLAY_TIME = 50000;
    private static final int ANIMATION_TIME = 400;
    private static final int ANIMATION_DISTANCE = -400;

    private Context mContext;
    private WindowManager mWindowManager;
    private CroutonView mCrouton;
    private WindowManager.LayoutParams mParams;
    private ValueAnimator animatorStart;
    private ValueAnimator animatorFinish;
    private boolean isFinishing;
    private OnClickCroutonListener mOnClickCroutonListener;

    public CroutonMessage(Context context) {
        this.mContext = context;
        init();
    }

    private int dpToPixels(Context context, int dp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return Math.round(dp * scaledDensity);
    }

    private void init() {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                dpToPixels(mContext, 100),
                Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT ? WindowManager.LayoutParams.TYPE_SYSTEM_ALERT : WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        mParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        mParams.x = 0;
        mParams.y = ANIMATION_DISTANCE;

        animatorStart = ValueAnimator.ofInt(ANIMATION_DISTANCE, 0);
        animatorStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mParams.y = (int) valueAnimator.getAnimatedValue();
                updateViewLayout();
            }
        });
        animatorStart.setDuration(ANIMATION_TIME);

        animatorFinish = ValueAnimator.ofInt(0, ANIMATION_DISTANCE);
        animatorFinish.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mParams.y = (int) valueAnimator.getAnimatedValue();
                if (mParams.y == ANIMATION_DISTANCE) {
                    removeCrouton();
                } else {
                    updateViewLayout();
                }
            }
        });
        animatorFinish.setDuration(ANIMATION_TIME);
    }

    private void finishCrouton() {
        isFinishing = true;
        animatorFinish.start();
    }

    private void updateViewLayout() {
        if (mCrouton.getWindowToken() != null) {
            mWindowManager.updateViewLayout(mCrouton, mParams);
        }
    }

    private void removeCrouton() {
        if (mWindowManager != null && mCrouton != null && mCrouton.getWindowToken() != null) {
            mWindowManager.removeView(mCrouton);
            isFinishing = false;
        }
    }

    public void setOnClickCroutonListener(OnClickCroutonListener listener) {
        this.mOnClickCroutonListener = listener;
    }

    public void show(CharSequence message) {
        if (mCrouton == null) {
            mCrouton = new CroutonView(mContext);
            mCrouton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFinishing) {
                        removeCrouton();
                    } else {
                        finishCrouton();
                    }

                    if (mOnClickCroutonListener != null) {
                        mOnClickCroutonListener.onClickCrouton();
                    }
                }
            });
        }
        mCrouton.setText(message);
        mCrouton.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFinishing) {
                    removeCrouton();
                } else {
                    finishCrouton();
                }
            }
        }, DISPLAY_TIME);
        mWindowManager.addView(mCrouton, mParams);
        animatorStart.start();
    }

    private class CroutonView extends TextView {
        public CroutonView(Context context) {
            super(context);
            initUI();
        }

        private void initUI() {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(layoutParams);
            setBackgroundResource(R.drawable.bg_notify_app);
            setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher, 0, 0, 0);
            setTextColor(Color.WHITE);
            setGravity(Gravity.CENTER_VERTICAL);
            setCompoundDrawablePadding(20);
        }
    }
}