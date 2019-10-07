package ruacon.example.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.base.view.widget.AmazingBaseCustomRelativeLayout;

import ruacon.example.R;

/**
 * Created by Nguyen Tien Hoang on 10/5/16.
 * <p>
 * Like badge on iOS. Using to show number notification.
 */

public class Badge extends AmazingBaseCustomRelativeLayout {
    private TextView tvBadge;

    public Badge(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_badge;
    }

    @Override
    protected void initCompoundView() {
        tvBadge = findViewById(R.id.tv_badge);
        setVisibility(GONE);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
    }

    /**
     * Display number notification
     *
     * @param numberNotification Number display on badge.
     */
    public void setNumberNotification(int numberNotification) {
        if (numberNotification <= 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            tvBadge.setText(numberNotification < 1000 ? String.valueOf(numberNotification) : "999+");
            if (numberNotification < 10) {
                setPadding(20, 0, 20, 0);
            } else {
                setPadding(5, 0, 5, 0);
            }
        }
    }
}