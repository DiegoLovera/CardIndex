package com.diegolovera.cardindex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardListAdapter(context: Context, private var mCharacterList: List<Card>?)
    : RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CardViewHolder {
        val view = mInflater.inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = mCharacterList!![position]
        holder.mTextEntity.text = card.cardEntity
        holder.mTextType.text = card.cardType

        holder.mTextNumber1.text = card.cardNumber.substring(0, 4)
        holder.mTextNumber2.text = card.cardNumber.substring(4, 8)
        holder.mTextNumber3.text = card.cardNumber.substring(8, 12)
        holder.mTextNumber4.text = card.cardNumber.substring(12, 16)

        holder.mTextValidUntil.text = card.cardValidUntil
        holder.mTextCvv.text = card.cardCvv
        holder.mTextHolderName.text = card.cardHolderName
        holder.mTextBrand.text = card.cardBrand
        /*holder.mCardCharacter.setOnClickListener {
            val intent = Intent(context, CharacterActivity::class.java)
            intent.putExtra(CharacterActivity.CHARACTER_ID, character.id)
            context.startActivity(intent)
        }*/
    }

    override fun getItemCount(): Int {
        return mCharacterList!!.size
    }

    fun setData(data: List<Card>) {
        mCharacterList = data
        notifyDataSetChanged()
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTextEntity: TextView = itemView.findViewById(R.id.item_card_text_entity)
        var mTextType: TextView = itemView.findViewById(R.id.item_card_text_type)

        var mTextNumber1: TextView = itemView.findViewById(R.id.item_card_text_number_1)
        var mTextNumber2: TextView = itemView.findViewById(R.id.item_card_text_number_2)
        var mTextNumber3: TextView = itemView.findViewById(R.id.item_card_text_number_3)
        var mTextNumber4: TextView = itemView.findViewById(R.id.item_card_text_number_4)

        var mTextValidUntil: TextView = itemView.findViewById(R.id.item_card_text_valid_until)
        var mTextCvv: TextView = itemView.findViewById(R.id.item_card_text_cvv)
        var mTextHolderName: TextView = itemView.findViewById(R.id.item_card_text_holder_name)
        var mTextBrand: TextView = itemView.findViewById(R.id.item_card_text_brand)
    }
}