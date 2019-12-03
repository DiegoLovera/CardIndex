package com.diegolovera.cardindex.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.diegolovera.cardindex.data.models.Card

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(card: Card): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(card: Card)

    @Delete
    fun delete(card: Card)

    @Query("SELECT * FROM cards")
    fun getAll(): LiveData<List<Card>>

    @Query("SELECT * FROM cards WHERE id = :id")
    fun getById(id: String): LiveData<Card>
}