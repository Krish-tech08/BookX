package com.example.bookx.adapter

import OrderItem
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookx.Orderdetails
import com.example.bookx.R


class OrderAdapter(
    private val context: Context,
    private val orderList: List<OrderItem>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView11)
        val deliveryDate: TextView = view.findViewById(R.id.deliverydate)
        val bookNameAndPrice: TextView = view.findViewById(R.id.booknameandprice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = orderList[position]

        // Set book details
        holder.deliveryDate.text = orderItem.deliveryDate
        holder.bookNameAndPrice.text = "${orderItem.bookName}\n\n Price: ₹${orderItem.price}"

        // Set image
        holder.imageView.setImageResource(orderItem.imageResId)

        // 🔥 Set click listener on the entire item view
        holder.itemView.setOnClickListener {
            val intent = Intent(context, Orderdetails::class.java).apply {
                putExtra("bookName", orderItem.bookName)
                putExtra("bookPrice", orderItem.price)
                putExtra("deliveryDate", orderItem.deliveryDate)
                putExtra("bookImageResId", orderItem.imageResId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = orderList.size
}
