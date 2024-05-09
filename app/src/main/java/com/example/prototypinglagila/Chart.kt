package com.example.prototypinglagila

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.yabu.livechart.model.DataPoint


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Chart.newInstance] factory method to
 * create an instance of this fragment.
 */
class Chart : Fragment() {

    private lateinit var lineChart1 : LineChart
    private lateinit var lineChart2 : LineChart
    private lateinit var lineChart3 : LineChart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_chart, container, false)


        FirebaseUtils.getUserData { sensorList ->
            // Convert the sensorList into a list of DataPoint objects
            val dataPoints = mutableListOf<DataPoint>()
            val dataPointsTemp = mutableListOf<DataPoint>()
            val dataPointsTurb = mutableListOf<DataPoint>()

            sensorList.takeLast(10).forEachIndexed { index, sensor ->
                val value = sensor.acidity?.toFloatOrNull() ?: 0f
                dataPoints.add(DataPoint(index.toFloat(), value))
                val value1 = sensor.temperature?.toFloatOrNull() ?: 0f
                dataPointsTemp.add(DataPoint(index.toFloat(),value1))
                val value2 = sensor.turbidity?.toFloatOrNull() ?: 0f
                dataPointsTurb.add(DataPoint(index.toFloat(),value2))
            }

            //grafik 1
            lineChart1 = view.findViewById(R.id.lineChart)

            val entries = dataPoints.map { Entry(it.x.toFloat(), it.y) }
            val dataSet = LineDataSet(entries,"Nilai PH Air")
            val lineData = LineData(dataSet)
            dataSet.notifyDataSetChanged()
            dataSet.color = Color.BLUE
            dataSet.lineWidth = 3f
            lineChart1.data = lineData
            lineChart1.description.text =""//kosongan aja
            lineChart1.invalidate()


            //grafik 2
            lineChart2 = view.findViewById(R.id.lineChart2)

                val entriesTemp = dataPointsTemp.map{Entry(it.x.toFloat(),it.y)}
                val dataSetTemp =  LineDataSet(entriesTemp,"Nilai Suhu Air")
                val lineDataTemp = LineData(dataSetTemp)
                dataSetTemp.notifyDataSetChanged()
                dataSetTemp.color = Color.GREEN
                dataSetTemp.lineWidth = 3f
                lineChart2.data = lineDataTemp
                lineChart2.description.text = "" //kosongan aja
                lineChart2.invalidate() //muat ulang grafik

            //grafik3
            lineChart3 = view.findViewById(R.id.lineChart3)
                val entriesTur = dataPointsTurb.map { Entry(it.x.toFloat(),it.y) }
                val datasetTur = LineDataSet(entriesTur,"Nilai Keruh Air")
                val lineDataTur = LineData(datasetTur)
                datasetTur.lineWidth = 3f
                datasetTur.color = Color.CYAN
                lineChart3.data = lineDataTur
                lineChart3.description.text = ""//kosongan aja
                lineChart3.invalidate()



       }


        return view
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Chart().apply {
                arguments = Bundle().apply {

                }
            }
    }
}