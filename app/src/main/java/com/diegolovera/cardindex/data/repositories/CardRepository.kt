package com.diegolovera.cardindex.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.diegolovera.cardindex.data.CardIndexDatabase
import com.diegolovera.cardindex.data.daos.CardDao
import com.diegolovera.cardindex.data.models.Card

class CardRepository(context: Context) {
    private var db: CardIndexDatabase = CardIndexDatabase.invoke(context)
    private var cardDao: CardDao

    init {
        cardDao = db.cardDao()
    }

    fun getAllCards(): LiveData<List<Card>> {
        return cardDao.getAll()
    }

    private fun insertCard(card: Card): Long {
        return cardDao.insert(card)
    }

    private fun updateCard(card: Card) {
        cardDao.update(card)
    }

    fun upsertCard(card: Card) {
        val status = insertCard(card)
        if (status == -1L) {
            updateCard(card)
        }
    }

    fun deleteCard(card: Card) {
        cardDao.delete(card)
    }
}