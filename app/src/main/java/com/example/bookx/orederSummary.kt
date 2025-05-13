package com.example.bookx

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookx.databinding.ActivityOrederSummaryBinding

class orederSummary : AppCompatActivity() {

    private lateinit var binding: ActivityOrederSummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrederSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.bckarrw.setOnClickListener {
            finish()
        }

        // Load address from SharedPreferences
        val sharedPref = getSharedPreferences("addresses", Context.MODE_PRIVATE)
        val addressList = sharedPref.getStringSet("address_list", setOf())?.toList() ?: emptyList()
        binding.adresstext.text = if (addressList.isNotEmpty()) {
            addressList.first()
        } else {
            "No saved addresses"
        }

        // Change address
        binding.change.setOnClickListener {
            startActivity(Intent(this, Saved_Adress::class.java))
        }

        // Load book info
        val bookPref = getSharedPreferences("OrderData", Context.MODE_PRIVATE)
        val name = bookPref.getString("bookName", "Unknown Book")
        val priceStr = bookPref.getString("bookPrice", "0") ?: "0"
        val price = priceStr.toDoubleOrNull() ?: 0.0

        // Calculate pricing
        val discount = price * 0.10
        val deliveryCharge = 50.0
        val total = price - discount + deliveryCharge

        // Show book summary
        binding.bookdetail.text = "$name\n₹${price.toInt()}"

        // Set pricing details
        binding.textView25.text = "Price: ₹${price.toInt()}"
        binding.textView26.text = "Discount: ₹${discount.toInt()}"
        binding.textView27.text = "Delivery Charges: ₹${deliveryCharge.toInt()}"
        binding.textView28.text = "Total Amount: ₹${total.toInt()}"

        // Store the total amount in SharedPreferences
        val editor = bookPref.edit()
        editor.putFloat("totalAmount", total.toFloat())  // Save as a float
        editor.apply()  // Commit the changes

        // Proceed to payment
        binding.paybtn.setOnClickListener {
            startActivity(Intent(this, payment_page::class.java))
        }

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
