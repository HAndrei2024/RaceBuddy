package com.example.racebuddy.ui.event

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun HorizontalPagerTabRowSample() {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val pages = listOf(
        "Details",
        "Results"
    )
    val scrollCoroutineScope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier//.padding(horizontal = 32.dp)
            .fillMaxWidth()
    ) {

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                )
            },
        ) {
            pages.forEachIndexed { index, title ->
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = title,
                        modifier = Modifier
                            .clickable {
                                scrollCoroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                            .padding(8.dp)
                    )
                }
            }

        }
        HorizontalPager(
            state = pagerState,
            pageSpacing = 8.dp
        ) {
            val pages = (1..15).map { "Page $it" }
            PageScreen(
                items = pages
            )
        }
    }
}

@Composable
fun PageScreen(
    items: List<String>
) {
    Column() {
        items.forEach { item ->
            Text(
                text = item,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxSize()
                    .padding(32.dp)
            )
        }
    }
}

@Preview
@Composable
fun PagerPreview() {
    HorizontalPagerTabRowSample()
}