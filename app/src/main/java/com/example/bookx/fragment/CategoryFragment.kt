package com.example.bookx.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookx.R
import com.example.bookx.adapter.CategoryBookAdapter
import com.example.bookx.model.Book

class CategoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryBookAdapter
    private val allBooks = mutableListOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.classRecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = CategoryBookAdapter(emptyList())
        recyclerView.adapter = adapter

        // Add sample books
        allBooks.addAll(
            listOf(
                Book("NCERT Class 10 Science", "₹120", R.drawable.sample_image_7, 10),
                Book("RD Sharma Class 10 Maths", "₹160", R.drawable.sample_image_12, 10),
                Book("NCERT Exemplar Class 9 Science", "₹90", R.drawable.sample_image_15, 9),
                Book("Together with Chemistry Class 11", "₹140", R.drawable.sample_image_14, 11),
                Book("NCERT Class 12 Physics Part 1", "₹125", R.drawable.sample_image_10, 12),
                        Book("Oswaal Question Bank Class 12 Physics", "₹150", R.drawable.sample_image_13, 12),
                Book("NCERT Class 12 Biology", "₹130", R.drawable.sample_image_9, 12),
                Book("NCERT Class 12 Chemistry Part 2", "₹135", R.drawable.sample_image_11, 12)
            )
        )

        // Add click listeners to class level buttons (TextViews)
        val classLevels = listOf(5, 6, 7, 8, 9, 10, 11, 12)
        classLevels.forEach { classNum ->
            val resId = resources.getIdentifier("class$classNum", "id", requireContext().packageName)
            val textView = view.findViewById<View>(resId)

            // Ensure we're working with TextView and not another view type
            if (textView is TextView) {
                textView.setOnClickListener {
                    filterBooksByClass(classNum)
                }
            } else {
                // Log or handle error here if the view is not a TextView
                Log.e("CategoryFragment", "View with ID $resId is not a TextView!")
            }
        }
    }



    // Function to filter books by class level
    private fun filterBooksByClass(classLevel: Int) {
        val filteredBooks = allBooks.filter { it.classLevel == classLevel }
        adapter.updateBooks(filteredBooks)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = CategoryFragment()
    }
}
