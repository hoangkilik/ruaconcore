package ruacon.example.activity;

import android.widget.TextView;

import com.base.util.LogUtil;
import com.base.view.activity.AmazingBaseActivity;

import ruacon.example.R;
import ruacon.example.TextViewClickMovement;

/**
 * Created by Nguyen Tien Hoang on 2017-07-26.
 */

public class TextViewMovementActivity extends AmazingBaseActivity {

    private TextView mTvMovement;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_text_view_movement;
    }

    @Override
    protected void initViews() {
        mTvMovement = findViewById(R.id.tv_movement);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListeners() {
        String text = "Phone: 0123456789";
        text += "/n/nUrl: http://rxjd9k047gew.bo-ard.com/album/5ead5eaf75d7dea30db89bb50ad74ff6/";
        mTvMovement.setText(text);
        mTvMovement.setMovementMethod(new TextViewClickMovement(this, new TextViewClickMovement.OnTextViewClickMovementListener() {
            @Override
            public void onLinkClicked(String linkText, TextViewClickMovement.LinkType linkType) {
                LogUtil.d("Click", linkText);
            }

            @Override
            public void onLongClick(String text) {

            }
        }));
    }
}