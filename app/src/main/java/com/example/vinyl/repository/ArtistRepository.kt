package com.example.vinyl.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.vinyl.model.dto.Artist
import com.example.vinyl.model.network.CacheManager
import com.example.vinyl.model.network.NetworkServiceAdapter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONArray

class ArtistRepository (val application: Application) {
    val format = Json {  }

    suspend fun refreshData(): List<Artist>{
        var artists = getArtists()
        return if(artists.isNullOrEmpty()){
            val cm = application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if( cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI && cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE){
                emptyList()
            } else {
                artists = NetworkServiceAdapter.getInstance(application).getArtists()
                addArtists(artists)
                artists
            }
        } else artists
    }

    suspend fun getArtists(): List<Artist>{
        val prefs = CacheManager.getPrefs(application.baseContext, CacheManager.ALBUMS_SPREFS)
        if(prefs.contains("artists")){
            val storedVal = prefs.getString("artists", "")
            if(!storedVal.isNullOrBlank()){
                val resp = JSONArray(storedVal)
                Log.d("deserialize", resp.toString())
                return format.decodeFromString<List<Artist>>(storedVal)
            }
        }
        return listOf<Artist>()
    }

    suspend fun addArtists(artists: List<Artist>){
        val prefs = CacheManager.getPrefs(application.baseContext, CacheManager.ALBUMS_SPREFS)
        if(!prefs.contains("artists")){
            val store = format.encodeToString(artists)
            with(prefs.edit(),{
                putString("artists", store)
                apply()
            })
        }
    }
}