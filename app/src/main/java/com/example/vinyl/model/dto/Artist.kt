package com.example.vinyl.model.dto

data class Artist (
    val albumId: Int,
    val name:String,
    val cover:String,
    val releaseDate:String,
    val description:String,
    val genre:String,
    val recordLabel:String
)