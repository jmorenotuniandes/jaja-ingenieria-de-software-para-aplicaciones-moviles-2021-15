package com.example.vinyl.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.vinyl.model.dto.Album
import com.example.vinyl.model.dto.Artist
import com.example.vinyl.model.dto.Song
import com.example.vinyl.model.network.CacheManager
import com.example.vinyl.model.network.NetworkServiceAdapter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.lang.Exception

class AlbumsRepository (val application: Application) {
/*
    suspend fun refreshData(): List<Album> {
        return NetworkServiceAdapter.getInstance(application).getAlbums()
    }
*/

    val format = Json {  }

    suspend fun getAlbum(album: Album): Album? {
        return try {
            NetworkServiceAdapter.getInstance(application).getAlbum(album.albumId)
        } catch (e:Exception) {
            null
        }
    }

    suspend fun refreshData(forced:Boolean = false): List<Album>{
        if (forced) {
            var albums = NetworkServiceAdapter.getInstance(application).getAlbums()
            addAlbums(albums)
            return albums
        }

        var albums = getAlbums()
        return if(albums.isNullOrEmpty()){
            val cm = application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if( cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI && cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE){
                emptyList()
            } else {
                albums = NetworkServiceAdapter.getInstance(application).getAlbums()
                addAlbums(albums)
                albums
            }
        } else albums
    }

    suspend fun getAlbums(): List<Album>{
        val prefs = CacheManager.getPrefs(application.baseContext, CacheManager.ALBUMS_SPREFS)
        if(prefs.contains("albums")){
            val storedVal = prefs.getString("albums", "")
            if(!storedVal.isNullOrBlank()){
                val resp = JSONArray(storedVal)
                Log.d("deserialize", resp.toString())
                return format.decodeFromString<List<Album>>(storedVal)
            }
        }
        return listOf<Album>()
    }

    suspend fun addAlbums(albums: List<Album>){
        val prefs = CacheManager.getPrefs(application.baseContext, CacheManager.ALBUMS_SPREFS)
        if(!prefs.contains("albums")){
            val store = format.encodeToString(albums)
            with(prefs.edit(),{
                putString("albums", store)
                apply()
            })
        }
    }

    suspend fun addSongToAlbum(song: Song, album:Album) {
        NetworkServiceAdapter.getInstance(application).addSongToAlbum(song, album)
    }

    suspend fun addAlbum(album:Album) {
        NetworkServiceAdapter.getInstance(application).addAlbum(album)
    }
}