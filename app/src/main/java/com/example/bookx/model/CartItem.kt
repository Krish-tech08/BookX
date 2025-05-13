package com.example.bookx.model


data class CartItem(
    val name: String,
    val price: Double,
    val imageResId: Int,
    var quantity: Int = 1
)
