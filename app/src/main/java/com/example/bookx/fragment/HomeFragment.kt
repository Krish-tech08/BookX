package com.example.bookx.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.bookx.Product_page_Activity
import com.example.bookx.R
import com.example.bookx.Seller_Activity
import com.example.bookx.SliderItem
import com.example.bookx.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recentlyViewedBooks: List<SliderItem> // store all items for filtering

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.sellToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val intent = Intent(requireContext(), Seller_Activity::class.java)
                startActivity(intent)
                binding.sellToggle.isChecked = false
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMainBannerSlider()
        setupRecentlyViewedSlider()
        setupSearchFilter()

        val sharedPreferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val selectedLocation = sharedPreferences.getString("selected_location", "Not selected")
        binding.location.setText(selectedLocation)
    }

    private fun setupMainBannerSlider() {
        val imageList = arrayListOf(
            SlideModel(R.drawable.slider1, ScaleTypes.FIT),
            SlideModel(R.drawable.slider2, ScaleTypes.FIT),
            SlideModel(R.drawable.slider3, ScaleTypes.FIT)
        )
        binding.imageSlider.setImageList(imageList)
    }

    private fun setupRecentlyViewedSlider() {
        recentlyViewedBooks = listOf(
            SliderItem(R.drawable.sample_image_1, "Harry Potter", 199.0, "The Harry Potter series follows a young wizard, Harry, as he attends Hogwarts School of Witchcraft and Wizardry and confronts the dark wizard Lord Voldemort. A tale of friendship, bravery, and magic."),
            SliderItem(R.drawable.sample_image_22, "Lord of the Rings", 179.0, "An epic fantasy trilogy by J.R.R. Tolkien where Frodo Baggins embarks on a perilous quest to destroy the One Ring and defeat the Dark Lord Sauron."),
            SliderItem(R.drawable.sample_image_33, "The Alchemist", 99.0, "Paulo Coelho's philosophical novel about Santiago, a shepherd boy who dreams of treasure and learns about his destiny and the meaning of life on his journey."),
            SliderItem(R.drawable.sample_image_55, "Atomic Habits", 159.0, "James Clear shows how small changes in habits can lead to remarkable results over time. A practical guide to habit formation and personal development."),
            SliderItem(R.drawable.sample_image_66, "Rich Dad Poor Dad", 149.0, "Robert Kiyosaki contrasts financial philosophies from two father figures, advocating for financial literacy and building wealth through assets and entrepreneurship."),

            SliderItem(R.drawable.sample_image_7, "NCERT Class 10 Science", 120.0, "Covers key scientific concepts including Chemical Reactions, Life Processes, Electricity, and Natural Resources. Designed as per the CBSE syllabus."),
            SliderItem(R.drawable.sample_image_8, "NCERT Class 10 Mathematics", 110.0, "Essential topics like Algebra, Trigonometry, Geometry, and Probability tailored to help students prepare for board exams effectively."),
            SliderItem(R.drawable.sample_image_9, "NCERT Class 12 Biology", 130.0, "Explains complex biological systems and processes, including Genetics, Evolution, and Ecology. Ideal for Class 12 CBSE and NEET preparation."),
            SliderItem(R.drawable.sample_image_10, "NCERT Class 12 Physics Part 1", 125.0, "Detailed chapters on Electrostatics, Current Electricity, and Magnetism. Aligned with CBSE board requirements."),
            SliderItem(R.drawable.sample_image_11, "NCERT Class 12 Chemistry Part 2", 135.0, "Second part focuses on Surface Chemistry, Biomolecules, Polymers, and Chemistry in Everyday Life. CBSE-focused content."),
            SliderItem(R.drawable.sample_image_12, "RD Sharma Class 10 Maths", 160.0, "Comprehensive practice book for Class 10 students with step-by-step solutions and detailed explanations for all major math concepts."),
            SliderItem(R.drawable.sample_image_13, "Oswaal Question Bank Class 12 Physics", 150.0, "Includes chapter-wise important questions, sample papers, mind maps, and expert tips to aid in effective board exam preparation."),
            SliderItem(R.drawable.sample_image_14, "Together with Chemistry Class 11", 140.0, "Practice book with solved questions, concept summaries, and CBSE exam-style question patterns for Class 11 Chemistry."),
            SliderItem(R.drawable.sample_image_15, "NCERT Exemplar Class 9 Science", 90.0, "Supplementary book with higher-order thinking questions, designed to strengthen conceptual understanding for Class 9 Science students.")
        )


        displayBooksInSlider(recentlyViewedBooks)
    }

    private fun setupSearchFilter() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val filtered = recentlyViewedBooks.filter { book ->
                        book.name.contains(it.trim(), ignoreCase = true)
                    }

                    if (filtered.isNotEmpty()) {
                        displayBooksInSlider(filtered)
                    } else {
                        Toast.makeText(requireContext(), "Book not available", Toast.LENGTH_SHORT).show()
                        displayBooksInSlider(emptyList()) // or keep showing all if you prefer
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally filter in real-time here
                return false
            }
        })
    }

    private fun displayBooksInSlider(bookList: List<SliderItem>) {
        binding.horizontalslider.setSlides(bookList) { position ->
            val intent = Intent(requireContext(), Product_page_Activity::class.java).apply {
                putExtra("book_data", bookList[position])
            }
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                // initialize params if needed
            }
    }
}
