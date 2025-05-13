package com.example.bookx

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookx.adapter.AddressAdapter

class Saved_Adress : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_saved_adress)
        val backButton = findViewById<View>(R.id.bckarrw)
        backButton.setOnClickListener {
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_addresses)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPref = getSharedPreferences("addresses", Context.MODE_PRIVATE)
        val addressSet = sharedPref.getStringSet("address_list", emptySet())?.toList() ?: emptyList()

        val adapter = AddressAdapter(addressSet)
        recyclerView.adapter = adapter

        val addadress = findViewById<View>(R.id.add_address)
        addadress.setOnClickListener {
            val intent = Intent(this, Add_Adress::class.java)
            startActivity(intent)
        }
    }
}
