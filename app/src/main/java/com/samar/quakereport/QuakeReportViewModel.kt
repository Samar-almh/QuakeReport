package com.samar.quakereport

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class QuakeReportViewModel: ViewModel() {

    val quakereportLiveData: LiveData<List<ErthData>>
    init {
        quakereportLiveData = RecentEarthquake().fetchPhotos()
    }
}