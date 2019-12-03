package com.diegolovera.cardindex.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "cardEntity")
    val cardEntity: String,

    @ColumnInfo(name = "cardType")
    val cardType: String,

    @ColumnInfo(name = "cardNumber")
    val cardNumber: String,

    @ColumnInfo(name = "cardValidUntil")
    val cardValidUntil: String,

    @ColumnInfo(name = "cardCode")
    val cardCode: String,

    @ColumnInfo(name = "cardHolderName")
    val cardHolderName: String,

    @ColumnInfo(name = "cardBrand")
    val cardBrand: String)