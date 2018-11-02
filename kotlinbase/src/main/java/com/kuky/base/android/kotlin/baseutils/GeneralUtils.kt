package com.kuky.base.android.kotlin.baseutils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat

/**
 * @author kuky.
 * @description
 */
object GeneralUtils {

    const val TEXT_VIEW_LEFT_DRAWABLE = "com.base.text.left"
    const val TEXT_VIEW_TOP_DRAWABLE = "com.base.text.top"
    const val TEXT_VIEW_RIGHT_DRAWABLE = "com.base.text.right"
    const val TEXT_VIEW_BOTTOM_DRAWABLE = "com.base.text.bottom"

    @JvmStatic
    fun setTextDrawable(context: Context, view: TextView, drawableRes: Int, direction: String) {
        val drawable = context.resources.getDrawable(drawableRes, null)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        when (direction) {
            TEXT_VIEW_LEFT_DRAWABLE -> view.setCompoundDrawables(drawable, null, null, null)
            TEXT_VIEW_TOP_DRAWABLE -> view.setCompoundDrawables(null, drawable, null, null)
            TEXT_VIEW_RIGHT_DRAWABLE -> view.setCompoundDrawables(null, null, drawable, null)
            TEXT_VIEW_BOTTOM_DRAWABLE -> view.setCompoundDrawables(null, null, null, drawable)
            else -> {
            }
        }
    }

    /**
     * #.## 表示会去掉小数点后最后的0 例如 1.20 就会变成1.2
     * 0.00 表示不会去掉小数点后的0 例如 1.20 依然是 1.20

    @kotlin.jvm.JvmStatic */
    fun getSavePoint(double: Double, format: String): String {
        return DecimalFormat(format).format(double)
    }

    @JvmStatic
    fun showSoftInput(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }

    @JvmStatic
    fun forceHideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    @JvmStatic
    fun compressBitmap(path: String, rqsWidth: Int, rqsHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        val outWidth = options.outWidth
        val outHeight = options.outHeight
        var size = (outWidth * outHeight) / (rqsWidth * rqsHeight)
        if (size % 2 != 0) {
            size++
        }
        options.inSampleSize = size
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    @JvmStatic
    fun compressBitmapForNewGoods(path: String, rqsWidth: Int, rqsHeight: Int, compressSize: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        val outWidth = options.outWidth
        val outHeight = options.outHeight
        options.inSampleSize = calculateInSampleSize(options, rqsWidth, rqsHeight, outWidth, outHeight) * compressSize
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, rqsWidth: Int, rqsHeight: Int, width: Int, height: Int): Int {
        var inSampleSize = 1

        if (rqsWidth == 0 || rqsHeight == 0) {
            return 1
        }

        if (height > rqsHeight || width > rqsWidth) {
            val widthRatio: Int = Math.round(width.toFloat() / rqsWidth.toFloat())
            val heightRatio: Int = Math.round(height.toFloat() / rqsHeight.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }

    @JvmStatic
    fun bitmapQualityCompress(bitmap: Bitmap): Bitmap? {
        val baos = ByteArrayOutputStream()
        var options = 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        while (baos.toByteArray().size / 1024 > 100) {
            baos.reset()
            options -= 10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos)
        }

        val bais = ByteArrayInputStream(baos.toByteArray())
        return BitmapFactory.decodeStream(bais, null, null)
    }

    @JvmStatic
    fun getScaledBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true)
    }

    @JvmStatic
    fun getVideoThumbnail(videoPath: String): Bitmap {
        val media = MediaMetadataRetriever()
        media.setDataSource(videoPath)
        return media.frameAtTime
    }

    @JvmStatic
    fun startAnotherApp(context: Context, packageName: String) {
        var packageInfo: PackageInfo? = null

        try {
            packageInfo = (context as Activity).packageManager.getPackageInfo(packageName, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (packageInfo == null) return

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.`package` = packageInfo.packageName

        val resolveInfoList = context.packageManager.queryIntentActivities(intent, 0)
        val resolveInfo = resolveInfoList.iterator().next()

        if (resolveInfo != null) {
            val packName = resolveInfo.activityInfo.packageName
            val className = resolveInfo.activityInfo.name
            val startIntent = Intent(Intent.ACTION_MAIN)
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER)

            startIntent.component = ComponentName(packName, className)
            context.startActivity(startIntent)
        }
    }
}