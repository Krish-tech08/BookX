package com.example.bookx.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookx.R
import com.example.bookx.Saved_Adress
import com.example.bookx.adapter.CartAdapter
import com.example.bookx.databinding.FragmentCartBinding
import com.example.bookx.manager.CartManager
import com.example.bookx.model.CartItem

class CartFragment : Fragment() {

    private lateinit var totalpricetextview: TextView
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
val change = view.findViewById<TextView>(R.id.change)
        change.setOnClickListener {
            val intent = Intent(requireContext(), Saved_Adress::class.java)
            startActivity(intent)
        }
        totalpricetextview = view.findViewById(R.id.totalpricetextview)

        val recyclerView = view.findViewById<RecyclerView>(R.id.cartRecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Use the shared cartItems from CartManager
        cartAdapter = CartAdapter(CartManager.cartItems) {
            updateTotalPrice()
        }

        if (CartManager.cartItems.isEmpty()) {
            CartManager.addItem(CartItem("Debug Book", 123.0, R.drawable.sample_image_1, 1))
        }

        recyclerView.adapter = cartAdapter

        // Retrieve the saved address list from SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("addresses", Context.MODE_PRIVATE)
        val addressList = sharedPref.getStringSet("address_list", setOf())?.toList() ?: emptyList()

        // Set the address to the TextView
        val addressTextView = view.findViewById<TextView>(R.id.textView12)  // Assuming this is your TextView for the address
        addressTextView.text = if (addressList.isNotEmpty()) {
            addressList.first()  // Display the first saved address
        } else {
            "No saved addresses"  // Default message if the address list is empty
        }

        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val total = CartManager.calculateTotal()

        // Save the updated total price to SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("totalPrice", total.toFloat())
        editor.apply()

        // Update the TextView with the new total price
        totalpricetextview.text = "Total: ₹${total.toInt()}"
    }

    override fun onResume() {
        super.onResume()
        cartAdapter.notifyDataSetChanged()
        updateTotalPrice()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = CartFragment()
    }
}
