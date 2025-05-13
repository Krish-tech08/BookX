package com.example.bookx.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bookx.*
import com.example.bookx.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        // Navigate to Plus Membership screen
        binding.plusmember.setOnClickListener {
            startActivity(Intent(requireContext(), plus_membership::class.java))
        }

        // Navigate to Order Page
        binding.orderdirect.setOnClickListener {
            startActivity(Intent(requireContext(), OrderPageActivity::class.java))
        }

        // Navigate to Terms and Conditions screen
        binding.termsndcodition.setOnClickListener {
            startActivity(Intent(requireContext(), termsndcondition::class.java))
        }

        // Navigate to FAQ screen
        binding.browsefaq.setOnClickListener {
            startActivity(Intent(requireContext(), faq::class.java))
        }

        // Navigate to Saved Addresses screen
        binding.saveAddress.setOnClickListener {
            startActivity(Intent(requireContext(), Saved_Adress::class.java))
        }

        // Navigate to Edit Profile screen
        binding.editprofile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        // Set the user name from SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserData", MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", "Default Name")
        binding.nametextview.text = userName

        // Logout button click (clears SharedPreferences)
        binding.logout.setOnClickListener {
            // Sign out from Firebase
            FirebaseAuth.getInstance().signOut()

            // Clear SharedPreferences to erase user data
            val editor = sharedPreferences.edit()
            editor.clear()  // Clears all the data saved in SharedPreferences
            editor.apply()

            // Redirect to the login screen and clear the activity stack
            val intent = Intent(requireContext(), bookx_login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = AccountFragment()
    }
}
