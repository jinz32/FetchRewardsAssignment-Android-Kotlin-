package com.example.fetchrewardsassignment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel that manages the state of the Home screen. Loads items from an API, sorts and filters them, then updates the state with grouped items or handles errors
class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        load()
    }

    private fun load() {
        // use viewmodelScope for async
        viewModelScope.launch {
            try {
                val response = ItemAPIProvider.client.getItems()
                // Filter and sort items from API response
                val sortedItems =
                    response
                        .filter { it.name != null && it.name.isNotBlank() } // Filter out items with blank or null names
                        .sortedWith(compareBy({ it.listId }, { getIdForSorting(it) }))
                val groupedItems =
                    sortedItems
                        .groupBy { it.listId }
                        .flatMap { (listId, items) ->
                            listOf(GroupedItem.Header(listId.toString())) +
                                items.map {
                                    GroupedItem.Item(listId.toString(), it.id.toString(), it.name ?: "")
                                }
                        }
                // groupedItems is 324 (320 DataItem with 4 headers(itemList grouping) that I created, checked JSON in excel to confirm data)
                _state.value = State.Data(groupedItems)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading items", e)
                _state.value = State.Error
            }
        }
    }

    // as iterating through sorts after removing Item (was considering item a string and not sorting properly without) ex. item 42 next to item 420
    private fun getIdForSorting(item: ItemData): Int =
        if (item.name.isNullOrBlank()) {
            Int.MAX_VALUE
        } else {
            item.name.removePrefix("Item ").toIntOrNull() ?: Int.MAX_VALUE
        }

    fun setDatum(itemData: ItemData) {
        _state.value = State.DataItem(itemData)
    }
}

sealed class GroupedItem {
    abstract val listID: String // Common property for both Header and Item

    data class Header(
        override val listID: String,
    ) : GroupedItem()

    data class Item(
        override val listID: String,
        val id: String,
        val name: String,
    ) : GroupedItem()
}

sealed class State {
    data object Loading : State()

    data class Data(
        val items: List<GroupedItem>,
    ) : State()

    data class DataItem(
        val item: ItemData,
    ) : State()

    data object Error : State()
}
