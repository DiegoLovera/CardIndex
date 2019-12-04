package com.diegolovera.cardindex

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.diegolovera.cardindex.security.EncryptionUtility

fun Context.openLockScreenSettings() {
    val intent = Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD)
    startActivity(intent)
}

fun Context.openSecuritySettings() {
    val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
    startActivity(intent)
}

fun String.encrypt(key: String): String {
    return EncryptionUtility.encrypt(this, key)
}

fun String.decrypt(key: String): String {
    return EncryptionUtility.decrypt(this, key)
}