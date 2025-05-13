package com.example.bookx.manager

import com.example.bookx.model.CartItem

object CartManager {
    val cartItems = mutableListOf<CartItem>()

    fun addItem(item: CartItem) {
        val existing = cartItems.find { it.name == item.name }
        if (existing != null) {
            existing.quantity += item.quantity
        } else {
            cartItems.add(item)
        }
    }

    fun removeItem(item: CartItem) {
        cartItems.remove(item)
    }

    fun calculateTotal(): Double {
        return cartItems.sumOf { it.price * it.quantity }
    }
}
