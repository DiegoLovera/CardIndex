package com.diegolovera.cardindex.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.diegolovera.cardindex.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class PasswordDialog(context: Context,
                     private val listener: PasswordFormListener? = null) : Dialog(context) {
    private lateinit var mButtonOk: MaterialButton
    private lateinit var mButtonCancel: MaterialButton
    private lateinit var mEditPassword: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_password)

        mButtonOk = findViewById(R.id.dialog_password_button_ok)
        mButtonCancel = findViewById(R.id.dialog_password_button_cancel)
        mEditPassword = findViewById(R.id.dialog_password_edit_password)

        mButtonOk.setOnClickListener {
            if (mEditPassword.editText?.text != null) {
                val password: String = mEditPassword.editText?.text.toString()
                if (password.isNotEmpty() && password.length > 2) {
                    listener?.onPasswordSet(password)
                    mEditPassword.isErrorEnabled = false
                    dismiss()
                } else {
                    mEditPassword.isErrorEnabled = true
                    mEditPassword.error = "The password is too short"
                }
            } else {
                mEditPassword.isErrorEnabled = true
                mEditPassword.error = "Empty password"
            }
        }

        mButtonCancel.setOnClickListener { dismiss() }
    }

    interface PasswordFormListener {
        fun onPasswordSet(password: String)
    }
}