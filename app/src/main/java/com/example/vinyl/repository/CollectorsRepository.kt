package com.example.vinyl.repository

import android.app.Application
import com.android.volley.VolleyError
import com.example.vinyl.model.dto.Collector
import com.example.vinyls_jetpack_application.network.NetworkServiceAdapter

class CollectorRepository (val application: Application) {
    fun refreshData(callback: (List<Collector>) -> Unit, onError: (VolleyError) -> Unit) {
        NetworkServiceAdapter.getInstance(application).getCollectors({
            callback(it)
        }, onError)
    }
}