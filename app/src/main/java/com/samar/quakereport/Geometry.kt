package com.samar.quakereport

import com.google.gson.annotations.SerializedName

 data class Geometry(
    @SerializedName("coordinates") var coordinates: List<Double>)
