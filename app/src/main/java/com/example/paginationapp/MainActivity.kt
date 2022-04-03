package com.example.paginationapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private var offset = 0
    private var isExhausted = false
    private var isLoading = false

    private lateinit var stockItemsAdapter: StockItemsAdapter


    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerview_item_stock)

        recyclerView.also {
            stockItemsAdapter = StockItemsAdapter(this, arrayListOf())
            it.adapter = stockItemsAdapter
            it.layoutManager = LinearLayoutManager(this)
        }

        requestItemsWithPagination()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!isExhausted && !isLoading) {
                        requestItemsWithPagination()
                    }
                }
            }
        })
    }

    private fun requestItemsWithPagination() {
        CoroutineScope(Dispatchers.IO).launch {
            isLoading = true

            val stockItemsList = ArrayList<StockItem>()
            val queue = Volley.newRequestQueue(this@MainActivity)
            val url = "http://stock.digitalregister.in:8080/api/v4/stockItem/getPaginatedItemsList"
            val jsonObject = JSONObject()
            try {
                jsonObject.put("storeId", "9a917180-f9b7-465f-b1d6-dd1b4cb5a9e6")
                jsonObject.put("searchString", "")
                jsonObject.put("categoryId", "ALL")
                jsonObject.put("offset", offset)
                jsonObject.put("isLowCountItems", false)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val requestList = JSONArray()
            requestList.put(jsonObject)
            val jsonObjectRequest =
                object : JsonArrayRequest(
                    Request.Method.POST,
                    url,
                    requestList,
                    Response.Listener<JSONArray> { response ->
                        Log.i("API_SUCCESS", "SUCCESS")
                        val length = response.length()
                        for (i in 0..response.length()) {
                            try {
                                val obj = response.getJSONObject(i)
                                if (obj.optDouble("lowCountLimit", -5.0) < 0) {
                                    stockItemsList.add(
                                        StockItem(
                                            obj.getString("stockItemId"),
                                            obj.getString("storeId"),
                                            obj.getString("name"),
                                            obj.getString("purchasePrice"),
                                            obj.getString("sellingPrice"),
                                            obj.getString("mrp"),
                                            obj.getString("priceUnit"),
                                            obj.getString("availableCount"),
                                            obj.getString("lastUpdatedTime"),
                                            obj.getBoolean("isItemCountLow")
                                        )
                                    )
                                } else {
                                    stockItemsList.add(
                                        StockItem(
                                            obj.getString("stockItemId"),
                                            obj.getString("storeId"),
                                            obj.getString("name"),
                                            obj.getString("purchasePrice"),
                                            obj.getString("sellingPrice"),
                                            obj.getString("mrp"),
                                            obj.getString("priceUnit"),
                                            obj.getString("availableCount"),
                                            obj.getString("lastUpdatedTime"),
                                            obj.getDouble("lowCountLimit"),
                                            obj.getBoolean("isItemCountLow")
                                        )
                                    )
                                }
                                val count = stockItemsList.size

                                val size = stockItemsList.size
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                        updateStockList(stockItemsList)
                    },
                    Response.ErrorListener { error ->
                        Log.e("API_ERROR", error.message.toString())
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers.put("Content-Type", "application/json")
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }
    }

    private fun updateStockList(stockItemsList: ArrayList<StockItem>) {
        isLoading = false
        if (stockItemsList.size < 30) {
            isExhausted = true
        }
        offset += 1
        stockItemsAdapter.updateList(stockItemsList)
    }
}