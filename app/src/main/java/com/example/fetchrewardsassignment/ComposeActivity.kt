package com.example.fetchrewardsassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// Composable set up
class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Home()
        }
    }
}

@Composable
fun Home(vm: HomeViewModel = viewModel()) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val state by vm.state.collectAsState()
        when (val currentState = state) {
            State.Loading -> Loading()
            State.Error -> Error()
            is State.Data -> DisplayGroupedItems(currentState.items)
            is State.DataItem -> TODO()
        }
    }
}

// displays my state
@Composable
fun BoxScope.Loading() {
    Text(
        modifier = Modifier.align(Alignment.Center),
        text = "Loading",
    )
}

@Composable
fun BoxScope.Error() {
    Text(
        modifier = Modifier.align(Alignment.Center),
        textAlign = TextAlign.Center,
        text = "Error\nUnable to get itemList",
    )
}

@Composable
fun DisplayGroupedItems(items: List<GroupedItem>) {
    // Group items by listID
    val groupedItems: Map<String, List<GroupedItem.Item>> =
        items
            .filterIsInstance<GroupedItem.Item>()
            .groupBy { it.listID }

    LazyColumn {
        groupedItems.forEach { (listId, itemList) ->
            item {
                Text(
                    text = "List ID: $listId",
                    modifier =
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium),
                )
            }
            items(itemList) { item ->
                ItemRow(itemData = item) // Display each item
            }
        }
    }
}

@Composable
fun ItemRow(itemData: GroupedItem.Item) {
    Row(
        modifier =
            Modifier
                .requiredHeight(48.dp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BoldText(text = "List ID:")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = itemData.listID,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Left,
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            BoldText(text = "ID:")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = itemData.id,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Left,
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            BoldText(text = "Name:")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = itemData.name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Left,
            )
        }
    }
}

@Composable
fun BoldText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        textAlign = TextAlign.Start,
    )
}
