package com.diegolovera.cardindex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.diegolovera.swipelayout.SwipeLayout

class CardListAdapter(private val context: Context, private var mCharacterList: ArrayList<Card>?)
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

    fun setData(data: ArrayList<Card>) {
        mCharacterList = data
        notifyDataSetChanged()
    }

    private fun removeAt(position: Int) {
        mCharacterList!!.removeAt(position)
        notifyDataSetChanged()
        //notifyItemRemoved(position)
        //notifyItemRangeChanged(position, mCharacterList!!.size)
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mSwipeLayout: SwipeLayout = itemView.findViewById(R.id.item_card_swipelayout)

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
                mTextNumber1.visibility = View.INVISIBLE
                mTextNumber2.visibility = View.INVISIBLE
                mTextNumber3.visibility = View.INVISIBLE
                mTextNumber4.visibility = View.INVISIBLE

                mTextCvv.visibility = View.INVISIBLE
                mTextValidUntil.visibility = View.INVISIBLE
            }
            mButtonEdit.setOnClickListener {

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
    }
}