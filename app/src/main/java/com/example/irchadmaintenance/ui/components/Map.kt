package com.example.irchadmaintenance.ui.components

import android.annotation.SuppressLint
import android.preference.PreferenceManager
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.irchadmaintenance.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.zIndex

@SuppressLint("ClickableViewAccessibility")
@Composable
fun OSMDroidMap(location: String, latitude: Double, longitude: Double) {
    val context = LocalContext.current
    var markerClicked by remember { mutableStateOf(false) }
    var mapExpanded by remember { mutableStateOf(false) }

    // Map reference for controlling zoom after expansion
    val mapRef = remember { mutableStateOf<MapView?>(null) }

    // Keep track of user's touch events so we can detect single taps
    var lastClickTime by remember { mutableStateOf(0L) }
    var touchEvents by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            // Use animateContentSize to smoothly transition between sizes
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        //////////// Floating button to expand / collapse the map ////////////
        IconButton(
            onClick = {
                mapExpanded = !mapExpanded
                mapRef.value?.controller?.setZoom(if (mapExpanded) 17.0 else 15.0)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(48.dp)
                .shadow(4.dp, RoundedCornerShape(24.dp))
                .background(Color.White, RoundedCornerShape(24.dp))
                .zIndex(10f)
        ) {
            Icon(
                imageVector = if (mapExpanded) Icons.Filled.Place else Icons.Filled.Place,
                contentDescription = if (mapExpanded) "Réduire la carte" else "Agrandir la carte",
                tint = Color(0xFF3AAFA9)
            )
        }

        AndroidView(
            factory = { ctx ->
                val map = MapView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setTileSource(TileSourceFactory.MAPNIK)
                    Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
                    controller.setZoom(15.0)
                    setMultiTouchControls(true)

                    val geoPoint = GeoPoint(latitude, longitude)
                    controller.setCenter(geoPoint)

                    val marker = Marker(this)
                    marker.position = geoPoint
                    marker.icon = ctx.getDrawable(R.drawable.marker)
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                    marker.setOnMarkerClickListener { _, _ ->
                        markerClicked = !markerClicked
                        true
                    }

                    overlays.add(marker)

                    // Custom touch listener to detect single taps for map expansion
                    setOnTouchListener { _, event ->
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                touchEvents = 1
                                lastClickTime = System.currentTimeMillis()
                                false
                            }
                            MotionEvent.ACTION_UP -> {
                                val clickTime = System.currentTimeMillis()
                                // If it's a quick tap and not a drag or zoom operation
                                if (clickTime - lastClickTime < 200 && touchEvents < 3) {
                                    // Toggle expansion only if not interacting with marker
                                    val markerBounds = marker.bounds
                                    val x = event.x.toInt()
                                    val y = event.y.toInt()
                                    if (markerBounds == null || !markerBounds.contains(x.toDouble(),
                                            y.toDouble()
                                        )) {
                                        //////////////////// maps normal events like zoom ins //////////
                                    }
                                }
                                touchEvents = 0
                                false
                            }
                            MotionEvent.ACTION_MOVE -> {
                                touchEvents++
                                false
                            }
                            else -> false
                        }
                    }
                }

                mapRef.value = map
                map
            },
            modifier = Modifier
                .fillMaxWidth()
                // Adjust height based on expanded state
                .height(if (mapExpanded) 500.dp else 216.dp)
                .shadow(6.dp, shape = RoundedCornerShape(16.dp), clip = false)
                .clip(RoundedCornerShape(16.dp)),
            update = { mapView ->
                // Update the map when expanded state changes
                if (mapExpanded) {
                    mapView.controller.setZoom(17.0)
                } else {
                    mapView.controller.setZoom(15.0)
                }
            }
        )

        /////////////////// just to display marker info /////////////////
        if (markerClicked) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = location,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color(0xFF3AAFA9))

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Latitude: $latitude",
                        fontSize = 14.sp,
                        color = Color(0xFFAAA3A3))

                    Text(text = "Longitude: $longitude",
                        fontSize = 14.sp,
                        color = Color(0xFFAAA3A3))

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { markerClicked = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF3AAFA9)
                            )
                        ) {
                            Text("Fermer")
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Add toggle for expand/collapse when marker info is showing
                        Button(
                            onClick = {
                                mapExpanded = !mapExpanded
                                mapRef.value?.controller?.setZoom(if (mapExpanded) 17.0 else 15.0)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF17252A)
                            )
                        ) {
                            Text(if (mapExpanded) "Réduire" else "Agrandir")
                        }
                    }
                }
            }
        }
    }
}