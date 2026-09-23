package com.example.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

data class GeocodedAddress(
    val country: String = "الجمهورية اليمنية",
    val governorate: String = "",
    val city: String = "",
    val district: String = "",
    val street: String = "",
    val fullFormattedAddress: String = "",
    val latitude: Double,
    val longitude: Double
) {
    val googleMapsUrl: String
        get() = "https://www.google.com/maps?q=$latitude,$longitude"
}

object LocationAndGeocodingHelper {

    // Major Yemeni governorates list
    val YEMEN_GOVERNORATES = listOf(
        "صنعاء",
        "إب",
        "تعز",
        "عدن",
        "الحديدة",
        "حضرموت",
        "ذمار",
        "مأرب",
        "صعدة",
        "حجة",
        "البيضاء",
        "لحج",
        "أبين",
        "شبوة",
        "المهرة",
        "ريمة",
        "المحويت",
        "عمران",
        "الضالع",
        "سقطرى"
    )

    // Major reference points for quick navigation
    val YEMEN_CITIES_COORDINATES = mapOf(
        "صنعاء" to Pair(15.3694, 44.1910),
        "إب" to Pair(13.9785, 44.1706),
        "تعز" to Pair(13.5789, 44.0183),
        "عدن" to Pair(12.7855, 45.0187),
        "الحديدة" to Pair(14.7978, 42.9545),
        "المكلا" to Pair(14.5425, 49.1242),
        "ذمار" to Pair(14.5427, 44.4051),
        "مأرب" to Pair(15.4597, 45.3253)
    )

    fun buildGoogleMapsUrl(latitude: Double, longitude: Double): String {
        return "https://www.google.com/maps?q=$latitude,$longitude"
    }

    fun openInGoogleMaps(context: Context, latitude: Double, longitude: Double, label: String = "موقع العميل") {
        try {
            val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($label)")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to browser URL
            val webUri = Uri.parse(buildGoogleMapsUrl(latitude, longitude))
            val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(webIntent)
        }
    }

    fun openMapsUrl(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر فتح الخريطة", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Attempts to fetch real device GPS location using FusedLocationProviderClient or LocationManager.
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): Pair<Double, Double>? = withContext(Dispatchers.IO) {
        try {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            val cts = CancellationTokenSource()

            val location: Location? = suspendCancellableCoroutine { continuation ->
                fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                    .addOnSuccessListener { loc ->
                        if (continuation.isActive) continuation.resume(loc)
                    }
                    .addOnFailureListener {
                        if (continuation.isActive) continuation.resume(null)
                    }
            }

            if (location != null) {
                return@withContext Pair(location.latitude, location.longitude)
            }

            // Fallback to LocationManager last known location
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val gpsLoc = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val netLoc = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val best = gpsLoc ?: netLoc
            if (best != null) {
                return@withContext Pair(best.latitude, best.longitude)
            }
        } catch (e: Exception) {
            // Handle gracefully
        }
        null
    }

    /**
     * Reverse geocodes the coordinates into an Arabic Yemeni address using Android Geocoder.
     */
    suspend fun reverseGeocode(
        context: Context,
        latitude: Double,
        longitude: Double
    ): GeocodedAddress = withContext(Dispatchers.IO) {
        var detectedGov = ""
        var detectedCity = ""
        var detectedDistrict = ""
        var detectedStreet = ""
        var fullAddress = ""

        try {
            val geocoder = Geocoder(context, Locale("ar", "YE"))
            val addresses: List<Address>? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { cont ->
                    geocoder.getFromLocation(latitude, longitude, 1) { result ->
                        if (cont.isActive) cont.resume(result)
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(latitude, longitude, 1)
            }

            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                detectedGov = detectGovernorateFromText(addr.adminArea ?: "")
                detectedCity = addr.locality ?: addr.subAdminArea ?: ""
                detectedDistrict = addr.subLocality ?: ""
                detectedStreet = addr.thoroughfare ?: ""
                fullAddress = (0..addr.maxAddressLineIndex)
                    .mapNotNull { addr.getAddressLine(it) }
                    .joinToString("، ")
            }
        } catch (e: Exception) {
            // Geocoder service might not be available or network issues
        }

        // Approximate governorate by distance if not detected by Geocoder
        if (detectedGov.isEmpty()) {
            detectedGov = findNearestGovernorate(latitude, longitude)
            if (detectedCity.isEmpty()) detectedCity = detectedGov
        }

        if (fullAddress.isEmpty()) {
            val parts = listOfNotNull(
                "الجمهورية اليمنية",
                detectedGov.ifEmpty { null },
                detectedCity.ifEmpty { null },
                detectedDistrict.ifEmpty { null },
                detectedStreet.ifEmpty { null }
            )
            fullAddress = parts.joinToString("، ")
        }

        GeocodedAddress(
            country = "الجمهورية اليمنية",
            governorate = detectedGov,
            city = detectedCity,
            district = detectedDistrict,
            street = detectedStreet,
            fullFormattedAddress = fullAddress,
            latitude = latitude,
            longitude = longitude
        )
    }

    private fun detectGovernorateFromText(text: String): String {
        for (gov in YEMEN_GOVERNORATES) {
            if (text.contains(gov)) return gov
        }
        return ""
    }

    private fun findNearestGovernorate(lat: Double, lng: Double): String {
        var closest = "صنعاء"
        var minDistance = Double.MAX_VALUE

        for ((gov, coords) in YEMEN_CITIES_COORDINATES) {
            val dLat = lat - coords.first
            val dLng = lng - coords.second
            val dist = dLat * dLat + dLng * dLng
            if (dist < minDistance) {
                minDistance = dist
                closest = gov
            }
        }
        return closest
    }
}
