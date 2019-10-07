package ruacon.example.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.base.view.activity.AmazingBaseActivity;

import ruacon.example.R;

/**
 * Created by on 2/24/2017.
 */

public class FirebaseActivity extends AmazingBaseActivity {
    private TextView[] mTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_firebase;
    }

    @Override
    protected void initViews() {
        mTv = new TextView[6];
        mTv[0] = findViewById(R.id.tv_link_information);
        mTv[1] = findViewById(R.id.tv_link_album);
        mTv[2] = findViewById(R.id.tv_link_profile);
        mTv[3] = findViewById(R.id.tv_deep_link_information);
        mTv[4] = findViewById(R.id.tv_deep_link_album);
        mTv[5] = findViewById(R.id.tv_deep_link_profile);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListeners() {
        for (final TextView tv : mTv) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tv.getText().toString()));
                    startActivity(browserIntent);
                }
            });
        }
    }
}