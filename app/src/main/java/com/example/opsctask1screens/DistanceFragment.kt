package com.example.opsctask1screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import kotlin.math.round

class DistanceFragment : Fragment() {
    private lateinit var distanceTextView: TextView
    private lateinit var locationNameTextView: TextView

    companion object {
        private const val USER_LATITUDE = "user_latitude"
        private const val USER_LONGITUDE = "user_longitude"
        private const val HOTSPOT_LATITUDE = "hotspot_latitude"
        private const val HOTSPOT_LONGITUDE = "hotspot_longitude"

        fun newInstance(
            userLatitude: Double,
            userLongitude: Double,
            hotspotLatitude: Double,
            hotspotLongitude: Double
        ): DistanceFragment {
            val fragment = DistanceFragment()
            val args = Bundle()
            args.putDouble(USER_LATITUDE, userLatitude)
            args.putDouble(USER_LONGITUDE, userLongitude)
            args.putDouble(HOTSPOT_LATITUDE, hotspotLatitude)
            args.putDouble(HOTSPOT_LONGITUDE, hotspotLongitude)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_distance, container, false)
        distanceTextView = view.findViewById(R.id.distanceTextView)
        locationNameTextView = view.findViewById(R.id.locationNameTextView)

        val userLatitude = arguments?.getDouble(USER_LATITUDE) ?: 0.0
        val userLongitude = arguments?.getDouble(USER_LONGITUDE) ?: 0.0
        val hotspotLatitude = arguments?.getDouble(HOTSPOT_LATITUDE) ?: 0.0
        val hotspotLongitude = arguments?.getDouble(HOTSPOT_LONGITUDE) ?: 0.0

        val userLocation = LatLng(userLatitude, userLongitude)
        val hotspotLocation = LatLng(hotspotLatitude, hotspotLongitude)

        val distance = calculateDistance(userLocation, hotspotLocation)
        distanceTextView.text = "Distance: ${round(distance * 100.0) / 100.0} km"
        locationNameTextView.text = "Location Name: Some Location Name" // Set the actual location name

        return view
    }

    private fun calculateDistance(userLocation: LatLng, hotspotLocation: LatLng): Double {
        val earthRadius = 6371 // Radius of the Earth in kilometers
        val dLat = Math.toRadians(hotspotLocation.latitude - userLocation.latitude)
        val dLng = Math.toRadians(hotspotLocation.longitude - userLocation.longitude)
        val a = (
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(userLocation.latitude)) * Math.cos(Math.toRadians(hotspotLocation.latitude)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2)
                )
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c
    }
}
