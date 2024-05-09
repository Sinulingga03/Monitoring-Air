package com.example.prototypinglagila

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class MyAdapter(private val sensorList : ArrayList<Sensor>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{

        val view = LayoutInflater.from(parent.context).inflate(R.layout.sensor_value, parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sensorList.size    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {  holder.itemView.apply {

        val tvacidity : TextView = findViewById(R.id.tvAcidity)
        val tvtemperature : TextView = findViewById(R.id.tvTemprature)
        val tvtimestamp : TextView = findViewById(R.id.tvTimestamp)
        val tvturbidity : TextView = findViewById(R.id.tvTurbidity)

        tvacidity.text = sensorList[position].acidity ?: "Null"
        tvtemperature.text = sensorList[position].temperature ?: "Null"
        tvtimestamp.text = sensorList[position].timestamp   ?: "Null"
        tvturbidity.text = sensorList[position].turbidity ?: "Null"

    }


        //val currentitem = sensorList[position]



//        holder.acidity.text = currentitem.acidity ?: "Err"
//        holder.temperature.text = currentitem.temperature ?:"Err"
//        holder.time.text = currentitem.timestamp ?: "Err"
//        holder.turbidity.text = currentitem.turbidity ?: "Err"
    }



//    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
//
//        val acidity : TextView = itemView.findViewById(R.id.tvAcidity)
//        val temperature : TextView = itemView.findViewById(R.id.tvTemprature)
//        val time : TextView = itemView.findViewById(R.id.tvTimestamp)
//        val turbidity : TextView = itemView.findViewById(R.id.tvTurbidity)
//
//    }

}