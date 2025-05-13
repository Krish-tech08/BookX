package com.example.bookx

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookx.databinding.ActivityBookxSignupBinding

class bookx_signup : AppCompatActivity() {
    private lateinit var binding: ActivityBookxSignupBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookxSignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)

        binding.submit.setOnClickListener {
            // Get the values entered by the user
            val name = binding.nametext.text.toString().trim()
            val dob = binding.editTextTextEmailAddress.text.toString().trim()
            val email = binding.editTextTextEmailAddress.text.toString().trim()

            // Check if all fields are filled
            if (name.isEmpty() || dob.isEmpty() || email.isEmpty()) {
                // Show a toast if any field is empty
                Toast.makeText(this, "Please enter name, date of birth, and email", Toast.LENGTH_SHORT).show()
            } else {
                // Storing name, dob, and email to shared preferences
                val editor = sharedPreferences.edit()
                editor.putString("userName", name)
                editor.putString("userEmail", email) // Save email
                editor.apply()

                // Proceed to the next activity if all fields are filled
                val intent = Intent(this, Location_access::class.java)
                startActivity(intent)
            }
        }

        // Ensuring layout respects edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
