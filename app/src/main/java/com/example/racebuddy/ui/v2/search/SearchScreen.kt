package com.example.racebuddy.ui.v2.search

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.ui.theme.heights
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.common.TopBarWithCategoryAndSearchChat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    events: List<EventInfo>,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    //val animatedOffset by animateDpAsState(targetValue = 64.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f)
            //.offset(y = paddings.spacingLarge)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            //.background(MaterialTheme.colors.surface)
            .zIndex(1f) // Make sure it's above background
    ) {
        Scaffold(
            topBar = {
                TopBarWithCategoryAndSearchChat(
                    categories = emptyList(),
                    selectedCategory = "",
                    onSearchQueryChanged = {},
                    onCategorySelected = {},
                    onCloseClick = onCloseClick
                )
            },
            containerColor = Color.White,
            contentColor = Color.Black
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {

                FilterButtons(
                    onFilterButtonClick = {

                    }
                )

                LazyColumn {
                    items(events) { eventInfo ->
                        SearchEventCard(
                            eventInfo = eventInfo
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenSheet(
    events: List<EventInfo>,
) {
    var searchQuery by remember { mutableStateOf("") }


//    ModalBottomSheet(
//        onDismissRequest = {},
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
        Column(
            modifier = Modifier
                //.padding(padding)
                .fillMaxWidth()
        ) {

            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    //onSearchQueryChanged(it)
                },
                placeholder = {
                    Text(
                        text = "Search...",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .weight(3f)
                    .padding(paddings.spacingXSmall)
                    .scale(scaleY = 0.9F, scaleX = 0.9F)
                //.height(80.dp)
                ,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = ""
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White, //Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color.White, //Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                ),
                shape = shapes.large,

                )

            FilterButtons(
                onFilterButtonClick = {

                }
            )

            LazyColumn {
                items(events) { eventInfo ->
                    SearchEventCard(
                        eventInfo = eventInfo
                    )
                }
            }
        }
    //}
}

@Composable
fun FilterButtons(
    items: List<String> = listOf("All", "Past", "Upcoming"),
    onFilterButtonClick: (String) -> Unit
) {
    var selectedItem by remember { mutableStateOf<String?>("All") }


    Column() {
//        Text(
//            text = "Period",
//            style = MaterialTheme.typography.labelLarge,
//            color = Color.Black,
//            fontWeight = FontWeight.Normal,
//            modifier = Modifier
//                .padding(
//                    start = paddings.spacingMedium,
//                    bottom = paddings.spacingXSmall/2
//                    //top = paddings.spacingXSmall
//                )
//            //.offset(y = -paddings.spacingXSmall)
//        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = paddings.spacingMedium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddings.spacingSmall, bottom = paddings.spacingSmall),
            ) {
            items(items) { item ->
                val isSelected = item == selectedItem
                Button(
                    onClick = {
                        selectedItem = item
                        // TODO: Implement actual action here
                        onFilterButtonClick(item)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                        contentColor = if (isSelected) Color.White else Color.Gray
                    ),
                    elevation = ButtonDefaults.buttonElevation(2.dp),
                    shape = RoundedCornerShape(shapes.small.topEnd),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier
                        .height(heights.xSmall)
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun SearchEventCard(eventInfo: EventInfo) {

}


@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        onCloseClick = {},
        events = emptyList()
    )
}
