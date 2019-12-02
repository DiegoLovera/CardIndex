package com.diegolovera.cardindex

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class CardFormDialog(context: Context, private val listener: CardFormListener) : Dialog(context) {
    private lateinit var mButtonOk: MaterialButton
    private lateinit var mButtonCancel: MaterialButton

    private lateinit var mEditEntity: TextInputLayout
    private lateinit var mEditType: TextInputLayout
    private lateinit var mEditNumber: TextInputLayout
    private lateinit var mEditValidUntil: TextInputLayout
    private lateinit var mEditCode: TextInputLayout
    private lateinit var mEditHolderName: TextInputLayout
    private lateinit var mEditBrand: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_card_form)

        mButtonOk = findViewById(R.id.dialog_card_button_ok)
        mButtonCancel = findViewById(R.id.dialog_card_button_cancel)

        mEditEntity = findViewById(R.id.dialog_card_edit_entity)
        mEditType = findViewById(R.id.dialog_card_edit_type)
        mEditNumber = findViewById(R.id.dialog_card_edit_number)
        mEditValidUntil = findViewById(R.id.dialog_card_edit_valid_until)
        mEditCode = findViewById(R.id.dialog_card_edit_code)
        mEditHolderName = findViewById(R.id.dialog_card_edit_holder_name)
        mEditBrand = findViewById(R.id.dialog_card_edit_brand)

        mButtonOk.setOnClickListener {
            listener.onCardCreated(
                Card(mEditEntity.editText?.text.toString(),
                    mEditType.editText?.text.toString(),
                    mEditNumber.editText?.text.toString(),
                    mEditValidUntil.editText?.text.toString(),
                    mEditCode.editText?.text.toString(),
                    mEditHolderName.editText?.text.toString(),
                    mEditBrand.editText?.text.toString()))
            dismiss()
        }
        mButtonCancel.setOnClickListener {
            dismiss()
        }
    }
}
