package com.samar.quakereport

import com.google.gson.annotations.SerializedName

class RecentResponse {
    @SerializedName("features")
    lateinit var recentItems: List<ErthData>
}