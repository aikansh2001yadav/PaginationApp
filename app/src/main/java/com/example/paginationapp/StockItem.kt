package com.example.paginationapp

data class StockItem(
    var stockItemId: String? = null,
    var storeId: String? = null,
    var name: String? = null,
    var purchasePrice: String? = null,
    var sellingPrice: String? = null,
    var mrp: String? = null,
    var priceUnit: String? = null,
    var availableCount: String? = null,
    var lastUpdatedTime: String? = null,
    var lowCountLimit: Double? = null,
    var isItemCountLow: Boolean? = null
) {
    constructor(
        stockItemId: String?,
        storeId: String?,
        name: String?,
        purchasePrice: String?,
        sellingPrice: String?,
        mrp: String?,
        priceUnit: String?,
        availableCount: String?,
        lastUpdatedTime: String?,
        isItemCountLow: Boolean?
    ) : this() {
        this.stockItemId = stockItemId
        this.storeId = storeId
        this.name = name
        this.purchasePrice = purchasePrice
        this.sellingPrice = sellingPrice
        this.mrp = mrp
        this.priceUnit = priceUnit
        this.availableCount = availableCount
        this.lastUpdatedTime = lastUpdatedTime
        this.isItemCountLow = isItemCountLow
    }
}