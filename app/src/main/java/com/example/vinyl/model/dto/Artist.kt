package com.example.vinyl.model.dto
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String,
    val bgColor: String? = null,
    val albums: List<Album>,
): Parcelable