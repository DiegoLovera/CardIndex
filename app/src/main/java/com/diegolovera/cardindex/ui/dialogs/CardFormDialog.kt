package com.diegolovera.cardindex.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import com.diegolovera.cardindex.R
import com.diegolovera.cardindex.data.models.Card
import com.diegolovera.cardindex.decrypt
import com.diegolovera.cardindex.encrypt
import com.diegolovera.cardindex.security.PasswordManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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

    private lateinit var mChipGroupColor: ChipGroup
    private lateinit var mChipColor1: Chip
    private lateinit var mChipColor2: Chip
    private lateinit var mChipColor3: Chip
    private lateinit var mChipColor4: Chip
    private lateinit var mChipColor5: Chip

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

        mChipGroupColor = findViewById(R.id.dialog_card_chip_colors)
        mChipColor1 = findViewById(R.id.dialog_card_chip_color_1)
        mChipColor1.chipBackgroundColor = getColorStateList(context, R.color.cardColorOption1)
        mChipColor2 = findViewById(R.id.dialog_card_chip_color_2)
        mChipColor2.chipBackgroundColor = getColorStateList(context, R.color.cardColorOption2)
        mChipColor3 = findViewById(R.id.dialog_card_chip_color_3)
        mChipColor3.chipBackgroundColor = getColorStateList(context, R.color.cardColorOption3)
        mChipColor4 = findViewById(R.id.dialog_card_chip_color_4)
        mChipColor4.chipBackgroundColor = getColorStateList(context, R.color.cardColorOption4)
        mChipColor5 = findViewById(R.id.dialog_card_chip_color_5)
        mChipColor5.chipBackgroundColor = getColorStateList(context, R.color.cardColorOption5)


        if (card != null) {
            mTextHeader.text = context.getText(R.string.dialog_card_label_header_edit)
            mEditTag.editText?.setText(card.cardTag)
            mEditEntity.editText?.setText(card.cardEntity)
            mEditType.editText?.setText(card.cardType)
            mEditNumber.editText?.setText(card.cardNumber.decrypt(PasswordManager.userPassword))
            mEditValidUntil.editText?.setText(card.cardValidUntil.decrypt(PasswordManager.userPassword))
            mEditCode.editText?.setText(card.cardCode.decrypt(PasswordManager.userPassword))
            mEditHolderName.editText?.setText(card.cardHolderName)
            mEditBrand.editText?.setText(card.cardBrand)
            val checkedId = when (card.cardColor) {
                R.color.cardColorOption1 -> {
                    R.id.dialog_card_chip_color_1
                }
                R.color.cardColorOption2 -> {
                    R.id.dialog_card_chip_color_2
                }
                R.color.cardColorOption3 -> {
                    R.id.dialog_card_chip_color_3
                }
                R.color.cardColorOption4 -> {
                    R.id.dialog_card_chip_color_4
                }
                R.color.cardColorOption5 -> {

                    R.id.dialog_card_chip_color_5
                }
                else -> {
                    View.NO_ID
                }
            }
            mChipGroupColor.check(checkedId)
        }

        mButtonOk.setOnClickListener {
            val cardId = card?.id ?: 0L
            if (validateForm()) {
                val colorInt = when (mChipGroupColor.checkedChipId) {
                    R.id.dialog_card_chip_color_1 -> {
                        R.color.cardColorOption1
                    }
                    R.id.dialog_card_chip_color_2 -> {
                        R.color.cardColorOption2
                    }
                    R.id.dialog_card_chip_color_3 -> {
                        R.color.cardColorOption3
                    }
                    R.id.dialog_card_chip_color_4 -> {
                        R.color.cardColorOption4
                    }
                    R.id.dialog_card_chip_color_5 -> {
                        R.color.cardColorOption5
                    }
                    else -> {
                        0
                    }
                }

                listener.onCardCreated(
                    Card(
                        cardId,
                        mEditTag.editText?.text.toString(),
                        mEditEntity.editText?.text.toString(),
                        mEditType.editText?.text.toString(),
                        mEditNumber.editText?.text.toString().encrypt(PasswordManager.userPassword),
                        mEditValidUntil.editText?.text.toString().encrypt(PasswordManager.userPassword),
                        mEditCode.editText?.text.toString().encrypt(PasswordManager.userPassword),
                        mEditHolderName.editText?.text.toString(),
                        mEditBrand.editText?.text.toString(),
                        cardLocked = true,
                        cardColor = colorInt
                    )
                )
                dismiss()
            }
        }

        mButtonCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun validateForm(): Boolean {
        if (mEditNumber.editText?.text.toString().length != 16) {
            mEditNumber.error = context.getString(R.string.error_invalid_card_number_length)
            return false
        }
        if (mEditValidUntil.editText?.text.toString().isEmpty()
            || mEditValidUntil.editText?.text.toString().length > 5) {
            mEditValidUntil.error = context.getString(R.string.error_invalid_date_length)
            return false
        } else {
            if (!mEditValidUntil.editText?.text.toString()
                    .matches(Regex("[0-9][0-9]+/[0-9][0-9]"))) {
                mEditValidUntil.error = context.getString(R.string.error_invalid_date_format)
                return false
            }
        }
        if (mEditCode.editText?.text.toString().isEmpty()
            || mEditCode.editText?.text.toString().length > 4) {
            mEditCode.error = context.getString(R.string.error_invalid_security_code_length)
            return false
        }
        return true
    }

    interface CardFormListener {
        fun onCardCreated(cardCrated: Card)
    }
}
