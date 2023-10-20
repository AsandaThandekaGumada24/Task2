package com.example.opsctask1screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ObservationAdapter(private val dataset: List<ObservationModel>) : RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder>() {

    class ObservationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val speciesCodeTextView: TextView = view.findViewById(R.id.speciesCodeTextView)
        val comNameTextView: TextView = view.findViewById(R.id.comNameTextView)
        val image: ImageView = view.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_observation, parent, false)
        return ObservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObservationViewHolder, position: Int) {
        val observation = dataset[position]
        holder.speciesCodeTextView.text = observation.comName
        holder.comNameTextView.text = observation.sciName
    }

    override fun getItemCount() = dataset.size
}
