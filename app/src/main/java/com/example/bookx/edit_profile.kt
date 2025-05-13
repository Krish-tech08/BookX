package com.example.bookx

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize views
        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editPhone = findViewById(R.id.editPhone)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        // SharedPreferences setup
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Load saved data into EditTexts
        loadUserData()

        // Save button click
        btnSave.setOnClickListener {
            saveUserData()
        }

        // Cancel button click
        btnCancel.setOnClickListener {
            finish()
        }
    }

    // Function to load data from SharedPreferences and populate the EditTexts
    private fun loadUserData() {
        val name = sharedPreferences.getString("userName", "")
        val email = sharedPreferences.getString("userEmail", "")
        val phone = sharedPreferences.getString("userPhone", "")

        // Set the saved data into EditTexts
        editName.setText(name)
        editEmail.setText(email)
        editPhone.setText(phone)
    }

    // Function to save data entered by the user into SharedPreferences
    private fun saveUserData() {
        val name = editName.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val phone = editPhone.text.toString().trim()

        // Check if any field is empty
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Save the updated data into SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("userName", name)
        editor.putString("userEmail", email)
        editor.putString("userPhone", phone)
        editor.apply()

        // Show a toast message confirming profile update
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()

        // Optionally finish activity or go back to the previous screen
        finish()
    }
}
