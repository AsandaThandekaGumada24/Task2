package com.example.opsctask1screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.opsctask1screens.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class FirstPage : Fragment(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_page, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Check for and request location permission if needed
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            // If permission is granted, initialize location detection
            detectLocation()
        }

        val detectLocationButton = view.findViewById<Button>(R.id.detectLocationButton)
        detectLocationButton.setOnClickListener { detectLocation() }

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    private fun detectLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Get the user's last known location
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val lat = location.latitude
                    val lng = location.longitude


                    // Move the map camera to the user's location
                    val userLocation = LatLng(lat, lng)
                    googleMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f)) // Adjust the zoom level as needed

                    // Detect the locId based on latitude and longitude
                    detectLocId(lat, lng)
                }
            }
        }
    }

    private fun detectLocId(lat: Double, lng: Double) {
        val client = OkHttpClient.Builder().build()
        val url = "https://api.ebird.org/v2/ref/hotspot/geo?lat=$lat&lng=$lng"
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("X-eBirdApiToken", "6gclai1l9b4t") // Replace with your eBird API key
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()

                responseData?.let {
                    parseLocId(responseData)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    private fun parseLocId(responseData: String) {
        try {
            val locId = JSONObject(responseData).getString("locId")
            // Fetch hotspot information using locId
            fetchHotspotInfo(locId)
        } catch (e: JSONException) {
            e.printStackTrace()
            // Handle the JSON parsing error
        }
    }

    private fun fetchHotspotInfo(locId: String) {
        val client = OkHttpClient.Builder().build()
        val url = "https://api.ebird.org/v2/ref/hotspot/info/$locId"
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("X-eBirdApiToken", "6gclai1l9b4t") // Replace with your eBird API key
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()

                responseData?.let {
                    parseHotspotInfo(responseData)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    private fun parseHotspotInfo(responseData: String) {
        try {
            val hotspotObject = JSONObject(responseData)
            val name = hotspotObject.getString("name")
            val lat = hotspotObject.getDouble("lat")
            val lng = hotspotObject.getDouble("lng")

            // Add hotspot location to the map
            val hotspotLocation = LatLng(lat, lng)
            googleMap.addMarker(MarkerOptions().position(hotspotLocation).title(name))
        } catch (e: JSONException) {
            e.printStackTrace()
            // Handle the JSON parsing error
        }
    }
}
