package ruacon

import android.content.Context
import android.content.DialogInterface
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.base.R
import com.base.util.AppUtil
import ruacon.constant.MenuAction


/**
 * Created by Nguyen Tien Hoang on 2018-04-27.
 */
object MenuUtil {

    fun action(context: Context, action: MenuAction) {
        when (action) {
            MenuAction.PRIVACY -> showMessageDialog(context, R.string.privacy_and_licence, R.string.privacy_content, null)
            MenuAction.ABOUT -> showMessageDialog(context, R.string.about_us, R.string.about_us_content, null)
            MenuAction.SHARE -> AppUtil.shareAll(context, context.getString(R.string.share_app), context.getString(R.string.message_share, AppUtil.getAppPackage(context)))
            MenuAction.FEEDBACK -> AppUtil.openMailApp(context, context.getString(R.string.send_feedback), context.getString(R.string.email_feedback), context.getString(R.string.subject_feedback, AppUtil.getApplicationName(context)), context.getString(R.string.text_feedback))
            MenuAction.MORE_APP -> AppUtil.openRuaConStore(context)
            else -> AppUtil.openAppInPlayStore(context)
        }
    }

    private fun showMessageDialog(context: Context, resTitle: Int, resMessage: Int, okListener: DialogInterface.OnClickListener?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(resTitle)
                .setIcon(R.drawable.logo_rua_con_studio)
                .setMessage(resMessage)
                .setPositiveButton(android.R.string.ok, okListener)
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.findViewById<TextView>(android.R.id.message)?.movementMethod = LinkMovementMethod.getInstance()
    }
}