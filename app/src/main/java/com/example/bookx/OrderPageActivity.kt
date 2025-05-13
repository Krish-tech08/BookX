package com.example.bookx

import OrderItem
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookx.adapter.OrderAdapter
import com.example.bookx.databinding.ActivityOrderPageBinding

import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class OrderPageActivity : AppCompatActivity() {

    // Declare binding variable
    private lateinit var binding: ActivityOrderPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate the binding and set the content view
        binding = ActivityOrderPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
binding.backarrow.setOnClickListener {
    finish()
}
        // Retrieve order data from SharedPreferences
        val sharedPref = getSharedPreferences("OrderData", Context.MODE_PRIVATE)
        val bookName = sharedPref.getString("bookName", "Unknown Book") ?: "Unknown Book"
        val bookPrice = sharedPref.getString("bookPrice", "0.0")?.toFloat() ?: 0f
        val bookDescription = sharedPref.getString("bookDescription", "No Description") ?: "No Description"
        val deliveryDate = sharedPref.getString("deliveryDate", "2025-05-15") ?: "2025-05-15"
        val bookImageResId = sharedPref.getInt("bookImageResId", 0) // Replace with actual image resource if saved in SharedPreferences

        // Example data; you will replace this with real data from SharedPreferences
        val orderList = listOf(
            OrderItem(
                bookName = bookName,
                price = bookPrice,
                deliveryDate = deliveryDate,
                imageResId = bookImageResId // Use the stored image resource if available
            )
        )

        // Set up RecyclerView with OrderAdapter
        val orderAdapter = OrderAdapter(this, orderList)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = orderAdapter

        // Handle edge-to-edge insets for the layout
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Helper function to generate a random delivery date within 1 to 7 days from today
    private fun generateRandomDeliveryDate(): String {
        val calendar = Calendar.getInstance()
        val randomDays = Random.nextInt(1, 8) // Random number between 1 and 7
        calendar.add(Calendar.DAY_OF_YEAR, randomDays)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
