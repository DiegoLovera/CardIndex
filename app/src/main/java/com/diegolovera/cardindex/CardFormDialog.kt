package com.diegolovera.cardindex

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window

class CardFormDialog(context: Context) : Dialog(context) {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_card_form)

    }
}
