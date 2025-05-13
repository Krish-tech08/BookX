package com.example.bookx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookx.R
import com.example.bookx.model.Book

class CategoryBookAdapter(private var books: List<Book>) : RecyclerView.Adapter<CategoryBookAdapter.BookViewHolder>() {

    // Define a ViewHolder to hold references to your views
    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookTitle: TextView = itemView.findViewById(R.id.book_name)
        val bookPrice: TextView = itemView.findViewById(R.id.book_price)
        val bookImage: ImageView = itemView.findViewById(R.id.book_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bookTitle.text = book.title
        holder.bookPrice.text = book.price
        holder.bookImage.setImageResource(book.imageResId) // Assume book has image resource id
    }

    override fun getItemCount() = books.size

    // Method to update the book list
    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged() // Notify the adapter that data has changed
    }
}
