package com.diegolovera.cardindex.ui.activites

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.diegolovera.cardindex.data.repositories.CardRepository

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                mCardRepository = CardRepository(
                    context
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}