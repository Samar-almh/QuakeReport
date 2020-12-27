package com.samar.quakereport


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.math.RoundingMode
import java.util.*

private const val TAG = "EarthquakesFragment"

class RecentEarthquakeFragment : Fragment() {

    private lateinit var quakeReportViewModel:QuakeReportViewModel
    private lateinit var recentRecyclerView: RecyclerView


    companion object {
        fun newInstance() = RecentEarthquakeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        quakeReportViewModel =
            ViewModelProviders.of(this).get(QuakeReportViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recent_earthquake, container, false)
        recentRecyclerView = view.findViewById(R.id.recent_recycler_view);
        recentRecyclerView.layoutManager = LinearLayoutManager(context)

        return view;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quakeReportViewModel.quakereportLiveData.observe(
            viewLifecycleOwner,
            Observer { recentItems ->
                recentRecyclerView.adapter = EarthAdapter(recentItems)
            })
    }

    private class EarthHolder(itemTextView: View) : RecyclerView.ViewHolder(itemTextView),
        View.OnClickListener {
        val placeTextView = itemTextView.findViewById(R.id.place) as TextView
        val titleTextView = itemTextView.findViewById(R.id.title) as TextView
        val timeTextView = itemTextView.findViewById(R.id.time) as TextView
        val dateTextView = itemTextView.findViewById(R.id.date) as TextView
        val magButton = itemTextView.findViewById(R.id.mag) as Button
        private var longtude: Double = 0.0
        private var latitude: Double = 0.0


        init {
            itemView.setOnClickListener(this)
        }


        fun bind(erthData: ErthData) {
            placeTextView.setText(erthData.properties.place)
            titleTextView.setText(erthData.properties.title)
            date(erthData.properties.time)
            time(erthData.properties.time)
            setCoordinates(erthData.geometry.coordinates)

            mag(erthData.properties.mag)
            if (erthData.properties.title.contains(" of ".toRegex())) {
                placeTextView.text = erthData.properties.title.split("of")[0] + "of"
                titleTextView.text = erthData.properties.title.split("of")[1]
            } else {
                titleTextView.text = erthData.properties.title
                placeTextView.text = erthData.properties.place
            }

        }

        fun date(date: Long) {
            val cal = Calendar.getInstance()
            cal.time = Date(date)
            val date = "${cal.get(Calendar.YEAR)}-" + "${cal.get(Calendar.MONTH)}-" + "${cal.get(Calendar.DAY_OF_MONTH)}"
            dateTextView.text = date
        }

        fun time(time:Long){
            val cal = Calendar.getInstance()
            cal.time = Date(time)
            val time = "${cal.get(Calendar.HOUR_OF_DAY)}:" + "${cal.get(Calendar.MINUTE)}"
            timeTextView.text = time
        }

        fun mag(mag: Double) {
            magButton.apply {
                text = mag.toBigDecimal().setScale(1, RoundingMode.CEILING).toString()
                when {
                    mag < 3.9 -> setBackgroundResource(R.drawable.green_shape)
                    mag in 4.0.. 4.9 -> setBackgroundResource(R.drawable.yallow_shape)
                    mag in 5.0.. 5.9 -> setBackgroundResource(R.drawable.orange_shape)
                    mag in 6.0..10.0 -> setBackgroundResource(R.drawable.red_shape)
                }
            }
        }


        fun setCoordinates(coordinates: List<Double>) {
            longtude = coordinates[0]
            latitude = coordinates[1]
        }
        override fun onClick(v: View?) {
            val uri = Uri.parse("geo:$latitude,$longtude")
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = uri
            }
            ContextCompat.startActivity(itemView.context, intent, null)
        }
    }

    private class EarthAdapter(private val recentItems: List<ErthData>) :
        RecyclerView.Adapter<EarthHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): EarthHolder {

            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.erthquick_det, parent, false)
            return EarthHolder(view)
        }

        override fun getItemCount(): Int = recentItems.size


        override fun onBindViewHolder(holder: EarthHolder, position: Int) {
            val recentItem = recentItems[position]
            holder.bind(recentItem)
        }
    }
}