package com.example.vinyl.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Song (
    val name:String,
    val duration:String
): Parcelable