package com.example.vinyl.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Collector (
    val collectorId: Int,
    val name:String,
    val telephone:String,
    val email:String,
    val bgColor: String? = null,
    var collectorAlbums: List<Album>,
) : Parcelable