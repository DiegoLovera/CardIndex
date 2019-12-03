package com.diegolovera.cardindex.ui.activites

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diegolovera.cardindex.data.models.Card
import com.diegolovera.cardindex.data.repositories.CardRepository

class MainViewModel(private val mCardRepository: CardRepository): ViewModel() {

    fun getCards(): LiveData<List<Card>> {
        return mCardRepository.getAllCards()
    }

    fun addCard(card: Card) {
        AsyncTask.execute {
            mCardRepository.upsertCard(card)
        }
    }

    fun removeCard(card: Card) {
        AsyncTask.execute {
            mCardRepository.deleteCard(card)
        }
    }
}