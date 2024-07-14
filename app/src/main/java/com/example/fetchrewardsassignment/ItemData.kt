package com.example.fetchrewardsassignment

import kotlinx.serialization.Serializable

@Serializable
data class ItemData(
    val id: Int,
    val listId: Int,
    val name: String?,
)
