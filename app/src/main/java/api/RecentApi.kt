package api

import com.samar.quakereport.EarthquakeResponse
import com.samar.quakereport.RecentResponse
import retrofit2.Call
import retrofit2.http.GET

interface RecentApi {
    @GET(
        "query?format=geojson&limit=10"
    )
    fun fetchPhotos(): Call<RecentResponse>
}
