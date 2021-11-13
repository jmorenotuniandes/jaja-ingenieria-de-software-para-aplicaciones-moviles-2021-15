package com.example.vinyl.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Collector (
    val collectorId: Int,
    val name:String,
    val telephone:String,
    val email:String,

    val bgColor: String? = null
) : Parcelable