package com.diegolovera.cardindex.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import com.diegolovera.cardindex.R
import com.diegolovera.cardindex.data.models.Card
import com.diegolovera.cardindex.decrypt
import com.diegolovera.cardindex.encrypt
import com.diegolovera.cardindex.security.PasswordManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class CardFormDialog(context: Context,
                     private val listener: CardFormListener,
                     private val card: Card? = null) : Dialog(context) {
    private lateinit var mButtonOk: MaterialButton
    private lateinit var mButtonCancel: MaterialButton

    private lateinit var mTextHeader: TextView

    private lateinit var mEditTag: TextInputLayout
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

        mTextHeader = findViewById(R.id.dialog_card_text_header)

        mEditTag = findViewById(R.id.dialog_card_edit_tag)
        mEditEntity = findViewById(R.id.dialog_card_edit_entity)
        mEditType = findViewById(R.id.dialog_card_edit_type)
        mEditNumber = findViewById(R.id.dialog_card_edit_number)
        mEditValidUntil = findViewById(R.id.dialog_card_edit_valid_until)
        mEditCode = findViewById(R.id.dialog_card_edit_code)
        mEditHolderName = findViewById(R.id.dialog_card_edit_holder_name)
        mEditBrand = findViewById(R.id.dialog_card_edit_brand)

        if (card != null) {
            mTextHeader.text = context.getText(R.string.dialog_card_label_header_edit)
            mEditTag.editText?.setText(card.cardTag)
            mEditEntity.editText?.setText(card.cardEntity)
            mEditType.editText?.setText(card.cardType)
            mEditNumber.editText?.setText(card.cardNumber.decrypt(PasswordManager.userPassword))
            mEditValidUntil.editText?.setText(card.cardValidUntil)
            mEditCode.editText?.setText(card.cardCode)
            mEditHolderName.editText?.setText(card.cardHolderName)
            mEditBrand.editText?.setText(card.cardBrand)
        }

        mButtonOk.setOnClickListener {
            val cardId = card?.id ?: 0L
            listener.onCardCreated(
                Card(
                    cardId,
                    mEditTag.editText?.text.toString(),
                    mEditEntity.editText?.text.toString(),
                    mEditType.editText?.text.toString(),
                    mEditNumber.editText?.text.toString().encrypt(PasswordManager.userPassword),
                    mEditValidUntil.editText?.text.toString(),
                    mEditCode.editText?.text.toString(),
                    mEditHolderName.editText?.text.toString(),
                    mEditBrand.editText?.text.toString(),
                    cardLocked = true
                )
            )
            dismiss()
        }

        mButtonCancel.setOnClickListener {
            dismiss()
        }
    }

    interface CardFormListener {
        fun onCardCreated(cardCrated: Card)
    }
}
