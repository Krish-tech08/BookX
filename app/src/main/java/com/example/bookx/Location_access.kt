package com.example.bookx

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookx.databinding.ActivityLocationAccessBinding

class Location_access : AppCompatActivity() {
    private lateinit var binding: ActivityLocationAccessBinding
    val location=listOf<String>("Ghaziabad","Delhi","Noida","Gurgoan","Meerut")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLocationAccessBinding.inflate(layoutInflater)
        val view = binding.root
        val adapter=ArrayAdapter(this,
            com.denzcoskun.imageslider.R.layout.support_simple_spinner_dropdown_item,location)
        binding.listOfLocation.setAdapter(adapter)

        binding.listOfLocation.setOnItemClickListener { parent, view, position, id ->
            val selectedLocation = parent.getItemAtPosition(position).toString()
            val sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("selected_location", binding.listOfLocation.text.toString())
            editor.apply()
            Toast.makeText(this, "Selected: $selectedLocation", Toast.LENGTH_SHORT).show()
        }

        binding.next.setOnClickListener {
            // Check if a location is selected
            val selectedLocation = binding.listOfLocation.text.toString()

            if (selectedLocation.isEmpty()) {
                // If no location is selected, show a Toast
                Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show()
            } else {
                // Save the selected location in SharedPreferences
                val sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("selected_location", selectedLocation)
                editor.apply()

                // Proceed to the next screen
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}