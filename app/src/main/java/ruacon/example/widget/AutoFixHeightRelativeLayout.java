package ruacon.example.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Nguyen Tien Hoang on 1/25/2017.
 */

public class AutoFixHeightRelativeLayout extends RelativeLayout {

    public AutoFixHeightRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        post(new Runnable() {
            @Override
            public void run() {
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getHeight());
                setLayoutParams(params);
            }
        });
    }
}