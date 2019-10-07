package ruacon

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Point

/**
 * Created by Nguyen Tien HOANG on 2018-01-25.
 */
object AppConfig {
    var screenWidth: Int = 0
    var screenHeight: Int = 0

    fun init(activity: Activity) {
        if (screenWidth <= 0 || screenHeight <= 0) {
            val w = activity.windowManager
            val size = Point()
            w.defaultDisplay.getSize(size)
            if (activity.resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                screenWidth = size.y
                screenHeight = size.x
            } else {
                screenWidth = size.x
                screenHeight = size.y
            }
        }
    }
}