package com.example.bookx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.bookx.R
import com.example.bookx.model.CartItem

class CartAdapter(
    private val cartList: MutableList<CartItem>,
    private val onCartChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookName: TextView = itemView.findViewById(R.id.book_name)
        val bookPrice: TextView = itemView.findViewById(R.id.price)
        val bookImage: ImageView = itemView.findViewById(R.id.book_img)
        val bookRating: TextView = itemView.findViewById(R.id.rating)
        val spinner: Spinner = itemView.findViewById(R.id.spinner)
        val removeBtn: TextView = itemView.findViewById(R.id.removeBtn)
        val buyNowBtn: TextView = itemView.findViewById(R.id.buyNowBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_books, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartList[position]
        holder.bookName.text = item.name
        holder.bookPrice.text = "₹${item.price * item.quantity}"
        holder.bookImage.setImageResource(item.imageResId)
        holder.bookRating.text = "Qty: ${item.quantity}"

        val qtyOptions = (1..10).map { it.toString() }
        val adapter = ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_item, qtyOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinner.adapter = adapter
        holder.spinner.setSelection(item.quantity - 1)

        holder.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val newQty = pos + 1
                if (item.quantity != newQty) {
                    item.quantity = newQty
                    notifyItemChanged(position)
                    onCartChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        holder.removeBtn.setOnClickListener {
            cartList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartList.size)
            onCartChanged()
        }
    }

    override fun getItemCount(): Int = cartList.size
}
