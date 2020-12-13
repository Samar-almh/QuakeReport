package com.samar.quakereport

import com.google.gson.annotations.SerializedName


data class Properties(
    @SerializedName("mag") var mag: Double,
    @SerializedName("place") var place:String,
    @SerializedName("time") var time: Long,
    @SerializedName("title") var title: String
    )


