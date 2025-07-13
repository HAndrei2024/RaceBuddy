package com.example.racebuddy.ui.v2.organizer.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Event(
    val name: String,
    val participants: Int
)

@Composable
fun EventList(events: List<Event>) {
    val maxParticipants = events.maxOfOrNull { it.participants } ?: 1

    LazyColumn {
        items(events) {event ->
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {

                Text("${event.name}", fontWeight = FontWeight.Bold)
                Text("Participants: ${event.participants}")

                LinearProgressIndicator(
                    progress = event.participants / maxParticipants.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(top = 4.dp),
                    color = Color(0xFF3F51B5)
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}


@Preview
@Composable
fun EventListPreview() {
    EventListHorizontal(
        events = listOf(Event("e1", 10),Event("e2", 20),Event("e3", 15),Event("e4", 12),Event("e5", 7) )
    )
}

@Composable
fun EventListHorizontal(events: List<Event>) {
    val maxParticipants = events.maxOfOrNull { it.participants } ?: 1

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(events) { event ->
            Card(
                modifier = Modifier
                    .width(180.dp)
                    .height(160.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${event.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Circular indicator to represent participant count visually
                        CircularProgressIndicator(
                            progress = event.participants / maxParticipants.toFloat(),
                            modifier = Modifier.size(40.dp),
                            strokeWidth = 4.dp,
                            color = Color(0xFF3F51B5)
                        )

                        Text(
                            text = "${event.participants} participants",
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
