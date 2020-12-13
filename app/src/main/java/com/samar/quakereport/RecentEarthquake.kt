package com.samar.quakereport

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import api.RecentApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "RecentEarthquake"
class RecentEarthquake {
    private val recentApi: RecentApi
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://earthquake.usgs.gov/fdsnws/event/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        recentApi = retrofit.create(RecentApi::class.java)
    }

    fun fetchPhotos(): LiveData<List<ErthData>> {
        val responseLiveData: MutableLiveData<List<ErthData>> = MutableLiveData()
        val recentRequest: Call<RecentResponse> = recentApi.fetchPhotos()
        recentRequest.enqueue(object : Callback<RecentResponse> {
            override fun onFailure(call: Call<RecentResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch earthquake", t)
            }
            override fun onResponse(
                call: Call<RecentResponse>,
                response: Response<RecentResponse>
            ) {
                Log.d(TAG, "Response received")
                val earthquakeResponse:RecentResponse ? = response.body()
              //  val recentResponse:RecentResponse? = earthquakeResponse?.recents
                var recentItems: List<ErthData> = earthquakeResponse?.recentItems
                    ?: mutableListOf()
                //recentItems = recentItems.filterNot { it.url.isBlank() }
                responseLiveData.value =recentItems
            }
        })
        return responseLiveData
    }
}
