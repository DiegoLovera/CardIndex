package com.diegolovera.cardindex

data class Card(
    val cardEntity: String,
    val cardType: String,
    val cardNumber: String,
    val cardValidUntil: String,
    val cardCvv: String,
    val cardHolderName: String,
    val cardBrand: String)