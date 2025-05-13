package com.example.bookx.model

data class Book(
    val title: String,
    val price: String,
    val imageResId: Int,
    val classLevel: Int  // Used to filter books by class
)
