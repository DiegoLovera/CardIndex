package com.diegolovera.cardindex.ui

import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.diegolovera.cardindex.security.PasswordManager
import com.diegolovera.cardindex.security.SystemServices
import com.diegolovera.cardindex.ui.dialogs.PasswordDialog

abstract class BaseSecureActivity : AppCompatActivity() {

    private val systemServices by lazy(LazyThreadSafetyMode.NONE) {
        SystemServices(
            this
        )
    }

    private var deviceSecurityAlert: AlertDialog? = null

    override fun onStart() {
        super.onStart()
        if (!systemServices.isDeviceSecure()) {
            deviceSecurityAlert = systemServices.showDeviceSecurityAlert()
        }
        if (PasswordManager.userPassword.isEmpty()) {
            showPasswordMissing()
        }
    }

    override fun onStop() {
        super.onStop()
        deviceSecurityAlert?.dismiss()
    }

    fun showPasswordMissing() {
        val dialog = PasswordDialog(this,
            object : PasswordDialog.PasswordFormListener {
                override fun onPasswordSet(password: String) {
                    PasswordManager.userPassword = password
                }
            })
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun showPasswordMissing(listener: PasswordDialog.PasswordFormListener) {
        val dialog = PasswordDialog(this, listener)
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}