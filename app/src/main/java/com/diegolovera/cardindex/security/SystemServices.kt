package com.diegolovera.cardindex.security

import android.annotation.TargetApi
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AlertDialog
import com.diegolovera.cardindex.BuildConfig
import com.diegolovera.cardindex.R
import com.diegolovera.cardindex.openLockScreenSettings
import kotlin.system.exitProcess

@TargetApi(Build.VERSION_CODES.M)
class SystemServices(private val context: Context) {

    companion object {
        fun hasMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private val keyguardManager: KeyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    fun isDeviceSecure(): Boolean = if (hasMarshmallow()) keyguardManager.isDeviceSecure else keyguardManager.isKeyguardSecure

    fun showAuthenticationScreen(activity: Activity, requestCode: Int, title: String? = null, description: String? = null) {
        // Create the Confirm Credentials screen. You can customize the title and description. Or
        // we will provide a generic one for you if you leave it null
        if (hasMarshmallow()) {
            val intent = keyguardManager.createConfirmDeviceCredentialIntent(title, description)
            if (intent != null) {
                activity.startActivityForResult(intent, requestCode)
            }
        }
    }

    fun showDeviceSecurityAlert(): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.alert_dialog_security_header))
            .setMessage(context.getString(R.string.alert_dialog_security_body))
            .setPositiveButton(context.getString(R.string.alert_dialog_security_ok)) { _, _ -> context.openLockScreenSettings() }
            .setNegativeButton(context.getString(R.string.alert_dialog_security_cancel)) { _, _ -> exitProcess(0) }
            .setCancelable(BuildConfig.DEBUG)
            .show()
    }
}