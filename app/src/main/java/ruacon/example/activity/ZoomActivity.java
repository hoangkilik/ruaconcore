package ruacon.example.activity;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.base.util.LogUtil;
import com.base.view.activity.AmazingBaseActivity;

import java.util.ArrayList;
import java.util.List;

import ruacon.example.R;
import ruacon.example.adapter.ImageAdapter;
import ruacon.example.entity.ImageData;

/**
 * Created by Nguyen Tien Hoang on 1/6/2017.
 */

public class ZoomActivity extends AmazingBaseActivity {
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1f;
    private boolean mIsScaleEnd = true;
    private int mSize;
    private ViewGroup.LayoutParams mParams;

    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zoom;
    }

    @Override
    protected void initViews() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        List<ImageData> listImage = new ArrayList<>();
        listImage.add(new ImageData(R.drawable.pic_1));
        listImage.add(new ImageData(R.drawable.pic_2));
        listImage.add(new ImageData(R.drawable.pic_3));
        listImage.add(new ImageData(R.drawable.pic_4));
        listImage.add(new ImageData(R.drawable.pic_5));
        listImage.add(new ImageData(R.drawable.pic_6));
        listImage.add(new ImageData(R.drawable.pic_7));
        listImage.add(new ImageData(R.drawable.pic_8));
        mRecyclerView.setAdapter(new ImageAdapter(listImage));
        mParams = mRecyclerView.getLayoutParams();
        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mSize = mRecyclerView.getWidth();
            }
        });
    }

    @Override
    protected void initListeners() {
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScaleDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 4f));
            if (mScaleFactor < 1.05f) {
                mScaleFactor = 1f;
            }
//            mRenderer.setScaleFactor(mScaleFactor);
            mParams.width = (int) (mSize * mScaleFactor);
            mRecyclerView.setLayoutParams(mParams);
            LogUtil.d("onScale mScaleFactor: ", mScaleFactor);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mIsScaleEnd = false;
            LogUtil.d("onScale Begin");
//            updatePages();
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mIsScaleEnd = true;
            super.onScaleEnd(detector);
            LogUtil.d("onScale End");
        }
    }
}