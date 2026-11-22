package com.example.waygofrontend.data.model

data class HotelModel(
    val id: Int,
    val name: String,
    val area: String,
    val rating: String,
    val reviews: String,
    val price: Long,
    val oldPrice: Long,
    val badge: String,
    val amenity: String,
    val imageUrl: String? = null
)
