package com.example.fetchrewardsassignment

import kotlinx.serialization.Serializable

// defining the attributes within the list of ItemData
@Serializable
data class ItemData(
    val id: Int,
    val listId: Int,
    val name: String?,
)
