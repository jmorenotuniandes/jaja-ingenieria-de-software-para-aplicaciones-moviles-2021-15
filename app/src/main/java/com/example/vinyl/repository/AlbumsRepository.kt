package com.example.vinyl.repository

import android.app.Application
import com.android.volley.VolleyError
import com.example.vinyl.model.dto.Album
import com.example.vinyl.model.network.NetworkServiceAdapter

class AlbumsRepository (val application: Application) {
    fun refreshData(callback: (List<Album>) -> Unit, onError: (VolleyError) -> Unit) {
        NetworkServiceAdapter.getInstance(application).getAlbums({
            callback(it)
        }, onError)
    }
}