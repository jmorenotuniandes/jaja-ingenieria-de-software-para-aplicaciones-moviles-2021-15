package com.example.vinyl.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song (
    val name:String,
    val duration:String
): Parcelable