import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.racebuddy.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.osmdroid.tileprovider.tilesource.TileSourceFactory


import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.config.Configuration
import android.content.Context

import android.location.Geocoder
import android.location.Address

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.size
import org.osmdroid.tileprovider.tilesource.XYTileSource
import java.util.Locale

@SuppressLint("MissingPermission")
@Composable
fun MapScreen() {
    var markerLocation by remember { mutableStateOf<LatLng?>(null) }

    var showWeatherCard by remember { mutableStateOf(false) }

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    val initialLatLng = LatLng(53.428543, 14.552812) // Replace with your desired latitude and longitude
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLatLng, 12f) // Adjust zoom level as needed
    }

    val context = LocalContext.current

    // Initialize location client
    val fusedLocationProviderClient: FusedLocationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }

    // Fetch current location on first launch
    LaunchedEffect(Unit) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                currentLocation = LatLng(it.latitude, it.longitude)
                // Center the camera on the current location
                cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation!!, 13.5f)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                markerLocation = latLng
                showWeatherCard = true
            },
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true, // Enables zoom buttons
                myLocationButtonEnabled = false, // Enables the "My Location" button
                compassEnabled = true // Enables the compass on the map
            ),
            properties = com.google.maps.android.compose.MapProperties(
                isMyLocationEnabled = true // Shows the default blue location indicator
            )
        )

        currentLocation?.let {
            androidx.compose.material3.FloatingActionButton(
                onClick = {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
                },
                modifier = Modifier
                    .align(Alignment.BottomStart) // Align to the bottom end (right)
                    .padding(16.dp)
                    .padding(start = 10.dp), // Additional padding to move it further to the right
                containerColor = Color(0xFF87CEEB)
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.outline_location_on_40), // Replace with your drawable's name
                    contentDescription = "My Location",
                    modifier = Modifier.size(24.dp), // Adjust size as needed
                )
            }
        }


        // Display a prompt to click anywhere on the map to show the weather
        if (!showWeatherCard) {
            Text(
                text = "Click anywhere on the map to show the weather",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .background(Color(0xFF87CEEB).copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Weather card displayed when a location is selected

    }
}

@Composable
fun BasicMapScreen() {
    // Initialize the camera position state, which controls the camera's position on the map
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(51.5074, -0.1278), 10f)
    }

    // Create a marker to place on the map
    val marker = MarkerState(position = LatLng(51.5074, -0.1278))

    // Display the Google Map with a marker
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Add a marker at the specified position
        Marker(state = marker)
    }
}


@Composable
fun OSMMapScreen(context: Context) {
    val mapView = remember { MapView(context) }

    // Configure the map settings
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

    mapView.apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(true)
        controller.setZoom(10)
        controller.setCenter(GeoPoint(51.5074, -0.1278))  // London coordinates
    }

    // Display the map
    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    )
}



@Composable
fun UpdatedOSMMapScreen(context: Context, address: String) {
    // Geocode the address string to get latitude and longitude
    val geoCoder = Geocoder(context, Locale.getDefault())
    val addresses: MutableList<Address>? = geoCoder.getFromLocationName(address, 1)

    // Get the latitude and longitude from the geocoded address
    val latitude = addresses?.firstOrNull()?.latitude
    val longitude = addresses?.firstOrNull()?.longitude

    // Default to a location if the geocoding fails
    val defaultLatitude = latitude ?: 51.5074 // Default to London if address not found
    val defaultLongitude = longitude ?: -0.1278

    // Create a MapView
    val mapView = remember { MapView(context) }

    // Configure the map settings
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

    mapView.apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(true)
        controller.setZoom(10)
        controller.setCenter(GeoPoint(defaultLatitude, defaultLongitude))  // Set to geocoded coordinates
    }


    // Display the map
    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    )
}


