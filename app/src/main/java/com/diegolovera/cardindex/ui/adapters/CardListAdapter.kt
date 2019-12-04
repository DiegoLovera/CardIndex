package com.diegolovera.cardindex.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.diegolovera.cardindex.security.PasswordManager
import com.diegolovera.cardindex.R
import com.diegolovera.cardindex.data.models.Card
import com.diegolovera.cardindex.decrypt
import com.diegolovera.cardindex.encrypt
import com.diegolovera.cardindex.security.EncryptionUtility
import com.diegolovera.cardindex.ui.activites.MainViewModel
import com.diegolovera.cardindex.ui.dialogs.CardFormDialog
import com.diegolovera.cardindex.ui.dialogs.PasswordDialog
import com.diegolovera.swipelayout.SwipeLayout
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
                builder.setMessage("Are you sure you want to delete this card?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        removeAt(adapterPosition)
                    }
                    .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
                val alert = builder.create()
                alert.show()
            }
        }

        private fun encryptDecryptData() {
            try {
                val card = mCharacterList!![adapterPosition]
                val data: String
                if (card.cardLocked) {
                    data = card.cardNumber.decrypt(PasswordManager.userPassword)
                    card.cardLocked = false
                } else {
                    data = card.cardNumber.encrypt(PasswordManager.userPassword)
                    card.cardLocked = true
                }

                mTextNumber1.text = data.substring(0, 4)
                mTextNumber2.text = data.substring(4, 8)
                mTextNumber3.text = data.substring(8, 12)
                mTextNumber4.text = data.substring(12, 16)

                mSwipeLayout.close(true)
            } catch (ex: BadPaddingException) {
                mSwipeLayout.close(true)
                Toast.makeText(context,
                    "The actual password does'nt match with the one that was used to save this card.",
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
                    "The actual password does'nt match with the one that was used to save this card.",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}