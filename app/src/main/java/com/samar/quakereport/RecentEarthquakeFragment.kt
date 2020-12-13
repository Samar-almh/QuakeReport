package com.samar.quakereport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.math.RoundingMode
import java.util.*

private const val TAG = "RecentEarthquake"
private lateinit var quakeReportViewModel:QuakeReportViewModel
class RecentEarthquakeFragment : Fragment() {

    private lateinit var recentRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        quakeReportViewModel = ViewModelProviders.of(this).get(QuakeReportViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_recent_earthquake, container, false)
        recentRecyclerView = view.findViewById(R.id.recent_recycler_view)
        recentRecyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quakeReportViewModel.quakereportLiveData.observe(
            viewLifecycleOwner,
            Observer { recentItems ->
                recentRecyclerView.adapter = EarthAdapter(recentItems)
            })
    }

    private class EarthHolder(itemTextView: View)
        : RecyclerView.ViewHolder(itemTextView) {
        val placeTextView=itemTextView.findViewById(R.id.place ) as TextView
       val titleTextView = itemTextView.findViewById(R.id.title) as TextView
       val timeTextView = itemTextView.findViewById(R.id.time) as TextView
       //val magTextView = itemTextView.findViewById(R.id.mag) as TextView
       val magButton = itemTextView.findViewById(R.id.mag) as Button



       // val bindTitle: (CharSequence) -> Unit = itemTextView::setText

        fun bind(erthData: ErthData){
            placeTextView.setText(erthData.properties.place)
            titleTextView.setText(erthData.properties.title)
            timeTextView.text = convertToTime(erthData.properties.time)
           // magTextView.setText(erthData.properties.mag)
            MagButton(erthData.properties.mag)
            if (erthData.properties.title.contains(" of ".toRegex())) {
                placeTextView.text = erthData.properties.title.split("of")[0] + "of"
                titleTextView.text = erthData.properties.title.split("of")[1]
            } else {
                titleTextView.text = erthData.properties.title
                placeTextView.text = erthData.properties.place
            }
        }

        fun convertToTime(dateTime: Long): String {
            val calendar = Calendar.getInstance()
            calendar.time = Date(dateTime)
            val earthTime: String = "${calendar.get(Calendar.HOUR_OF_DAY)}:" + "${calendar.get(Calendar.MINUTE)}"
            return earthTime
        }

        fun MagButton(mag: Double) {
            magButton.apply {
                text = mag.toBigDecimal().setScale(1, RoundingMode.CEILING).toString()
                when {
                    mag < 4.0 -> setBackgroundResource(R.drawable.mag_buton_low_level_shape)
                    mag < 5.0 -> setBackgroundResource(R.drawable.mag_buton_premed_level_shape)
                    mag <= 6.0 -> setBackgroundResource(R.drawable.mag_buton_medium_level_shape)
                    mag in 6.0..10.0 -> setBackgroundResource(R.drawable.mag_buton_high_level_shape)
                }
            }
        }

    }
    private class EarthAdapter(private val recentItems: List<ErthData>)
        : RecyclerView.Adapter<EarthHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): EarthHolder {
         val view = LayoutInflater.from(parent.context).inflate(R.layout.erthquick_det,parent,false)
            return EarthHolder(view)
        }
        override fun getItemCount(): Int = recentItems.size
        override fun onBindViewHolder(holder: EarthHolder, position: Int) {
            val recentItem = recentItems[position]
          //  holder.bind(recentItem.properties.place)
            holder.bind(recentItem)
        }
    }

    companion object {
        fun newInstance() = RecentEarthquakeFragment()
    }
}