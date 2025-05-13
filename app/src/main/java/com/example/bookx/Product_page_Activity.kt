package com.example.bookx

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookx.databinding.ActivityProductPageBinding
import com.example.bookx.manager.CartManager
import com.example.bookx.model.CartItem

class Product_page_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityProductPageBinding
    private var selectedBook: SliderItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProductPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load saved address from SharedPreferences
        val sharedPref = getSharedPreferences("addresses", Context.MODE_PRIVATE)
        val addressList = sharedPref.getStringSet("address_list", setOf())?.toList() ?: emptyList()

        binding.addresstext.text = if (addressList.isNotEmpty()) {
            addressList.first()
        } else {
            "No saved addresses"
        }

        binding.change.setOnClickListener {
            startActivity(Intent(this, Saved_Adress::class.java))
        }

        // Get book data from Intent
        selectedBook = intent.getSerializableExtra("book_data") as? SliderItem
        selectedBook?.let {
            binding.bookname.text = it.name
            binding.bookimg.setImageResource(it.imageResId)
            binding.bookPrice.text = "₹${it.price}"
            binding.bookDes.text = it.description
        }

        // Buy Now: Save data in SharedPreferences (excluding image) and go to summary
        binding.buynow.setOnClickListener {
            selectedBook?.let {
                saveOrderToPrefs(it)
                startActivity(Intent(this, orederSummary::class.java))
            }
        }

        // Add to Cart
        binding.addtocart.setOnClickListener {
            selectedBook?.let {
                addToCart(it)
            }
        }

        // Navigate back to main activity
        binding.backarw.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Navigate to cart fragment
        binding.gotocart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("openCart", true)
            startActivity(intent)
        }

        // Apply edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addToCart(book: SliderItem) {
        val cartItem = CartItem(
            name = book.name,
            price = book.price,
            imageResId = book.imageResId,
            quantity = 1
        )
        CartManager.addItem(cartItem)
        Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show()
    }

    // Save only relevant book info to SharedPreferences
    private fun saveOrderToPrefs(book: SliderItem) {
        val sharedPref = getSharedPreferences("OrderData", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("bookName", book.name)
            putString("bookPrice", book.price.toString())
            putString("bookDescription", book.description)
            putString("deliveryDate", "May 15, 2025") // Static or calculate dynamically
            putInt("bookImageResId", book.imageResId)  // Save the image resource ID
            apply()
        }
    }
}
