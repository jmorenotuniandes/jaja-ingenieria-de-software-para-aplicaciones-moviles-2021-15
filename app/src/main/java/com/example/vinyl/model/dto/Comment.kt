package com.example.vinyl.model.dto

data class Comment (
    val description:String,
    val rating:Int,
    val collectorId:Int,
    val albumId:Int
)