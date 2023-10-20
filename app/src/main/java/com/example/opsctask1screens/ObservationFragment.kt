package com.example.opsctask1screens

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ObservationFragment: Fragment() {

    private lateinit var birdListEditText: EditText
    private lateinit var notesEditText: EditText
    private lateinit var storeObservationButton: Button
    private lateinit var notificationTextView: TextView

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_observation, container, false)

        // Initialize Firebase components
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("tblUsers")

        // Initialize UI elements
        birdListEditText = rootView.findViewById(R.id.birdListEditText)
        notesEditText = rootView.findViewById(R.id.notesEditText)
        storeObservationButton = rootView.findViewById(R.id.storeObservationButton)

        notificationTextView = rootView.findViewById(R.id.showNotification)
        // Load user settings (if needed)

        // Save button click listener
        storeObservationButton.setOnClickListener {
            storeObservationData()
        }

        return rootView
    }

    private fun storeObservationData() {
        val birdList = birdListEditText.text.toString()
        val notes = notesEditText.text.toString()

        // Create an observation object
        val observationData = BirdObservation(birdList, notes)

        // Reference to the Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("tblObservation")

        // Push (add) the observation data to the database
        reference.push().setValue(observationData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showNotification("Observation saved successfully")
                } else {
                    showNotification("Failed to save observation")
                }
            }
    }


    private fun showNotification(message: String) {
        notificationTextView.text = message
    }

}
