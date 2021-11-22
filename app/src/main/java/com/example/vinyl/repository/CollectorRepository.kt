package com.example.vinyl.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.vinyl.model.dto.Album
import com.example.vinyl.model.dto.Collector
import com.example.vinyl.model.dto.Comment
import com.example.vinyl.model.network.NetworkServiceAdapter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.example.vinyl.model.network.CacheManager
import org.json.JSONArray

class CollectorRepository (val application: Application) {
    val format = Json {  }

    suspend fun refreshData(): List<Collector>{
        var collector = getCollectors()
        return if(collector.isNullOrEmpty()){
            val cm = application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if( cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI && cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE){
                emptyList()
            } else {
                collector = NetworkServiceAdapter.getInstance(application).getCollectors()
                addCollectors(collector)
                collector
            }
        } else collector
    }

    suspend fun getCollectors(): List<Collector>{
        val prefs = CacheManager.getPrefs(application.baseContext, CacheManager.ALBUMS_SPREFS)
        if(prefs.contains("collectors")){
            val storedVal = prefs.getString("collectors", "")
            if(!storedVal.isNullOrBlank()){
                val resp = JSONArray(storedVal)
                Log.d("deserialize", resp.toString())
                return format.decodeFromString<List<Collector>>(storedVal)
            }
        }
        return listOf<Collector>()
    }

    suspend fun addCollectors( collectors: List<Collector>){
        val prefs = CacheManager.getPrefs(application.baseContext, CacheManager.ALBUMS_SPREFS)
        if(!prefs.contains("collectors")){
            val store = format.encodeToString(collectors)
            with(prefs.edit(),{
                putString("collectors", store)
                apply()
            })
        }
    }


    suspend fun refreshAlbumsDetailsData(albumId: Int): Album {
        return NetworkServiceAdapter.getInstance(application).getAlbum(albumId)
    }



}