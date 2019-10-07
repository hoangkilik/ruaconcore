package com.base.util;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * Created by Bui Xuan VU on 6/15/2016.
 */
public class UIHelper {
    private static final String TAG = UIHelper.class.getSimpleName();

    /**
     * get display size
     *
     * @param context: Context
     * @return: point(display size)
     */
    public static Point getDisplaySize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        windowManager.getDefaultDisplay().getSize(outSize);
        return outSize;
    }

    /**
     * get display shorter side size
     *
     * @param context: Context
     * @return size of display shorter side
     */
    public static int getDisplayShorterSideSize(Context context) {
        Point outSize = getDisplaySize(context);
        return outSize.x < outSize.y ? outSize.x : outSize.y;
    }

    /**
     * calculate column of grid with cell width, will use when display image in grid view
     *
     * @param context:   Context
     * @param cellWidth: cell width
     * @return: number of column
     */
    public static int calcGridColumn(Context context, int cellWidth) {
        Point displaySize = getDisplaySize(context);
        int width = displaySize.x;
        int numColumn = width / cellWidth;
        LogUtil.d("NumColumn: ", numColumn, width);
        return numColumn;
    }
}
