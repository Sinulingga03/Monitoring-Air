package com.example.prototypinglagila

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class List : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private var sensorList = ArrayList<Sensor>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fetch data when the fragment is created
        fetchData()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_list, container, false)
        recyclerView = view.findViewById(R.id.sensorlist)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyAdapter(sensorList)
        recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // No need to set up RecyclerView here, it's already done in onCreateView
    }

    private fun fetchData() {
        FirebaseUtils.getUserData { newSensorList ->

            // Update sensorList with new data
            sensorList.clear()
            sensorList.addAll(newSensorList)
            // Notify the adapter that the data set has changed
            adapter.notifyDataSetChanged()
        }
    }

    companion object {

        //@JvmStatic
        //fun newInstance() = ListFragment()
    }
}






