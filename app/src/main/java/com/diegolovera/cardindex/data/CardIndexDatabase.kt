package com.diegolovera.cardindex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.diegolovera.cardindex.data.daos.CardDao
import com.diegolovera.cardindex.data.models.Card

@Database(entities = [Card::class], version = 1)
abstract class CardIndexDatabase: RoomDatabase() {
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile private var instance: CardIndexDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            CardIndexDatabase::class.java, "cardindex.db")
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                }
            })
            .build()

        private fun prepopulateDb(database: SupportSQLiteDatabase) {
            database.query("INSERT INTO cards ('Santander', 'Credit Card', '1234567890123456', '12/23', '102', 'Diego Lovera Viga', 'MasterCard')")
        }
    }
}