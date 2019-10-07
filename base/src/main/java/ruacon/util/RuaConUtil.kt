package ruacon.util

import android.content.Context
import android.preference.PreferenceManager
import android.text.TextUtils
import com.base.util.AppUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object RuaConUtil {

    private const val KEY_LIST_PENDING_INSTALL_APPS = "u8u987uh7g3g56t7y47u785tg4y"
    private const val KEY_LIST_RECEIVED_BONUS_APPS = "883mm4g4wn93ml6ls9m3bsi3m39"

    fun addPendingInstall(context: Context, packageApp: String) {
        val listPendingApps = getListApps(context, KEY_LIST_PENDING_INSTALL_APPS)
        if (!listPendingApps.contains(packageApp)) {
            listPendingApps.add(packageApp)
            saveListApps(context, KEY_LIST_PENDING_INSTALL_APPS, listPendingApps)
        }
    }

    fun addReceiveBonus(context: Context, packageApp: String) {
        val listReceivedBonus = getListApps(context, KEY_LIST_RECEIVED_BONUS_APPS)
        if (!listReceivedBonus.contains(packageApp)) {
            listReceivedBonus.add(packageApp)
            saveListApps(context, KEY_LIST_RECEIVED_BONUS_APPS, listReceivedBonus)
        }
    }

    fun getNumberPointFromInstallBonusApp(context: Context): Int {
        val listPendingApps = getListApps(context, KEY_LIST_PENDING_INSTALL_APPS)
        if (listPendingApps.size == 0) return 0
        val listReceivedBonus = getListApps(context, KEY_LIST_RECEIVED_BONUS_APPS)
        var count = 0
        for (i in (listPendingApps.size - 1)..0) {
            val app = listPendingApps[i]
            if (!listReceivedBonus.contains(app) && AppUtil.isAppInstalled(context, app)) {
                count++
                listReceivedBonus.add(app)
                listPendingApps.removeAt(i)
            }
        }

        saveListApps(context, KEY_LIST_PENDING_INSTALL_APPS, listPendingApps)
        saveListApps(context, KEY_LIST_RECEIVED_BONUS_APPS, listReceivedBonus)
        return count
    }

    fun isReceivedBonus(context: Context, packageApp: String): Boolean {
        val listReceivedBonus = getListApps(context, KEY_LIST_RECEIVED_BONUS_APPS)
        return listReceivedBonus.contains(packageApp)
    }

    private fun saveListApps(context: Context, key: String, list: ArrayList<String>) {
        val json = Gson().toJson(list)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(key, json)
        editor.apply()
    }

    private fun getListApps(context: Context, key: String): ArrayList<String> {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val json = preferences.getString(key, "")
        if (TextUtils.isEmpty(json)) return ArrayList()
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(json, type) as ArrayList<String>
    }
}