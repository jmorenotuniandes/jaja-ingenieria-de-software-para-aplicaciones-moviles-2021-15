package com.example.vinyl.model.network

import android.content.Context
import android.content.SharedPreferences
import com.example.vinyl.model.dto.Album
import com.example.vinyl.model.dto.Artist
import com.example.vinyl.model.dto.Collector

class CacheManager(context: Context) {
    companion object{
        var instance: CacheManager? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: CacheManager(context).also {
                    instance = it
                }
            }
        const val APP_SPREFS = "com.example.vinyl.app"
        const val ALBUMS_SPREFS = "com.example.vinyl.albums"
        fun getPrefs(context: Context, name:String): SharedPreferences {
            return context.getSharedPreferences(name,
                Context.MODE_PRIVATE
            )
        }
    }
    private var collectors: HashMap<String, List<Collector>> = hashMapOf<String, List<Collector>>()
    private var albums: HashMap<String, List<Album>> = hashMapOf<String, List<Album>>()
    private var artists: HashMap<String, List<Artist>> = hashMapOf<String, List<Artist>>()

    fun addCollectors( collectorsToAdd: List<Collector>){
        if (collectors.containsKey("collectors")){
            collectors["collectors"] = collectorsToAdd
        }
    }
    fun getCollectors() : List<Collector>{
        return if (collectors.containsKey("collectors")) collectors["collectors"]!! else listOf<Collector>()
    }

    fun addAlbums(albumsToAdd :List<Album>){
        if (albums.containsKey("albums")){
            albums["albums"] = albumsToAdd
        }
    }

    fun getAlbums() : List<Album> {
        return if (albums.containsKey("albums")) albums["albums"]!! else listOf<Album>()
    }


    fun addArtists(artistsToAdd :List<Artist>){
        if (artists.containsKey("artists")){
            artists["artists"] = artistsToAdd
        }
    }

    fun getArtists() : List<Artist> {
        return if (artists.containsKey("artists")) artists["artists"]!! else listOf<Artist>()
    }


}