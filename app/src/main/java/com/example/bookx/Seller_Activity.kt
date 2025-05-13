package com.example.bookx

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookx.databinding.ActivitySellerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.content.FileProvider
import java.io.File

class Seller_Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerBinding
    private val listOf_classes = listOf("Class 1", "Class 2", "Class 3", "Class 4", "Class 5", "Class 6", "Class 7", "Class 8", "Class 9", "Class 10", "Class 11", "Class 12", "None")
    private val listOf_category = listOf("Study", "Novel", "Historic", "Religious", "Competitive", "Others")

    private val cameraRequestCode = 100
    private var imageUri: Uri? = null

    private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            binding.imageView8.setImageURI(imageUri)
            Log.d("ImageSelection", "Gallery image selected: $imageUri")
        } ?: run {
            Log.e("Seller_Activity", "Failed to get image from gallery")
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && imageUri != null) {
            binding.imageView8.setImageURI(imageUri)
            Log.d("ImageSelection", "Photo taken and saved: $imageUri")
        } else {
            Toast.makeText(this, "Failed to capture photo", Toast.LENGTH_SHORT).show()
            imageUri = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listOfClasses.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listOf_classes))
        binding.listOfCategory.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listOf_category))

        binding.backArrow.setOnClickListener { finish() }

        binding.cardView2.setOnClickListener {
            val options = arrayOf("Take Photo", "Choose from Gallery")
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Choose an option")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            takePhoto()
                        } else {
                            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), cameraRequestCode)
                        }
                    }
                    1 -> getImageFromGallery.launch("image/*")
                }
            }
            builder.show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.uploadbutton.setOnClickListener {

            val intent=Intent(this, orderconfirmed::class.java)
            startActivity(intent)


        }
    }

    private fun takePhoto() {
        try {
            val photoFile = File(externalCacheDir, "photo_${System.currentTimeMillis()}.jpg")
            val photoUri = FileProvider.getUriForFile(this, "${packageName}.provider", photoFile)
            imageUri = photoUri

            // Grant URI permission
            val resInfoList = packageManager.queryIntentActivities(Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                grantUriPermission(packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            takePicture.launch(photoUri)
        } catch (e: Exception) {
            Toast.makeText(this, "Error taking photo: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("Seller_Activity", "Error taking photo: ${e.message}", e)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraRequestCode && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            takePhoto()
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadBookDetailsToFirebase(title: String, price: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val classLevel = binding.listOfClasses.text.toString()
        val category = binding.listOfCategory.text.toString()
        val storageRef = FirebaseStorage.getInstance().reference
        val firestore = FirebaseFirestore.getInstance()

        val imageRef = storageRef.child("book_images/$userId/${System.currentTimeMillis()}.jpg")

        imageUri?.let { uri ->
            imageRef.putFile(uri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val bookData = hashMapOf(
                            "title" to title,
                            "price" to price,
                            "classLevel" to classLevel,
                            "category" to category,
                            "imageUrl" to downloadUri.toString(),
                            "userId" to userId
                        )

                        firestore.collection("books")
                            .add(bookData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Book uploaded successfully!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to upload details: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("Upload", "Image upload failed", e)
                }
        } ?: run {
            Toast.makeText(this, "Image URI is null", Toast.LENGTH_SHORT).show()
        }
    }
}
