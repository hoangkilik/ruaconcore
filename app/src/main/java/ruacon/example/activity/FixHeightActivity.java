package ruacon.example.activity;

import android.view.View;

import com.base.view.activity.AmazingBaseActivity;

import ruacon.example.R;

/**
 * Created by on 2/2/2017.
 */

public class FixHeightActivity extends AmazingBaseActivity {
    private View[] mViews;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fix_height;
    }

    @Override
    protected void initViews() {
        mViews = new View[3];
        mViews[0] = findViewById(R.id.view_1);
        mViews[1] = findViewById(R.id.view_2);
        mViews[2] = findViewById(R.id.view_3);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListeners() {
        for (View view : mViews) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setVisibility(View.GONE);
                }
            });
        }
    }
}