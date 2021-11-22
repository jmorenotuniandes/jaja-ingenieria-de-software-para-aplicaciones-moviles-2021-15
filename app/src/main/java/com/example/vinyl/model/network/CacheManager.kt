package com.example.vinyl.model.network

import android.content.Context
import android.content.SharedPreferences
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

    fun addCollectors( collectorsToAdd: List<Collector>){
        if (collectors.containsKey("collectors")){
            collectors["collectors"] = collectorsToAdd
        }
    }
    fun getCollectors() : List<Collector>{
        return if (collectors.containsKey("collectors")) collectors["collectors"]!! else listOf<Collector>()
    }
}