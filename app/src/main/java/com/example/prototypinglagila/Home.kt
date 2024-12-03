package com.example.prototypinglagila

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.example.prototypinglagila.FirebaseUtils as FirebaseUtils

class Home : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //call FirebaseUtils hereeeeee
        FirebaseUtils.getUserData { sensorList ->
            if (sensorList.isNotEmpty()) {
                // If there is at least one sensor data, call holeIndicator for each sensorrrrrr
                sensorList.forEach { sensor ->
                    holeIndicator(sensor)
                    textIndicator(sensor)
                }
            }
        }
    }

    //handle text inside CircularProgressBar!

    private fun textIndicator(sensor: Sensor){
        val tvAcid : TextView? = view?.findViewById(R.id.tvIndicatorAcid)
        tvAcid?.apply {
            val acidity = sensor.acidity
            acidity?.let {
                text = it
            }
        }

        val tvTemp : TextView? = view?.findViewById(R.id.tvIndicatoTemp)
        tvTemp?.apply {
            val temperature = sensor.temperature
            temperature?.let {
                text = it

            }
        }
        val tvTurb : TextView? = view?.findViewById(R.id.tvIndicatoTurb)
        tvTurb?.apply {
          val turbidity = sensor.turbidity
          turbidity?.let {
              text = it
          }
        }
        val tvTime : TextView? = view?.findViewById(R.id.tvTimeNow)
        tvTime?.apply{
            val timestamp = sensor.timestamp
            timestamp?.let {
                text = it
            }
        }

        val parameter : TextView? = view?.findViewById(R.id.parameter1)
        parameter?.apply {
            val acidity = sensor.acidity
            val acidityFloat = acidity?.toFloatOrNull() ?: 0f
            acidityFloat.let {

                val progressValue = if (it >= 0.1) it else 0f

                text = when (progressValue.toDouble()){
                    in 0.0..6.49 -> {
                        setTextColor(Color.RED)
                        "Asam"
                    }

                    in 6.50..8.50 -> {
                        setTextColor(Color.BLUE)
                        "Netral"
                    }

                    in 8.51..14.00 -> {
                        setTextColor(Color.MAGENTA)
                        "basa"
                    }

                    else -> "N/A"
                }
            }
        }
        val parameter2 : TextView? = view?.findViewById(R.id.parameter2)
        parameter2?.apply{
            val temperature = sensor.temperature
            temperature?.let {
                text = when (it.toDouble()){
                    in 0.0..19.99 -> {
                        setTextColor(Color.BLUE)
                        "Suhu Rendah"
                    }
                    in 20.00..30.00 -> {
                        setTextColor(Color.GREEN)
                        "Suhu Optimal"
                    }

                    in 30.01..100.00 -> {
                        setTextColor(Color.RED)
                        "Suhu Tinggi!"
                    }
                    else ->"N/A"
                }
            }
        }
        val parameter3 : TextView? = view?.findViewById(R.id.parameter3)
        parameter3?.apply{
            val turbidity = sensor.turbidity
            turbidity?.let {
                text = when (it.toDouble()){
                    in 00.00 .. 05.00 ->{
                        setTextColor(Color.BLUE)
                        "Rendah"
                    }
                    in 05.01 .. 25.99 ->{
                        setTextColor(Color.YELLOW)
                        "Sedang"
                    }
                    in 26.00 .. 100.00 ->{
                        setTextColor(Color.RED)
                        "Tinggi"
                    }
                    else ->"N/A"
                }
            }
        }
    }
    //handle frontend HoleIndicator and get data into sensor
    @SuppressLint("SuspiciousIndentation")
    private fun holeIndicator(sensor: Sensor) {

        val indicatorProgress = view?.findViewById<CircularProgressBar>(R.id.indicatorProgress)
        indicatorProgress?.apply{

            progressMax = 14.00f //max ph
            progressBarWidth = 10f
            backgroundProgressBarWidth = 15f
            backgroundProgressBarColor = Color.LTGRAY
            roundBorder = true
            startAngle = 180f

            val acidity = sensor.acidity
            acidity?.let {
                val acidityFloat = acidity.toFloatOrNull() ?: 0f
                acidityFloat.let {
                    val progressValue = if (it >= 0) it else 0f
                    indicatorProgress.setProgressWithAnimation(progressValue,1000)
                    when (progressValue.toDouble()){
                        in 0.00 ..6.49 ->{
                            progressBarColor = Color.RED
                        }
                        in 6.50 .. 8.59 ->{
                            progressBarColor = Color.BLUE
                        }
                        in 8.60 ..14.00->{
                            progressBarColor = Color.MAGENTA
                        }
                    }
                }
            }
        }

        val indicatorProgress2 = view?.findViewById<CircularProgressBar>(R.id.indicatorProgress2)
        indicatorProgress2?.apply{

            progressMax = 50.00f //max temp C
            progressBarWidth = 10f
            backgroundProgressBarWidth = 15f
            backgroundProgressBarColor = Color.LTGRAY
            roundBorder = true
            startAngle = 180f
            val temperature = sensor.temperature
            temperature?.let{
                val temperatureFloat = temperature.toFloatOrNull()
                temperatureFloat?.let{
                    indicatorProgress2.setProgressWithAnimation(it,1000)


                    when(it.toDouble()){

                        in 00.00..19.99 -> {
                           progressBarColor = Color.BLUE
                        }
                        in 20.00..29.99 -> {
                            progressBarColor= Color.GREEN
                        }
                        in 30.00..50.00 -> {
                            progressBarColor = Color.RED
                        }

                    }

                }
            }
        }

        val indicatorProgress3 = view?.findViewById<CircularProgressBar>(R.id.indicatorProgress3)
        indicatorProgress3?.apply{

            progressMax = 100.00f //max tds
            progressBarWidth = 10f
            backgroundProgressBarWidth = 15f
            backgroundProgressBarColor = Color.LTGRAY
            roundBorder = true
            startAngle = 180f
            val turbidity = sensor.turbidity
            turbidity?.let {
                val turbidityFloat = turbidity.toFloatOrNull()
                turbidityFloat?.let {
                    indicatorProgress3.setProgressWithAnimation(it,1000)

                    when (it.toDouble()){

                        in 00.00 .. 5.00 ->{
                            progressBarColor = Color.BLUE
                        }
                        in 05.01 .. 25.99 ->{
                            progressBarColor = Color.YELLOW
                        }
                        in 26.00 .. 100.00 ->{
                            progressBarColor = Color.RED
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

       return inflater.inflate(R.layout.fragment_home, container, false)


    }



}