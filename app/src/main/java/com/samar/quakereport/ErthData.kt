package com.samar.quakereport

import com.google.gson.annotations.SerializedName

 data class ErthData(
    @SerializedName("properties") var properties: Properties,
    @SerializedName("geometry") var geometry: Geometry,
    @SerializedName("id") var id: String
)