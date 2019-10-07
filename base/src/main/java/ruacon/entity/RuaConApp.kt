package ruacon.entity

import android.content.Context
import com.base.util.AppUtil
import ruacon.util.RuaConUtil

class RuaConApp(var name: String, var icon: String, var link: String) {

    private var mIsInstalled: Boolean? = null
    private var mIsReceivedBonus: Boolean? = null

    val packageName: String
        get() = link.split("=".toRegex(), 0)[1]

    fun isInstall(context: Context): Boolean {
        if (mIsInstalled == null) mIsInstalled = AppUtil.isAppInstalled(context, packageName)
        return mIsInstalled!!
    }

    fun isReceivedBonus(context: Context): Boolean {
        if (mIsReceivedBonus == null) mIsReceivedBonus = RuaConUtil.isReceivedBonus(context, packageName)
        return mIsReceivedBonus!!
    }

    fun receiveBonus() {
        mIsReceivedBonus = true
    }
}