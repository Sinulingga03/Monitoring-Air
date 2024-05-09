package com.example.prototypinglagila

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.ListFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.prototypinglagila.databinding.ActivitySensorlistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.List


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySensorlistBinding

    private val homeFragment = Home()
    private val listFragment = List()
    private val chartFragment = Chart()
    private val authorFragment = Author()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySensorlistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(homeFragment)


        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Home -> replaceFragment(homeFragment)
                R.id.Datalogging -> replaceFragment(listFragment)
                R.id.Chart -> replaceFragment(chartFragment)
                R.id.Author -> replaceFragment(authorFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_fragment, fragment)

//        if (fragment !is Home) {
//            fragmentTransaction.addToBackStack(null)
//        }

        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commit()
//        binding.bottomNavigationView.menu.findItem(fragment.menuItemId)?.isChecked = true

    }
}










