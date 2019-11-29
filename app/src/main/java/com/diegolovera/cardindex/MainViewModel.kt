package com.diegolovera.cardindex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    fun getCards(): LiveData<List<Card>> {
        val result = MutableLiveData<List<Card>>()
        result.value = arrayListOf(
            Card("Santander",
                "Credit card",
                "1239658456821567",
                "09/26",
                "229",
                "Diego Lovera Viga",
                "MasterCard"),
            Card("BBVA",
                "Credit card",
                "1239658456821567",
                "12/26",
                "359",
                "Diego Lovera Viga",
                "MasterCard"),
            Card("Banorte",
                "Debit card",
                "1239658456821567",
                "07/21",
                "951",
                "Larissa Lovera Viga",
                "Visa"),
            Card("Suburbia",
                "Departmental",
                "1239658456821567",
                "03/24",
                "179",
                "Hugo Lovera Chavez",
                ""))
        return result
    }
}