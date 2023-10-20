package com.example.opsctask1screens

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class HotspotsAdapter(
    private val context: Context,
    private val hotspots: List<ObservationModel>,
    private val userLatitude: Double,
    private val userLongitude: Double
) : RecyclerView.Adapter<HotspotsAdapter.HotspotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotspotViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_hotspot, parent, false)
        return HotspotViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hotspots.size
    }

    override fun onBindViewHolder(holder: HotspotViewHolder, position: Int) {
        val hotspot = hotspots[position]
        holder.hotspotNameTextView.text = hotspot.locName
        val distance = calculateDistance(userLatitude, userLongitude, hotspot.latitude, hotspot.longitude)
        holder.hotspotDistanceTextView.text = "${String.format("%.2f", distance)} km"

        // Set a click listener for the "View Distance" button
        holder.viewDistanceButton.setOnClickListener {
            val hotspot = hotspots[position]
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.map, DistanceFragment.newInstance(userLatitude, userLongitude, hotspot.latitude, hotspot.longitude))
                .addToBackStack(null)
                .commit()
        }


    }

    private fun calculateDistance(
        userLatitude: Double,
        userLongitude: Double,
        hotspotLatitude: Double,
        hotspotLongitude: Double
    ): Double {
        // Implement your distance calculation logic here using coordinates
        val userLocation = Location("UserLocation")
        userLocation.latitude = userLatitude
        userLocation.longitude = userLongitude

        val hotspotLocation = Location("HotspotLocation")
        hotspotLocation.latitude = hotspotLatitude
        hotspotLocation.longitude = hotspotLongitude

        return userLocation.distanceTo(hotspotLocation) / 1000.0 // Convert meters to kilometers
    }

    class HotspotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hotspotNameTextView: TextView = view.findViewById(R.id.hotspotNameTextView)
        val hotspotDistanceTextView: TextView = view.findViewById(R.id.hotspotDistanceTextView)
        val viewDistanceButton: Button = view.findViewById(R.id.viewDistanceButton)
    }
}
