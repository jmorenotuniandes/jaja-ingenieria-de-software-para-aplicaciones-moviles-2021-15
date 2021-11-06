package com.example.vinyl.model.dto

data class Artist (
    val id: Int,
    val name:String,
    val image:String,
    val description:String,
    val birthDate:String,
    val bgColor: String? = null
)