package com.example.vinyl.repository

import android.app.Application
import com.android.volley.VolleyError
import com.example.vinyl.model.dto.Artist
import com.example.vinyl.model.network.NetworkServiceAdapter

class ArtistRepository (val application: Application) {
    fun refreshData(callback: (List<Artist>) -> Unit, onError: (VolleyError) -> Unit) {
        NetworkServiceAdapter.getInstance(application).getArtists({
            callback(it)
        }, onError)
    }
}