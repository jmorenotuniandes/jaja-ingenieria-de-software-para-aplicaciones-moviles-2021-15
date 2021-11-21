package com.example.vinyl.repository

import android.app.Application
import com.android.volley.VolleyError
import com.example.vinyl.model.dto.Album
import com.example.vinyl.model.network.NetworkServiceAdapter

class AlbumsRepository (val application: Application) {
    suspend fun refreshData(): List<Album> {
        return NetworkServiceAdapter.getInstance(application).getAlbums()
    }
}