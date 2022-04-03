package com.example.paginationapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class StockItemsAdapter(
    private val context: Context,
    private val stockItemsList: ArrayList<StockItem>
) :
    RecyclerView.Adapter<StockItemsAdapter.StockItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockItemViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_item_stock, parent, false)
        return StockItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockItemViewHolder, position: Int) {
        val stockItem = stockItemsList[position]

        holder.getItemNameTextView().text = stockItem.name
        holder.getSellingPriceTextView().text = "$ ${stockItem.sellingPrice}"
        holder.getStockValueTextView().text = "$ ${stockItem.mrp}"
        holder.getRemainingStocksTextView().text =
            "${stockItem.availableCount} ${stockItem.priceUnit}"
    }

    override fun getItemCount(): Int {
        return stockItemsList.size
    }

    fun updateList(stockItemsList: ArrayList<StockItem>) {
        this.stockItemsList.addAll(stockItemsList)
        notifyDataSetChanged()
    }

    class StockItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemNameTextView = itemView.findViewById<TextView>(R.id.tv_item_name)
        private val sellingPriceTextView = itemView.findViewById<TextView>(R.id.tv_selling_price)
        private val stockValueTextView = itemView.findViewById<TextView>(R.id.tv_stock_value)
        private val remainingStocksTextView =
            itemView.findViewById<TextView>(R.id.tv_remaining_stocks)

        fun getItemNameTextView(): TextView {
            return itemNameTextView
        }

        fun getSellingPriceTextView(): TextView {
            return sellingPriceTextView
        }

        fun getStockValueTextView(): TextView {
            return stockValueTextView
        }

        fun getRemainingStocksTextView(): TextView {
            return remainingStocksTextView
        }
    }
}