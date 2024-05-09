package com.example.prototypinglagila

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FirebaseUtils {
    fun convertTimestampToDateTime(timestamp: String?): String {

        // Check if the timestamp is null or empty
        if (timestamp.isNullOrEmpty()) {
            return "No Timestamp"
        }

        // Try to parse the timestamp to a Long
        val timestampLongSeconds = timestamp.toLongOrNull() ?: return "Invalid Timestamp"

        // Check if parsing was successful and the timestamp is valid

        // Convert seconds to milliseconds
        val timestampLongMilliseconds = timestampLongSeconds * 1000

        // Create a Date object from the timestamp
        val date = Date(timestampLongMilliseconds)

        // Create a SimpleDateFormat object to format the date
        val sdf = SimpleDateFormat("(HH:mm:ss)|dd-MM-yyyy", Locale.getDefault())

        // Format the date and return it as a string "yyyy-MM-dd | (HH:mm:ss)"
        return sdf.format(date)
    }

    fun getUserData(callback: (ArrayList<Sensor>) -> Unit){

        val email = "mardoslg03@gmail.com"
        val password = "westronger10"


        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    // Sign-in successful, retrieve the UID of the authenticated user
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    //catch error
                    Log.d(ContentValues.TAG, "login with user success")
                    uid?.let { userId ->
                        val dbref = FirebaseDatabase.getInstance().getReference("UsersData").child(userId)
                            .child("readings")
                        dbref.addValueEventListener(object : ValueEventListener {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val sensorList = arrayListOf<Sensor>()
                                if (snapshot.hasChildren()) {

                                    for (childSnapshot in snapshot.children) {
                                        val timestamp = childSnapshot.child("timestamp").getValue(String::class.java)
                                        val acidity = childSnapshot.child("acidity").getValue(String::class.java)
                                        val temperature = childSnapshot.child("temperature").getValue(String::class.java)
                                        val turbidity = childSnapshot.child("turbidity").getValue(String::class.java)
                                        val dateTimeString = convertTimestampToDateTime(timestamp)//convert time
                                        val sensor = Sensor(acidity, temperature, dateTimeString, turbidity)

                                        sensorList.add(sensor)
                                    }

//
                                    // Call holeIndicator with the retrieved sensor data
//                                        sensorArrayList.forEach { sensor ->
////                                            holeIndicator(sensor)
//                                        }
//                                        adapter.notifyDataSetChanged()

                                }
                                else {
                                    Log.w(ContentValues.TAG, "No data found in snapshot")
                                }
                                callback(sensorList)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                            }
                        })
                    } ?: run {
                        Log.e(ContentValues.TAG, "User ID is null")
                    }
                } else {
                    Log.w(ContentValues.TAG, "Login failed: ${task.exception}")
                    // Handle login failure
                }
            }
    }

}