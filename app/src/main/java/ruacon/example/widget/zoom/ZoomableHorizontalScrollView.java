package ruacon.example.widget.zoom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.HorizontalScrollView;

import com.base.util.LogUtil;

/**
 * Created by Bui Xuan VU on 1/13/2017.
 * Zoomable scroll view
 */

public class ZoomableHorizontalScrollView extends HorizontalScrollView {

    // States.
    private static final byte NONE = 0;
    private static final byte DRAG = 1;
    private static final byte ZOOM = 2;

    private byte mode = NONE;

    // Matrices used to move and zoom image.
    private Matrix matrix = new Matrix();
    private Matrix matrixInverse = new Matrix();
    private Matrix savedMatrix = new Matrix();

    // Parameters for zooming.
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float[] lastEvent = null;
    private long lastDownTime = 0l;

    private float[] mDispatchTouchEventWorkingArray = new float[2];
    private float[] mOnTouchEventWorkingArray = new float[2];

    private int mCanvasWidth;
    private int mCanvasHeight;
    private float mScaleFactor;

    // Gesture detector used to handle fling, scroll, double touch gesture
    private GestureDetector mGestureDetector;

    // Listener handle by user
    private OnTouchViewListener mOnTouchViewListener;
    private OnZoomHandlerListener mOnZoomHandlerListener;
    private OnChangePageListener mOnChangePageListener;

    // Parameters detect state of scroll view
    private int mCurrentPosition = 0;

    public ZoomableHorizontalScrollView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public ZoomableHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        sharedConstructing(context);
    }

    private void sharedConstructing(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void setOnTouchViewListener(OnTouchViewListener listener) {
        mOnTouchViewListener = listener;
    }

    public void setOnZoomHandlerListener(OnZoomHandlerListener listener) {
        mOnZoomHandlerListener = listener;
    }

    public void setOnChangePageListener(OnChangePageListener listener) {
        mOnChangePageListener = listener;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float[] scaledPointsToScreenPoints(float[] a) {
        matrix.mapPoints(a);
        return a;
    }

    private float[] screenPointsToScaledPoints(float[] a) {
        matrixInverse.mapPoints(a);
        return a;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = 0;
        int width = 0;
        int childCount = getChildCount();
        LogUtil.d("ZoomableScrollView: ", "onMeasure: ", childCount);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                height += child.getMeasuredHeight();
                width = Math.max(width, child.getMeasuredWidth());
            }
        }
        mCanvasWidth = width;
        mCanvasHeight = height;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        float[] values = new float[9];
        matrix.getValues(values);

        if (values[Matrix.MSCALE_X] < getMinScale()) {
            float scale = getMinScale() / values[Matrix.MSCALE_X];
            matrix.postScale(scale, scale, mid.x, mid.y);
            matrix.invert(matrixInverse);
        } else if (values[Matrix.MSCALE_X] > getMaxScale()) {
            float scale = getMaxScale() / values[Matrix.MSCALE_X];
            matrix.postScale(scale, scale, mid.x, mid.y);
            matrix.invert(matrixInverse);
        }
        matrix.getValues(values);

        mScaleFactor = values[Matrix.MSCALE_X];
        Log.d("TAG", "ScaleFactor: " + mScaleFactor);
        canvas.save();
        canvas.translate(values[Matrix.MTRANS_X], values[Matrix.MTRANS_Y]);
        canvas.scale(values[Matrix.MSCALE_X], values[Matrix.MSCALE_Y]);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    public float getScaleFactor() {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[Matrix.MSCALE_Y];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // handle touch events here
        mOnTouchEventWorkingArray[0] = event.getX();
        mOnTouchEventWorkingArray[1] = event.getY();

        mOnTouchEventWorkingArray = scaledPointsToScreenPoints(mOnTouchEventWorkingArray);

        event.setLocation(mOnTouchEventWorkingArray[0], mOnTouchEventWorkingArray[1]);
        mGestureDetector.onTouchEvent(event);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                mode = DRAG;
                lastEvent = null;
                long downTime = event.getDownTime();
                if (downTime - lastDownTime < 300l) {
                    float density = getResources().getDisplayMetrics().density;
                    if (Math.max(Math.abs(start.x - event.getX()), Math.abs(start.y - event.getY())) < 40.f * density) {
                        savedMatrix.set(matrix);
                        mid.set(event.getX(), event.getY());
                        mode = ZOOM;
                        lastEvent = new float[4];
                        lastEvent[0] = lastEvent[1] = event.getX();
                        lastEvent[2] = lastEvent[3] = event.getY();
                    }
                    lastDownTime = 0l;
                } else {
                    lastDownTime = downTime;
                }
                start.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                final float density = getResources().getDisplayMetrics().density;
                if (mode == DRAG) {
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    onMove(dx, dy);
                } else if (mode == ZOOM) {
                    if (event.getPointerCount() > 1) {
                        float newDist = spacing(event);
                        if (newDist > 10f * density) {
                            float scale = (newDist / oldDist);
                            onZooming(scale);
                        }
                    } else {
                        float scale = event.getY() / start.y;
                        onZooming(scale);
                    }
                }
                break;
        }

        invalidate();
        return true;
    }

    private void onMove(float dx, float dy) {
        matrix.set(savedMatrix);

        float[] topLeft = {0f, 0f};
        float[] bottomRight = {getWidth(), getHeight()};
        /*
         * Corners of the view in screen coordinates, so dx/dy should not be allowed to
         * push these beyond the canvas bounds.
         */
        float[] scaledTopLeft = screenPointsToScaledPoints(topLeft);
        float[] scaledBottomRight = screenPointsToScaledPoints(bottomRight);

        dx = Math.min(Math.max(dx, scaledBottomRight[0] - mCanvasWidth), scaledTopLeft[0]);
        dy = Math.min(Math.max(dy, scaledBottomRight[1] - mCanvasHeight), scaledTopLeft[1]);


        matrix.postTranslate(dx, dy);
        matrix.invert(matrixInverse);
    }

    private void onZooming(float scale) {
        matrix.set(savedMatrix);
        matrix.postScale(scale, scale, mid.x, mid.y);
        matrix.invert(matrixInverse);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mDispatchTouchEventWorkingArray[0] = ev.getX();
        mDispatchTouchEventWorkingArray[1] = ev.getY();
        mDispatchTouchEventWorkingArray = screenPointsToScaledPoints(mDispatchTouchEventWorkingArray);
        ev.setLocation(mDispatchTouchEventWorkingArray[0], mDispatchTouchEventWorkingArray[1]);
        return super.dispatchTouchEvent(ev);
    }


    private float getMaxScale() {
        return 4f;
    }

    private float getMinScale() {
        return 1f;
    }

    private float getMiddleScale() {
        return 2f;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void compatPostOnAnimation(Runnable runnable, long delayMilis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            postOnAnimation(runnable);

        } else {
            postDelayed(runnable, delayMilis);
        }
    }

    public void zoomIn() {
        float targetZoom = (mScaleFactor < getMiddleScale()) ? getMiddleScale() : getMaxScale();
        if (mOnZoomHandlerListener != null) {
            mOnZoomHandlerListener.onChangeZoom(targetZoom);
        }
        DoubleTapZoom doubleTap = new DoubleTapZoom(targetZoom, getWidth() / 2, getHeight() / 2);
        compatPostOnAnimation(doubleTap, 1000 / 60);
    }

    public void zoomOut() {
        float targetZoom = (mScaleFactor > getMiddleScale()) ? getMiddleScale() : getMinScale();
        if (mOnZoomHandlerListener != null) {
            mOnZoomHandlerListener.onChangeZoom(targetZoom);
        }
        DoubleTapZoom doubleTap = new DoubleTapZoom(targetZoom, getWidth() / 2, getHeight() / 2);
        compatPostOnAnimation(doubleTap, 1000 / 60);
    }

    /**
     * do scroll to page position
     */
    public void scrollToPosition(int position) {
        //scroll to position
        int step = position - mCurrentPosition;
        if (mOnChangePageListener != null) {
            mCurrentPosition = position;
            mOnChangePageListener.onChangePage(position);
            float translateX = step * mOnChangePageListener.getPageWidth() * getScaleFactor();
            onMove(-translateX, 0);
            invalidate();
            savedMatrix.set(matrix);
        }
    }

    /**
     * Gesture Listener detects a single click or long click and passes that on
     * to the view's listener.
     *
     * @author Ortiz
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mOnTouchViewListener != null) {
                mOnTouchViewListener.onSingleTap();
            }
            return performClick();
        }

        @Override
        public void onLongPress(MotionEvent e) {
            performLongClick();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LogUtil.d("onFling", velocityX, velocityY);
            Log.d("TAG", "onFLing");
            FlingTapTranslate flingTapTranslate = new FlingTapTranslate(velocityX, velocityY);
            compatPostOnAnimation(flingTapTranslate, 100);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float[] values = new float[9];
            matrix.getValues(values);
            float translateX = values[Matrix.MTRANS_X];
            float scaleX = values[Matrix.MSCALE_X];
            LogUtil.d("TranslateXY: ", translateX);
            if (mOnChangePageListener != null) {
                float pageWidth = mOnChangePageListener.getPageWidth();
                int pageNum = Math.round(- translateX / (pageWidth * scaleX));
                LogUtil.d("PageNum: ", pageNum);
                if (pageNum != mCurrentPosition) {
                    mOnChangePageListener.onChangePage(pageNum);
                    mCurrentPosition = pageNum;
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float targetZoom = (mScaleFactor == getMinScale()) ? getMaxScale() : getMinScale();
            if (mOnZoomHandlerListener != null) {
                mOnZoomHandlerListener.onChangeZoom(targetZoom);
            }
            DoubleTapZoom doubleTap = new DoubleTapZoom(targetZoom, e.getX(), e.getY());
            compatPostOnAnimation(doubleTap, 1000 / 60);
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }
    }

    /**
     * DoubleTapZoom calls a series of runnables which apply
     * an animated zoom in/out graphic to the image.
     *
     * @author Ortiz
     */
    private class DoubleTapZoom implements Runnable {

        private static final float ZOOM_TIME = 500;
        private long startTime;
        private float startZoom, targetZoom;
        private float normalizedScale;
        private AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

        DoubleTapZoom(float targetZoom, float touchX, float touchY) {
            startTime = System.currentTimeMillis();
            this.startZoom = mScaleFactor;
            this.normalizedScale = mScaleFactor;
            this.targetZoom = targetZoom;
            Log.d("TAG", "Zooming: " + startZoom + "|" + targetZoom);
            savedMatrix.set(matrix);
            mid.set(touchX, touchY);
        }

        @Override
        public void run() {
            float t = interpolate();
            double deltaScale = calculateDeltaScale(t);
            normalizedScale = (float) (normalizedScale * deltaScale);
            onZooming((float) deltaScale);
            invalidate();
            savedMatrix.set(matrix);
            if (t < 1f) {
                // We haven't finished zooming
                compatPostOnAnimation(this, 1000 / 60);

            } else {
                // Finished zooming
                mode = NONE;
            }
        }

        /**
         * Use interpolator to get t
         */
        private float interpolate() {
            long currTime = System.currentTimeMillis();
            float elapsed = (currTime - startTime) / ZOOM_TIME;
            elapsed = Math.min(1f, elapsed);
            return interpolator.getInterpolation(elapsed);
        }

        /**
         * Interpolate the current targeted zoom and get the delta
         * from the current zoom.
         */
        private double calculateDeltaScale(float t) {
            double zoom = startZoom + t * (targetZoom - startZoom);
            return zoom / normalizedScale;
        }
    }

    /**
     * Class implement Flinging  view
     */
    private class FlingTapTranslate implements Runnable {

        private static final float TRANSLATE_TIME = 1000; //translate time units ms
        private long startTime;
        private float velocityX, velocityY;
        private AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

        FlingTapTranslate(float velocityX, float velocityY) {
            startTime = System.currentTimeMillis();
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            savedMatrix.set(matrix);
        }

        @Override
        public void run() {
            float t = interpolate();
            float[] dXY = calculateTranslateDistance(t);

            onMove(dXY[0], dXY[1]);
            savedMatrix.set(matrix);
            invalidate();
            if (t < 1f) {
                compatPostOnAnimation(this, 10);
            } else {
                //Finish translating
                mode = NONE;
            }
        }

        /**
         * Use interpolator to get t
         */
        private float interpolate() {
            long currTime = System.currentTimeMillis();
            float elapsed = (currTime - startTime) / TRANSLATE_TIME;
            elapsed = Math.min(1f, elapsed);
            return interpolator.getInterpolation(elapsed);
        }

        /**
         * Interpolate the current targeted zoom and get the delta
         * from the current zoom.
         */
        private float[] calculateTranslateDistance(float t) {
            float[] translateXY = new float[2];
            translateXY[0] = t * velocityX / 200;
            translateXY[1] = t * velocityY / 200;
            return translateXY;
        }
    }

}
