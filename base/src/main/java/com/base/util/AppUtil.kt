package com.base.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri


/**
 * Created by on 17/05/2016.
 */
object AppUtil {
    private const val INTENT_TYPE_TEXT = "text/plain"

    fun getAppPackage(context: Context): String {
        return context.applicationContext.packageName
    }

    fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(stringId)
    }

    fun openAppInPlayStore(context: Context, appPackageName: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            LogUtil.d("ActivityNotFoundException $anfe")
        }
    }

    fun openAppInPlayStore(context: Context) {
        val appPackageName = getAppPackage(context)
        openAppInPlayStore(context, appPackageName)
    }

    fun openRuaConStore(context: Context) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=R%C3%B9a+Con+Studio")))
    }

    fun openApp(context: Context, packageName: String) {
        try {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
            context.startActivity(launchIntent)
        } catch (ex: ActivityNotFoundException) {
            LogUtil.d("ActivityNotFoundException $ex")
        }
    }

    fun openMailApp(context: Context, dialogTitle: String, email: String, subject: String, text: String) {
        val intentMail = Intent(Intent.ACTION_SENDTO)
        intentMail.data = Uri.parse(email)
        intentMail.putExtra(Intent.EXTRA_SUBJECT, subject)
        intentMail.putExtra(Intent.EXTRA_TEXT, text)
        try {
            context.startActivity(Intent.createChooser(intentMail, dialogTitle))
        } catch (ex: ActivityNotFoundException) {
            LogUtil.d("ActivityNotFoundException $ex")
        }
    }

    fun shareAll(context: Context, dialogTitle: String, content: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, content)
        intent.type = INTENT_TYPE_TEXT
        context.startActivity(Intent.createChooser(intent, dialogTitle))
    }

    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}