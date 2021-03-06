package com.diegolovera.cardindex.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.diegolovera.cardindex.R
import com.diegolovera.cardindex.data.models.Card
import com.diegolovera.cardindex.decrypt
import com.diegolovera.cardindex.encrypt
import com.diegolovera.cardindex.security.PasswordManager
import com.diegolovera.cardindex.ui.activites.MainViewModel
import com.diegolovera.cardindex.ui.dialogs.CardFormDialog
import com.diegolovera.cardindex.ui.dialogs.PasswordDialog
import com.diegolovera.swipelayout.SwipeLayout
import com.google.android.material.card.MaterialCardView
import javax.crypto.BadPaddingException


class CardListAdapter(private val context: Context,
                      private var mCharacterList: List<Card>?,
                      private val mMainViewModel: MainViewModel)
    : RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CardViewHolder {
        val view = mInflater.inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = mCharacterList!![position]
        holder.setIsRecyclable(false)

        if (card.cardColor != 0) {
            holder.mCardBackground.background.setTint(context.getColor(card.cardColor))
        }
        holder.mTextTag.text = card.cardTag
        holder.mTextEntity.text = card.cardEntity
        holder.mTextType.text = card.cardType

        holder.mTextNumber1.text = card.cardNumber.substring(0, 4)
        holder.mTextNumber2.text = card.cardNumber.substring(4, 8)
        holder.mTextNumber3.text = card.cardNumber.substring(8, 12)
        holder.mTextNumber4.text = card.cardNumber.substring(12, 16)

        holder.mTextValidUntil.text = card.cardValidUntil
        holder.mTextCvv.text = card.cardCode
        holder.mTextHolderName.text = card.cardHolderName
        holder.mTextBrand.text = card.cardBrand
    }

    override fun getItemCount(): Int {
        return mCharacterList!!.size
    }

    fun setData(data: List<Card>) {
        mCharacterList = data
        notifyDataSetChanged()
    }

    private fun removeAt(position: Int) {
        mMainViewModel.removeCard(mCharacterList!![position])
        notifyDataSetChanged()
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCardBackground: MaterialCardView = itemView.findViewById(R.id.item_card_background)
        var mSwipeLayout: SwipeLayout = itemView.findViewById(R.id.item_card_swipelayout)

        var mTextTag: TextView = itemView.findViewById(R.id.item_card_text_tag)
        var mTextEntity: TextView = itemView.findViewById(R.id.item_card_text_entity)
        var mTextType: TextView = itemView.findViewById(R.id.item_card_text_type)

        var mTextNumber1: TextView = itemView.findViewById(R.id.item_card_text_number_1)
        var mTextNumber2: TextView = itemView.findViewById(R.id.item_card_text_number_2)
        var mTextNumber3: TextView = itemView.findViewById(R.id.item_card_text_number_3)
        var mTextNumber4: TextView = itemView.findViewById(R.id.item_card_text_number_4)

        var mTextValidUntil: TextView = itemView.findViewById(R.id.item_card_text_valid_until)
        var mTextCvv: TextView = itemView.findViewById(R.id.item_card_text_code)
        var mTextHolderName: TextView = itemView.findViewById(R.id.item_card_text_holder_name)
        var mTextBrand: TextView = itemView.findViewById(R.id.item_card_text_brand)

        var mButtonLock: AppCompatImageButton = itemView.findViewById(R.id.item_card_button_lock)
        var mButtonEdit: AppCompatImageButton = itemView.findViewById(R.id.item_card_button_edit)
        var mButtonDelete: AppCompatImageButton = itemView.findViewById(R.id.item_card_button_delete)
        var mButtonShare: AppCompatImageButton = itemView.findViewById(R.id.item_card_button_share)

        init {
            mButtonLock.setOnClickListener {
                if (PasswordManager.userPassword.isEmpty()) {
                    val dialog = PasswordDialog(context,
                        object : PasswordDialog.PasswordFormListener {
                            override fun onPasswordSet(password: String) {
                                PasswordManager.userPassword = password
                                encryptDecryptData()
                            }
                        })
                    dialog.show()
                    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                } else {
                    encryptDecryptData()
                }
            }
            mButtonEdit.setOnClickListener {
                if (PasswordManager.userPassword.isEmpty()) {
                    val dialog = PasswordDialog(context,
                        object : PasswordDialog.PasswordFormListener {
                            override fun onPasswordSet(password: String) {
                                PasswordManager.userPassword = password
                                editData()
                            }
                        })
                    dialog.show()
                    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                } else {
                    editData()
                }
            }
            mButtonDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage(context.getString(R.string.alert_dialog_delete_card))
                    .setCancelable(false)
                    .setPositiveButton(context.getString(R.string.button_yes)) { _, _ ->
                        removeAt(adapterPosition)
                    }
                    .setNegativeButton(context.getString(R.string.button_no)) { dialog, _ ->
                        dialog.cancel() }
                val alert = builder.create()
                alert.show()
            }
            mButtonShare.setOnClickListener {
                shareData()
            }
        }

        private fun encryptDecryptData() {
            try {
                val card = mCharacterList!![adapterPosition]
                val cardNumber: String
                val cardValidUntil: String
                val cardCode: String
                if (card.cardLocked) {
                    cardNumber = card.cardNumber.decrypt(PasswordManager.userPassword)
                    cardValidUntil = card.cardValidUntil.decrypt(PasswordManager.userPassword)
                    cardCode = card.cardCode.decrypt(PasswordManager.userPassword)
                    card.cardLocked = false
                } else {
                    cardNumber = card.cardNumber.encrypt(PasswordManager.userPassword)
                    cardValidUntil = card.cardValidUntil.encrypt(PasswordManager.userPassword)
                    cardCode = card.cardCode.encrypt(PasswordManager.userPassword)
                    card.cardLocked = true
                }

                mTextNumber1.text = cardNumber.substring(0, 4)
                mTextNumber2.text = cardNumber.substring(4, 8)
                mTextNumber3.text = cardNumber.substring(8, 12)
                mTextNumber4.text = cardNumber.substring(12, 16)

                mTextCvv.text = cardCode
                mTextValidUntil.text = cardValidUntil

                mSwipeLayout.close(true)
            } catch (ex: BadPaddingException) {
                mSwipeLayout.close(true)
                Toast.makeText(context,
                    context.getString(R.string.error_password_doesnt_match),
                    Toast.LENGTH_LONG).show()
            }
        }

        private fun editData() {
            try {
                val cardDialog = CardFormDialog(
                    context,
                    object : CardFormDialog.CardFormListener {
                        override fun onCardCreated(cardCrated: Card) {
                            mMainViewModel.addCard(cardCrated)
                            mSwipeLayout.close(smooth = true)
                        }
                    },
                    mCharacterList!![adapterPosition])
                cardDialog.setOnDismissListener {
                    mSwipeLayout.close(smooth = true)
                }
                cardDialog.show()
                cardDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                cardDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            } catch (ex: BadPaddingException) {
                mSwipeLayout.close(true)
                Toast.makeText(context,
                    context.getString(R.string.error_password_doesnt_match),
                    Toast.LENGTH_LONG).show()
            }
        }

        private fun shareData() {
            try {
                val cardNumber: String = mCharacterList!![adapterPosition].cardNumber.decrypt(PasswordManager.userPassword)
                val cardValidUntil: String = mCharacterList!![adapterPosition].cardValidUntil.decrypt(PasswordManager.userPassword)
                val cardCode: String = mCharacterList!![adapterPosition].cardCode.decrypt(PasswordManager.userPassword)
                var textToShare = "Card Number: $cardNumber\n"
                textToShare += "Valid until: $cardValidUntil\n"
                textToShare += "Security code: $cardCode"

                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
                sendIntent.type = "text/plain"
                context.startActivity(Intent.createChooser(sendIntent, "Share"))
                mSwipeLayout.close(true)
            } catch (ex: BadPaddingException) {
                mSwipeLayout.close(true)
                Toast.makeText(context,
                    context.getString(R.string.error_password_doesnt_match),
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}