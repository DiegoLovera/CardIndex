package com.diegolovera.cardindex

import android.app.Application
import com.diegolovera.cardindex.data.CardIndexDatabase

class CardIndexApp: Application() {
    override fun onCreate() {
        super.onCreate()
        CardIndexDatabase.invoke(this)
    }
}