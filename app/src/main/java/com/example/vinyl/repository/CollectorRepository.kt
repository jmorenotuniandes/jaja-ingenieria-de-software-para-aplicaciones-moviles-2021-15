package com.example.vinyl.repository

import android.app.Application
import com.example.vinyl.model.dto.Album
import com.example.vinyl.model.dto.Collector
import com.example.vinyl.model.network.NetworkServiceAdapter

class CollectorRepository (val application: Application) {
    suspend fun refreshData(): List<Collector> {
        return NetworkServiceAdapter.getInstance(application).getCollectors()
    }
    suspend fun refreshAlbumsDetailsData(albumId: Int): Album {
        return NetworkServiceAdapter.getInstance(application).getAlbum(albumId)
    }

}