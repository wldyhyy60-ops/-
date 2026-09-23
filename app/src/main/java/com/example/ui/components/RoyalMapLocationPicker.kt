package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkGold
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.util.GeocodedAddress
import com.example.util.LocationAndGeocodingHelper
import kotlinx.coroutines.launch

/**
 * Interactive Google Maps / Location Picker Dialog for Yemen.
 * Provides interactive map dragging, pin placement, GPS current location lookup,
 * automatic reverse geocoding into Yemen address parts, and direct Google Maps links.
 */
@Composable
fun RoyalMapLocationPicker(
    initialLatitude: Double? = null,
    initialLongitude: Double? = null,
    onDismiss: () -> Unit,
    onLocationSelected: (GeocodedAddress) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Default coordinates: Sana'a center if not provided
    var currentLat by remember { mutableDoubleStateOf(initialLatitude ?: 15.3694) }
    var currentLng by remember { mutableDoubleStateOf(initialLongitude ?: 44.1910) }

    var isLocating by remember { mutableStateOf(false) }
    var isGeocoding by remember { mutableStateOf(false) }
    var geocodedAddress by remember { mutableStateOf<GeocodedAddress?>(null) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    // Reverse geocode whenever coordinates change
    fun updateCoordinates(lat: Double, lng: Double, reloadMap: Boolean = false) {
        currentLat = lat
        currentLng = lng
        isGeocoding = true
        coroutineScope.launch {
            val addr = LocationAndGeocodingHelper.reverseGeocode(context, lat, lng)
            geocodedAddress = addr
            isGeocoding = false
        }
        if (reloadMap) {
            webViewRef?.evaluateJavascript("if (window.setCenter) { window.setCenter($lat, $lng); }", null)
        }
    }

    LaunchedEffect(Unit) {
        updateCoordinates(currentLat, currentLng)
    }

    // Permission launcher for GPS
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            isLocating = true
            coroutineScope.launch {
                val loc = LocationAndGeocodingHelper.getCurrentLocation(context)
                isLocating = false
                if (loc != null) {
                    updateCoordinates(loc.first, loc.second, reloadMap = true)
                    Toast.makeText(context, "تم تحديد موقعك بدقة 🎯", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "تعذر تحديد الموقع بدقة، يرجى تحريك الدبوس على الخريطة", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context, "يرجى منح إذن الموقع لتحديد موقعك تلقائياً", Toast.LENGTH_SHORT).show()
        }
    }

    fun requestCurrentLocation() {
        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (hasFine || hasCoarse) {
            isLocating = true
            coroutineScope.launch {
                val loc = LocationAndGeocodingHelper.getCurrentLocation(context)
                isLocating = false
                if (loc != null) {
                    updateCoordinates(loc.first, loc.second, reloadMap = true)
                    Toast.makeText(context, "تم تحديد موقعك بدقة 🎯", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "تعذر التقاط إشارة GPS. يمكنك تحريك الخريطة لتحديد موقعك.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(RoyalBlack)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top App Bar
                Surface(
                    color = DarkSurface,
                    shadowElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "📍 تحديد موقع التوصيل على الخريطة",
                                color = TextGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "حرك الخريطة لوضع الدبوس بدقة على منزلك",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        IconButton(onClick = { requestCurrentLocation() }) {
                            if (isLocating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = GoldPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(Icons.Default.MyLocation, contentDescription = "موقعي", tint = GoldPrimary)
                            }
                        }
                    }
                }

                // Quick City Selectors
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkCard)
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(LocationAndGeocodingHelper.YEMEN_CITIES_COORDINATES.toList()) { (city, coords) ->
                        val isSelected = geocodedAddress?.governorate == city ||
                                (Math.abs(currentLat - coords.first) < 0.1 && Math.abs(currentLng - coords.second) < 0.1)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) GoldPrimary else DarkSurface)
                                .clickable {
                                    updateCoordinates(coords.first, coords.second, reloadMap = true)
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = city,
                                color = if (isSelected) RoyalBlack else Color.White,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Map View with Center Pin
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    // Leaflet OpenStreetMap HTML
                    val mapHtml = remember(currentLat, currentLng) {
                        """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="utf-8" />
                            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
                            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                            <style>
                                body { margin: 0; padding: 0; background: #0b0f19; }
                                #map { width: 100vw; height: 100vh; background: #0b0f19; }
                                .leaflet-control-zoom { display: none; }
                                .leaflet-control-attribution { font-size: 8px; background: rgba(0,0,0,0.5) !important; color: #aaa !important; }
                            </style>
                        </head>
                        <body>
                            <div id="map"></div>
                            <script>
                                var map = L.map('map', {
                                    center: [$currentLat, $currentLng],
                                    zoom: 15,
                                    zoomControl: false
                                });
                                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                                    maxZoom: 19
                                }).addTo(map);

                                map.on('moveend', function() {
                                    var center = map.getCenter();
                                    if (window.AndroidInterface) {
                                        window.AndroidInterface.onMapMoved(center.lat, center.lng);
                                    }
                                });

                                window.setCenter = function(lat, lng) {
                                    map.setView([lat, lng], 16);
                                };

                                window.zoomIn = function() { map.zoomIn(); };
                                window.zoomOut = function() { map.zoomOut(); };
                            </script>
                        </body>
                        </html>
                        """.trimIndent()
                    }

                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                settings.loadWithOverviewMode = true
                                settings.useWideViewPort = true
                                webViewClient = WebViewClient()
                                addJavascriptInterface(object {
                                    @JavascriptInterface
                                    fun onMapMoved(lat: Double, lng: Double) {
                                        (ctx as? android.app.Activity)?.runOnUiThread {
                                            updateCoordinates(lat, lng, reloadMap = false)
                                        }
                                    }
                                }, "AndroidInterface")
                                loadDataWithBaseURL("https://openstreetmap.org", mapHtml, "text/html", "UTF-8", null)
                                webViewRef = this
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Fixed Center Pin Overlay
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(bottom = 36.dp) // Pin tip at exact center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = DarkCard.copy(alpha = 0.95f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                                shadowElevation = 8.dp
                            ) {
                                Text(
                                    text = "📍 موقع التوصيل",
                                    color = TextGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "موقع التوصيل",
                                tint = Color(0xFFE53935),
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }

                    // Floating Zoom Controls
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FloatingIconButton(
                            icon = Icons.Default.Add,
                            contentDescription = "تكبير",
                            onClick = { webViewRef?.evaluateJavascript("window.zoomIn();", null) }
                        )
                        FloatingIconButton(
                            icon = Icons.Default.Remove,
                            contentDescription = "تصغير",
                            onClick = { webViewRef?.evaluateJavascript("window.zoomOut();", null) }
                        )
                    }

                    // Floating "My Location" Button
                    Surface(
                        shape = CircleShape,
                        color = DarkSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(52.dp)
                            .clip(CircleShape)
                            .clickable { requestCurrentLocation() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (isLocating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = GoldPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = "تحديد موقعي الحالي",
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                }

                // Bottom Sheet Details Card
                Card(
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Map,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "العنوان المستخرج من الخريطة",
                                    color = TextGold,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (isGeocoding) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = GoldPrimary,
                                    strokeWidth = 2.dp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Address Preview
                        val addr = geocodedAddress
                        val displayAddress = addr?.fullFormattedAddress?.ifEmpty { "جاري تحديد العنوان..." }
                            ?: "الجمهورية اليمنية، إحداثيات: (${String.format("%.4f", currentLat)}, ${String.format("%.4f", currentLng)})"

                        Text(
                            text = displayAddress,
                            color = Color.White,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Coordinates and Google Maps Link preview
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📍 ${String.format("%.4f", currentLat)}, ${String.format("%.4f", currentLng)}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )

                            Text(
                                text = "🗺️ فتح في Google Maps",
                                color = GoldPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .clickable {
                                        LocationAndGeocodingHelper.openInGoogleMaps(context, currentLat, currentLng)
                                    }
                                    .padding(4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, TextMuted),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("إلغاء", fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    val finalAddress = geocodedAddress ?: GeocodedAddress(
                                        country = "الجمهورية اليمنية",
                                        governorate = "صنعاء",
                                        city = "صنعاء",
                                        fullFormattedAddress = "الجمهورية اليمنية",
                                        latitude = currentLat,
                                        longitude = currentLng
                                    )
                                    onLocationSelected(finalAddress)
                                },
                                modifier = Modifier.weight(2f),
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = RoyalBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "تأكيد هذا الموقع ✅",
                                    color = RoyalBlack,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = DarkSurface.copy(alpha = 0.9f),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.7f)),
        shadowElevation = 4.dp,
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = GoldPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
