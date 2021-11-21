package com.example.vinyl.repository

import android.app.Application
import com.example.vinyl.model.dto.Artist
import com.example.vinyl.model.network.NetworkServiceAdapter

class ArtistRepository (val application: Application) {
    suspend fun refreshData(): List<Artist> {
        return NetworkServiceAdapter.getInstance(application).getArtists()
    }
}