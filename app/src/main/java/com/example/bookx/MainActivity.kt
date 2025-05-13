package com.example.bookx

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the NavController from the NavHostFragment
        val navController = findNavController(R.id.fragmentContainerView5)

        // Setup BottomNavigation with Navigation Controller
        binding.bottomNavigationView.setupWithNavController(navController)

        // Check if intent wants to open cart directly
        val openCart = intent.getBooleanExtra("openCart", false)
        Log.d("MainActivity", "openCart = $openCart")

        if (openCart) {
            binding.root.postDelayed({
                try {
                    navController.navigate(R.id.cartFragment)
                } catch (e: Exception) {
                    Log.e("MainActivity", "Navigation to cartFragment failed", e)
                }
            }, 100)
        }

        // Apply insets only to BottomNavigationView for better device compatibility
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                systemBars.bottom
            )
            insets
        }
    }
}
