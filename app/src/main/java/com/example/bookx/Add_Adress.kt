package com.example.bookx

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Add_Adress : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_adress)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fullName = findViewById<EditText>(R.id.et_full_name)
        val phone = findViewById<EditText>(R.id.et_phone)
        val address = findViewById<EditText>(R.id.et_address)
        val city = findViewById<EditText>(R.id.et_city)
        val postalCode = findViewById<EditText>(R.id.et_postal_code)
        val saveBtn = findViewById<Button>(R.id.btn_save_address)

        saveBtn.setOnClickListener {
            val name = fullName.text.toString().trim()
            val ph = phone.text.toString().trim()
            val addr = address.text.toString().trim()
            val ct = city.text.toString().trim()
            val postal = postalCode.text.toString().trim()

            // Check if any field is empty
            if (name.isEmpty() || ph.isEmpty() || addr.isEmpty() || ct.isEmpty() || postal.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Format the full address
            val fullAddress = "$name\n$ph\n$addr\n$ct - $postal"

            // Save the address to SharedPreferences
            val sharedPref = getSharedPreferences("addresses", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()

            // Retrieve existing addresses, or initialize an empty set if none exist
            val existing = sharedPref.getStringSet("address_list", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

            // Add the new address to the existing set
            existing.add(fullAddress)

            // Save the updated set back to SharedPreferences
            editor.putStringSet("address_list", existing)
            editor.apply()

            Toast.makeText(this, "Address Saved", Toast.LENGTH_SHORT).show()
            finish()  // Close the current activity
        }
    }
}
