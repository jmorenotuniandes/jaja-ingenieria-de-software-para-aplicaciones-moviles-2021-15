package com.example.vinyl.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album (
    val albumId: Int,
    val name:String,
    val cover:String? = null,
    val releaseDate:String? = null,
    val description:String? = null,
    val genre:String? = null,
    val recordLabel:String? = null,
    val bgColor: String? = null
): Parcelable
