package com.kuky.base.android.kotlin.baseutils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build

/**
 * @author kuky.
 * @description
 */
object ApplicationUtils {

    @JvmStatic
    fun getAppName(context: Context): String {
        try {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    @JvmStatic
    fun getAppVersionName(context: Context): String {
        try {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    @JvmStatic
    fun getAppVersionCode(context: Context): Int {
        try {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    @JvmStatic
    fun getAppIcon(context: Context, pkgName: String): Bitmap? {
        val pm = context.packageManager
        try {
            val drawable = pm.getApplicationIcon(pkgName)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                return (drawable as BitmapDrawable).bitmap
            else
                when (drawable) {
                    is BitmapDrawable -> return drawable.bitmap

                    is AdaptiveIconDrawable -> {
                        val layerDrawable = LayerDrawable(arrayOf(drawable.background, drawable.foreground))
                        val width = layerDrawable.intrinsicWidth
                        val height = layerDrawable.intrinsicHeight
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(bitmap)
                        layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
                        layerDrawable.draw(canvas)
                        return bitmap
                    }
                }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}