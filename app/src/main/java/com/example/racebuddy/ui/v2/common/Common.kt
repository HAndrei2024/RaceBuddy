package com.example.racebuddy.ui.v2.common

import android.media.Image
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.racebuddy.R
import com.example.racebuddy.data.database.Event
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.testEvent
import com.example.racebuddy.ui.theme.AppTypography
import com.example.racebuddy.ui.theme.heights
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.theme.sizes
import com.example.racebuddy.ui.v2.signup.Country
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.exp

@Composable
fun CustomTextField(
    textStringValue: String,
    placeholderString: String,
    iconImageVector: ImageVector,
    onValueChange: (String) -> Unit,
    supportingText:@Composable() (() -> Unit)? = null,
    onFocusChanged: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    var isPlaceholder by remember { mutableStateOf(true) }



    OutlinedTextField(
        value = textStringValue,
        textStyle = AppTypography.bodyLarge.copy(
            color = if (textStringValue == placeholderString) {
                MaterialTheme.colorScheme.onSurface
            } else {
                Color.Black
            }
        ),
        placeholder = {
            Text(
                text = if(!hasFocus) placeholderString else "",
            )
        },
        onValueChange = {
            onValueChange(it)
            isPlaceholder = false
        },
        shape = shapes.small,
        leadingIcon = {
            Icon(
                imageVector = iconImageVector,
                contentDescription = ""
            )
        },
        supportingText = supportingText,
        modifier = modifier
            .padding(
                bottom = paddings.spacingSmall,
                top = paddings.spacingSmall
            )
            .onFocusChanged { focusState ->

                // If focus was lost
                if (hasFocus && !focusState.isFocused) {
                    onFocusChanged()
                }
                hasFocus = focusState.isFocused
            }

    )

}

@Composable
fun ErrorText(
    textString: String = "Incorrect email or password."
) {
    Text(
        text = textString,
        style = AppTypography.bodyLarge,
        color = Color.Red
    )
}

@Composable
fun LoadingAnimation(
    onFinishLoadingAnimation: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Card(
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.size(80.dp)

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))

                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onNavigationClick: () -> Unit,
    showBackNavigation: Boolean
) {
    TopAppBar(
        title = { Text(text = "") },
        navigationIcon = {
            if(showBackNavigation) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, // or Icons.Default.Menu
                        contentDescription = "Back"
                    )
                }
            }
    },
        colors = TopAppBarColors(
            containerColor = Color.White,
            scrolledContainerColor = Color.White,
            navigationIconContentColor = Color.Black,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black
    )


    )
}

@Composable
fun EventCardChat(
    imageUrl: String,
    title: String,
    startDate: String,
    endDate: String,
    country: String,
    city: String,
    flagUrl: String? = null,
    isFavorite: Boolean,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "$startDate - $endDate", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    flagUrl?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = "Country flag",
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(text = "$city, $country", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun EventCardV2(
    eventInfo: EventInfo,
    modifier: Modifier = Modifier
) {
    Card(
        shape = shapes.medium,
        elevation = CardDefaults.cardElevation(paddings.spacingSmall),
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.default_background),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
            )

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(2f)
            ) {
                Text(
                    text = eventInfo.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(5.dp)
                )
                Text(
                    text = eventInfo.county + ", " + eventInfo.city,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(5.dp)
                )
                Text(
                    text = eventInfo.startDate.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }
        }
    }
}


@Composable
fun EventCardUpdated(
    eventInfo: EventInfo,
    //category: String = "Cycling",
    isUserLoggedIn: Boolean,
    countryCodeEmoji: String = "" ,
    favoriteIcon: Painter = painterResource(R.drawable.baseline_favorite_border_24),
    onFavoriteIconClick: () -> Unit = {},
    onEventClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        //border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onEventClick() }
            .padding(paddings.spacingSmall)
            .height(120.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = eventInfo.backgroundPictureUrl.takeIf { it.isNotBlank() },
                contentDescription = "Profile Picture",
                placeholder = painterResource(R.drawable.default_background),
                error = painterResource(R.drawable.default_background),
                fallback = painterResource(R.drawable.default_background),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(paddings.spacingSmall)
                    .clip(RoundedCornerShape(paddings.spacingSmall))
                    .weight(2f)
                    .height(heights.extraLarge)
                    .width(sizes.large)
                    //.border(1.dp, MaterialTheme.colorScheme.primary)
            )
//            Image(
//                painter = painterResource(R.drawable.ic_launcher_background),
//                contentDescription = "",
//                modifier = Modifier
//                    .padding(paddings.spacingSmall)
//                    .clip(RoundedCornerShape(paddings.spacingSmall))
//                    .weight(2f)
//
//            )
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(top = paddings.spacingXSmall)
                    .weight(3f)
            ) {

                Column() {
                    Text(
                        text = eventInfo.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(top = paddings.spacingXSmall)
                    )

                    Text(
                        text = " ${eventInfo.startDate.dayOfMonth} - ${eventInfo.endDate.dayOfMonth} ${
                            eventInfo.startDate.month.name.lowercase()
                                .replaceFirstChar { it.uppercase() }
                        } ${eventInfo.startDate.year}",
                        maxLines = 1,
                        color = Color.Black,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.bodyMedium,
//                    color = Color.Gray,
//                    fontWeight = FontWeight.Bold,
                        modifier = Modifier
                        //.padding(5.dp)
                    )
                }

                Spacer(
                    Modifier.padding(
                    top = paddings.spacingXSmall,
                    bottom = paddings.spacingXSmall
                    )
                )

                Text(
                    text = eventInfo.category,
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50)) // Fully rounded corners = oval effect
                        .background(Color.LightGray)
                        .padding(horizontal = 12.dp, vertical = 4.dp)// Custom background color
//                        .padding(
//                            top = paddings.spacingXSmall,
//                            bottom = paddings.spacingMedium
//                        )
                )


                Row() {
                    Text(
                        text = "📍 " + eventInfo.county + ", " + eventInfo.city + "   " + countryCodeEmoji,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .padding(top = paddings.spacingSmall)
                    )



                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = if(isUserLoggedIn) Arrangement.Top else Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .align(Alignment.Top)
            ) {
                if(isUserLoggedIn) {
                    IconButton(
                        onClick = onFavoriteIconClick,
                        enabled = isUserLoggedIn,
                        modifier = Modifier.alpha(if (isUserLoggedIn) 1f else 0f)
                    ) {
                        Icon(
                            painter = favoriteIcon,
                            contentDescription = ""
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow Right", // For accessibility tools
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(
                            top = paddings.spacingSmall,
                            end = paddings.spacingSmall
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopAppBar(
    onSearchIconClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
    showSearchIcon: Boolean = true
) {
    TopAppBar(
       title = {
           Row(
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically,
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(end = paddings.spacingSmall)
           ) {
               Text(
                   text = "RaceBuddy",
                   style = MaterialTheme.typography.titleMedium,
                   fontWeight = FontWeight.Normal,
                   modifier = Modifier
                       .padding(start = paddings.spacingXSmall)
               )
//               Row() {
////                   Icon(
////                       painterResource(R.drawable.baseline_pedal_bike_24),
////                       contentDescription = "",
////                       modifier = Modifier
////                           .padding(end = paddings.spacingXSmall)
////                   )
////
//
//
////                   Icon(
////                       imageVector = Icons.Filled.ArrowDropDown,
////                       contentDescription = "",
////                       modifier = Modifier
////                   )
//               }

               Row(
                   horizontalArrangement = Arrangement.SpaceEvenly
               ) {

                   if(showSearchIcon) {
                       IconButton(
                           onClick = onSearchIconClick,
                       ) {
                           Icon(
                               imageVector = Icons.Filled.Search,
                               contentDescription = "",
                               modifier = Modifier
                                   .padding(end = paddings.spacingSmall)
                           )
                       }
                   }
                   IconButton(
                       onClick = onSettingsIconClick
                   ) {
                       Icon(
                           imageVector = Icons.Filled.Settings,
                           contentDescription = "",
                           modifier = Modifier
                               .padding(end = paddings.spacingXSmall)
                       )
                   }
               }
           }
       },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White, //Color(0xFFF5F5F5),
            titleContentColor = Color.Black,
            //actionIconContentColor = Color(0xFF4169E1)
        ),
        modifier = Modifier
            .border(1.dp, Color(0xFFEEEEEE))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithCategoryAndSearchChat(
    categories: List<String>,
    onCloseClick: () -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit
) {
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

//                        Text(
//                            text = selectedCategory,
//                            style = MaterialTheme.typography.titleSmall,
//                            fontWeight = FontWeight.Normal,
//                            modifier = Modifier
//                                .clickable { categoryMenuExpanded = true }
//                                .padding(horizontal = 8.dp)
//                        )
//                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Category")
//                        DropdownMenu(
//                            expanded = categoryMenuExpanded,
//                            onDismissRequest = { categoryMenuExpanded = false }
//                        ) {
//                            categories.forEach { category ->
//                                DropdownMenuItem(
//                                    text = { Text(text = category) },
//                                    onClick = {
//                                        onCategorySelected(category)
//                                        categoryMenuExpanded = false
//                                    }
//                                )
//                            }
//                        }


                    //Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = onCloseClick,
                        modifier = Modifier
                            .weight(0.2f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = ""
                        )
                    }


                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            onSearchQueryChanged(it)
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



                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF5F5F5),
                titleContentColor = Color.Black,
                //actionIconContentColor = Color(0xFF4169E1)
            ),
            modifier = Modifier
                //.height(heights.medium)
//                .border(
//                    width = 1.dp,
//                    color = Color(0xFF4169E1),
//                    shape = RectangleShape
//                )
        )

        // Subtle Royal Blue bottom line (shadow effect)
        //Divider(color = Color(0xFF4169E1), thickness = 1.dp)

}

@Composable
fun BottomAppBarUpdated(
    isHomeSelected: Boolean = true,
    isFavoriteSelected: Boolean = false,
    isProfileSelected: Boolean = false,
    onHomeClick: () -> Unit = { },
    onFavoriteClick: () -> Unit = { },
    onProfileClick: () -> Unit = { },
    isFavoritesVisible: Boolean = true
) {
    BottomAppBar(
        containerColor = Color.White,//Color(0xFFF5F5F5),
        tonalElevation = 5.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BottomBarIconUpdated(
                isSelected = isHomeSelected,
                selectedImage = R.drawable.baseline_home_24,
                unselectedImage = R.drawable.outline_home_24,
                onClick = onHomeClick
            )

            if(isFavoritesVisible) {
                BottomBarIconUpdated(
                    isSelected = isFavoriteSelected,
                    selectedImage = R.drawable.baseline_favorite_24,
                    unselectedImage = R.drawable.baseline_favorite_border_24,
                    onClick = onFavoriteClick
                )
            }

            BottomBarIconUpdated(
                isSelected = isProfileSelected,
                selectedImage = R.drawable.baseline_person_24,
                unselectedImage = R.drawable.outline_person_24,
                onClick = onProfileClick
            )
        }
    }
}


@Composable
fun BottomNavigationBarChat(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    isFavoritesVisible: Boolean = true
) {
    val royalBlue = Color(0xFF4169E1)

    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = Color(0xFFDDDDDD),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
            }
        //tonalElevation = 8.dp
    ) {
        val items = if(isFavoritesVisible) listOf(
            Pair("Home", Icons.Default.Home),
            Pair("Favorite", Icons.Default.Favorite),
            Pair("Profile", Icons.Default.Person)
        ) else listOf(
            Pair("Home", Icons.Default.Home),
            Pair("Profile", Icons.Default.Person)
        )

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.second,
                        contentDescription = item.first
                    )
                },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    //unselectedIconColor = Color.Gray,
                    indicatorColor = royalBlue.copy(alpha = 0.12f) // optional background circle
                ),
                label = {
                    Text(
                        text = item.first,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Normal
                    )
                }
            )
        }
    }
}



@Composable
fun BottomBarIconUpdated(
    isSelected: Boolean,
    selectedImage: Int,
    unselectedImage: Int,
    contentDescription: String = "",
    onClick: () -> Unit = { }
) {
    val iconPainter = if(isSelected) {
        painterResource(selectedImage)
    }
    else {
        painterResource(unselectedImage)
    }

    IconButton(
        onClick = onClick
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun TestElevation() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().zIndex(1f),
            elevation = CardDefaults.elevatedCardElevation(10.dp)
        ) {
            Text(
                text = "This is a text!"
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth().zIndex(2f),
            elevation = CardDefaults.elevatedCardElevation(10.dp)
        ) {
            Text(
                text = "This is a text!"
            )
        }
    }
}

@Composable
fun LoadingWithCheckAnimation(
    onFinishLoadingAnimation: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var showCheck by remember { mutableStateOf(false) }

    // Simulate loading
    LaunchedEffect(Unit) {
        delay(500) // loading duration
        isLoading = false
        showCheck = true
        delay(500) // check visible for 1 second
        // Optionally do something else after

        onFinishLoadingAnimation()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
        }

        // Check icon
        AnimatedVisibility(visible = showCheck) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isAthleteLoggedIn: Boolean,
    areAthleteDetailsFilled: Boolean,
    athleteName: String,
    onBackIconClick: () -> Unit,
    onLogInOutButtonClick: () -> Unit,
    onDetailsButtonClick: () -> Unit,
    onCreateOrganizerAccountClick: () -> Unit,
    ) {

    Scaffold(
        topBar = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(paddings.spacingXSmall)
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = onBackIconClick
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = ""
                    )
                }
            }
        },
        bottomBar = {

        },
        containerColor = Color.White,
        contentColor = Color.Black
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                //.fillMaxHeight(1f)
                .padding(innerPadding)
        ) {
            item {
                Text(
                    text = "⚙\uFE0F Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.2.sp,
                    modifier = Modifier
                        .padding(paddings.spacingMedium)
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            val y =
                                size.height - strokeWidth / 2  // Adjust for stroke centering
//                            drawLine(
//                                color = Color(0xFFDDDDDD),
//                                start = Offset(0f, y),
//                                end = Offset(size.width, y),
//                                strokeWidth = strokeWidth
//                            )
                        }
                )
            }

            item {
                UserSettingsRows(
                    isAthleteLoggedIn = isAthleteLoggedIn,
                    areAthleteDetailsFilled = areAthleteDetailsFilled,
                    name = athleteName,
                    onLogInOutButtonClick = onLogInOutButtonClick,
                    onDetailsButtonClick = onDetailsButtonClick,
                    modifier = Modifier
                )
            }

            item {
                GeneralSettingsRows(
                    isAthleteLoggedIn = isAthleteLoggedIn,
                    onCreateOrganizerAccountClick = onCreateOrganizerAccountClick,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun OrganizerSettingsScreen(
    areOrganizerDetailsFilled: Boolean,
    organizerName: String,
    onBackIconClick: () -> Unit,
    onLogInOutButtonClick: () -> Unit,
    onDetailsButtonClick: () -> Unit,
    onCreateOrganizerAccountClick: () -> Unit,
) {

    Scaffold(
        topBar = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(paddings.spacingXSmall)
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = onBackIconClick
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = ""
                    )
                }
            }
        },
        bottomBar = {

        },
        containerColor = Color.White,
        contentColor = Color.Black
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                //.fillMaxHeight(1f)
                .padding(innerPadding)
        ) {
            item {
                Text(
                    text = "⚙\uFE0F Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.2.sp,
                    modifier = Modifier
                        .padding(paddings.spacingMedium)
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            val y =
                                size.height - strokeWidth / 2  // Adjust for stroke centering
//                            drawLine(
//                                color = Color(0xFFDDDDDD),
//                                start = Offset(0f, y),
//                                end = Offset(size.width, y),
//                                strokeWidth = strokeWidth
//                            )
                        }
                )
            }

            item {
                UserSettingsRows(
                    isAthleteLoggedIn = false,
                    areAthleteDetailsFilled = areOrganizerDetailsFilled,
                    name = organizerName,
                    onLogInOutButtonClick = onLogInOutButtonClick,
                    onDetailsButtonClick = onDetailsButtonClick,
                    modifier = Modifier
                )
            }

            item {

            }
        }
    }
}

@Composable
fun UserSettingsRows(
    isAthleteLoggedIn: Boolean,
    areAthleteDetailsFilled: Boolean,
    name: String,
    onLogInOutButtonClick: () -> Unit,
    onDetailsButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = paddings.spacingMedium, start = paddings.spacingSmall, end = paddings.spacingSmall)
            .fillMaxWidth()
    ) {
        Text(
            text = "Athlete",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(start = paddings.spacingXSmall, end = paddings.spacingXSmall, bottom = paddings.spacingXSmall)
        )

       UserSettingsRow(
           icon = Icons.Default.Person,
           detailsText = if(isAthleteLoggedIn) name else "Not logged in",
           buttonText = if(isAthleteLoggedIn) "Log Out" else "Log In",
           onButtonClick = onLogInOutButtonClick,
           isButtonEnabled = true,
           modifier = Modifier,
       )

        UserSettingsRow(
            icon = Icons.Default.Info,
            detailsText = "Personal Info",
            buttonText = "Details",
            onButtonClick = onDetailsButtonClick,
            isButtonEnabled = isAthleteLoggedIn && !areAthleteDetailsFilled,
            modifier = Modifier
        )

    }
}

@Composable
fun GeneralSettingsRows(
    isAthleteLoggedIn: Boolean,
    onCreateOrganizerAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = paddings.spacingMedium, start = paddings.spacingSmall, end = paddings.spacingSmall)
            .fillMaxWidth()
    ) {
        Text(
            text = "General",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(
                    start = paddings.spacingXSmall,
                    end = paddings.spacingXSmall,
                    bottom = paddings.spacingXSmall
                )
        )

        UserSettingsRow(
            icon = Icons.Default.Person,
            detailsText = "Create an Organizer Account",
            buttonText = "Create",
            onButtonClick = onCreateOrganizerAccountClick,
            isButtonEnabled = !isAthleteLoggedIn,
            modifier = Modifier,
        )
    }
}

@Composable
fun UserSettingsRow(
    icon: ImageVector,
    detailsText: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    isButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            //.padding(top = paddings.spacingMedium, start = paddings.spacingSmall, end = paddings.spacingSmall)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    drawLine(
                        color = Color(0xFFDDDDDD),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidth
                    )
                }
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val y =
                        size.height - strokeWidth / 2  // Adjust for stroke centering
                    drawLine(
                        color = Color(0xFFDDDDDD),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                }
                .padding(paddings.spacingXSmall)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                modifier = Modifier
                    .weight(5f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .offset(y = (-2).dp)
                    //.weight(1f)
                )

                Text(
                    text = detailsText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                    //.weight(1f)
                )
            }

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                enabled = isButtonEnabled,
                elevation = ButtonDefaults.buttonElevation(2.dp),
                shape = RoundedCornerShape(shapes.small.topEnd),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                onClick = onButtonClick,
                modifier = Modifier
                .weight(1.5f)
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingsBottomSheetPreview() {
    SettingsScreen(
        athleteName = "Not logged in",
        isAthleteLoggedIn = false,
        onLogInOutButtonClick = {},
        onDetailsButtonClick = {},
        onBackIconClick = {},
        areAthleteDetailsFilled = false,
        onCreateOrganizerAccountClick = {}
    )
}


@Preview
@Composable
fun TestElevationPreview() {
    TestElevation()
}

@Preview
@Composable
fun TopBarPreview() {
    TopBarWithCategoryAndSearchChat(
        categories = listOf("XC", "Downhill", "Road", "All"),
        selectedCategory = "All",
        onCategorySelected = {},
        onSearchQueryChanged = {},
        onCloseClick = {}
    )

}

@Preview
@Composable
fun BottomBar() {
    BottomAppBarUpdated()

    BottomNavigationBarChat(0, {})
}


@Preview
@Composable
fun MainScreenTopAppBarPreview() {
    MainScreenTopAppBar(
        onSearchIconClick = {},
        onSettingsIconClick = {}
    )
}

@Preview
@Composable
fun EventCardPreview() {
//    EventCardV2(
//        eventInfo = testEvent
//    )
    EventCardUpdated(
        eventInfo = testEvent,
        //category = "XC",
        isUserLoggedIn = true,
        favoriteIcon = painterResource(R.drawable.baseline_favorite_24),
        onFavoriteIconClick = {},
        onEventClick = {}
    )
}



////@Preview(showBackground = true)
////@Composable
////fun EventPreviewCardPreview() {
////    MaterialTheme {
////        EventCardChat(
////            imageUrl = "https://via.placeholder.com/600x400.png?text=Event+Image",
////            title = "Music Festival 2025",
////            startDate = "June 21, 2025",
////            endDate = "June 23, 2025",
////            country = "Germany",
////            city = "Berlin",
////            flagUrl = "https://flagcdn.com/w40/de.png",
////            isFavorite = true,
////            onCardClick = {},
////            onFavoriteClick = {}
////        )
////    }
////}
////
////
////
////@Preview
////@Composable
////fun TopBarPreview() {
////    TopBar(
////        onNavigationClick = {},
////        showBackNavigation = false
////    )
//}