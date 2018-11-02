package com.kuky.base.android.kotlin.baseviews

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kuky.base.android.kotlin.ActivityStackManager
import com.kuky.base.android.kotlin.PermissionListener

/**
 * @author kuky.
 * @description
 */
abstract class BaseActivity : AppCompatActivity() {
    private var mPermissionListener: PermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (needTransparentStatus()) transparentStatusBar()

        setContentView(getLayoutId())
        ActivityStackManager.addActivity(this)
        initActivity(savedInstanceState)
        setListener()
        handleRxBus()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStackManager.removeActivity(this)
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initActivity(savedInstanceState: Bundle?)

    protected open fun needTransparentStatus(): Boolean = false

    protected open fun setListener() {}

    protected open fun handleRxBus() {}

    protected open fun startActivity(clazz: Class<*>) = startActivity(Intent(this, clazz))

    protected open fun transparentStatusBar() {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
        supportActionBar?.hide()
    }

    protected open fun fullScreen() {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
        supportActionBar?.hide()
    }

    protected open fun onRuntimePermissionsAsk(permissions: kotlin.Array<String>, listener: PermissionListener) {
        this.mPermissionListener = listener
        val activity = ActivityStackManager.getTopActivity()
        val deniedPermissions: MutableList<String> = mutableListOf()

        permissions
            .filterNot { ContextCompat.checkSelfPermission(activity!!, it) == PackageManager.PERMISSION_GRANTED }
            .forEach { deniedPermissions.add(it) }

        if (deniedPermissions.isEmpty())
            mPermissionListener!!.onGranted()
        else
            ActivityCompat.requestPermissions(activity!!, deniedPermissions.toTypedArray(), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            val deniedPermissions: MutableList<String> = mutableListOf()
            if (grantResults.isNotEmpty()) {
                grantResults
                    .filter { it != PackageManager.PERMISSION_GRANTED }
                    .mapTo(deniedPermissions) { permissions[it] }

                if (deniedPermissions.isEmpty())
                    mPermissionListener!!.onGranted()
                else
                    mPermissionListener!!.onDenied(deniedPermissions)
            }
        }
    }
}