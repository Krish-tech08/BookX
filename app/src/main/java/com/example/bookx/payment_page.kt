package com.example.bookx

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookx.databinding.ActivityPaymentPageBinding

class payment_page : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.bckarw.setOnClickListener {
            finish()
        }

        // Retrieve the total amount from SharedPreferences
        val bookPref = getSharedPreferences("OrderData", Context.MODE_PRIVATE)
        val totalAmount = bookPref.getFloat("totalAmount", 0.0f)

        // Set the total amount to the TextView
        binding.totalamountTextview.text = "Total Amount: ₹${totalAmount.toInt()}"
binding.cod.setOnClickListener {
    val intent = Intent(this, orderconfirmed::class.java)
    startActivity(intent)
}
        // Insets for edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
