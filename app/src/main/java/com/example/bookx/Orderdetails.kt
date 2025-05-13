package com.example.bookx

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.*

class Orderdetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_orderdetails)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get views
        val bookNameText = findViewById<TextView>(R.id.textView)
        val bookPriceText = findViewById<TextView>(R.id.textView24)
        val bookImage = findViewById<ImageView>(R.id.imageView2)
        val dateOrdered = findViewById<TextView>(R.id.dateonwhichordered)
        val packingDate = findViewById<TextView>(R.id.expecteddateofpacking)
        val shippingDate = findViewById<TextView>(R.id.expecteddateofshipping)
        val deliveryDate = findViewById<TextView>(R.id.expecteddateofdelivery)
        val backArrow = findViewById<ImageView>(R.id.bckarw)
        val cancelBtn = findViewById<Button>(R.id.cancel)
        val helpBtn = findViewById<Button>(R.id.needhelp)

        // Back button
        backArrow.setOnClickListener {
            finish()
        }

        // Retrieve data from intent
        val bookName = intent.getStringExtra("bookName") ?: "Unknown"
        val bookPrice = intent.getFloatExtra("bookPrice", 0f)
        val imageResId = intent.getIntExtra("bookImageResId", 0)
        val delivery = intent.getStringExtra("deliveryDate") ?: getRandomFutureDate(5, 9)

        // Populate views
        bookNameText.text = bookName
        bookPriceText.text = "Price: ₹$bookPrice"
        bookImage.setImageResource(imageResId)
        dateOrdered.text = "Ordered On: ${getRandomPastDate(0, 1)}"
        packingDate.text = "Packing Expected: ${getRandomFutureDate(1, 2)}"
        shippingDate.text = "Shipped Expected: ${getRandomFutureDate(2, 4)}"
        deliveryDate.text = "Expected Delivery: $delivery"
    }

    private fun getRandomFutureDate(minDays: Int, maxDays: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, (minDays..maxDays).random())
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(calendar.time)
    }

    private fun getRandomPastDate(minDaysAgo: Int, maxDaysAgo: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -(minDaysAgo..maxDaysAgo).random())
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(calendar.time)
    }
}
