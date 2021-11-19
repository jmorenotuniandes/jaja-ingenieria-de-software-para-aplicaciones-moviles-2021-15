package com.example.vinyls_jetpack_application.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vinyl.model.dto.Album
import com.example.vinyl.model.dto.Artist
import com.example.vinyl.model.dto.Collector
import com.example.vinyl.model.dto.Comment
import org.json.JSONArray
import org.json.JSONObject

class NetworkServiceAdapter constructor(context: Context) {
    companion object {
        const val BASE_URL = "https://back-vinyls-populated.herokuapp.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        // applicationContext keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    fun getAlbums(onComplete: (resp: List<Album>) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(
            getRequest("albums",
                Response.Listener<String> { response ->
                    val resp = JSONArray(response)
                    val list = mutableListOf<Album>()
                    for (i in 0 until resp.length()) {
                        val item = resp.getJSONObject(i)
                        list.add(i, Album(
                            albumId = item.getInt("id"),
                            name = item.getString("name"),
                            bgColor = getBgColor(i)
                        ))
                    }
                    onComplete(list)
                },
                Response.ErrorListener {
                    onError(it)
                })
        )
    }

    fun getCollectors(
        onComplete: (resp: List<Collector>) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        requestQueue.add(
            getRequest("collectors",
                Response.Listener<String> { response ->
                    Log.d("tagb", response)
                    val resp = JSONArray(response)
                    val list = mutableListOf<Collector>()
                    for (i in 0 until resp.length()) {
                        val item = resp.getJSONObject(i)
                        list.add(
                            i,
                            Collector(
                                collectorId = item.getInt("id"),
                                name = item.getString("name"),
                                telephone = item.getString("telephone"),
                                email = item.getString("email"),
                                bgColor = getBgColor(i),
                            )
                        )
                    }
                    onComplete(list)
                },
                Response.ErrorListener {
                    onError(it)
                })
        )
    }

    fun getArtists(onComplete: (resp: List<Artist>) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(
            getRequest("musicians",
                Response.Listener<String> { response ->
                    val resp = JSONArray(response)
                    val artists = mutableListOf<Artist>()
                    for (i in 0 until resp.length()) {
                        val artistObject = resp.getJSONObject(i)
                        val albumsArray = artistObject.getJSONArray("albums")
                        val albums = mutableListOf<Album>()

                        for( i in 0 until albumsArray.length()){
                            val albumObject = albumsArray.getJSONObject(i)
                            val album = Album(
                                albumId = albumObject.getInt("id"),
                                name = albumObject.getString("name"),
                                releaseDate= albumObject.getString("releaseDate"),
                                bgColor = "#FFFFFF"
                            )
                            albums.add(i,album)
                        }

                        artists.add(i, Artist(
                            id = artistObject.getInt("id"),
                            name = artistObject.getString("name"),
                            image = artistObject.getString("image"),
                            description = artistObject.getString("description"),
                            birthDate = artistObject.getString("birthDate"),
                            bgColor = getBgColor(i),
                            albums = albums
                        ))
                    }
                    onComplete(artists)
                },
                Response.ErrorListener {
                    onError(it)
                })
        )
    }

    fun getComments(
        albumId: Int,
        onComplete: (resp: List<Comment>) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        requestQueue.add(
            getRequest("albums/$albumId/comments",
                Response.Listener<String> { response ->
                    val resp = JSONArray(response)
                    val list = mutableListOf<Comment>()
                    var item: JSONObject? = null
                    for (i in 0 until resp.length()) {
                        item = resp.getJSONObject(i)
                        Log.d("Response", item.toString())
                        list.add(
                            i, Comment(
                                albumId = albumId,
                                rating = item.getInt("rating"),
                                description = item.getString("description"),
                                collectorId = item.getInt("collectorId")
                            )
                        )
                    }
                    onComplete(list)
                },
                Response.ErrorListener {
                    onError(it)
                })
        )
    }

    fun postComment(
        body: JSONObject,
        albumId: Int,
        onComplete: (resp: JSONObject) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        requestQueue.add(
            postRequest("albums/$albumId/comments",
                body,
                Response.Listener<JSONObject> { response ->
                    onComplete(response)
                },
                Response.ErrorListener {
                    onError(it)
                })
        )
    }

    private fun getRequest(
        path: String,
        responseListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL + path, responseListener, errorListener)
    }

    private fun postRequest(
        path: String,
        body: JSONObject,
        responseListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ): JsonObjectRequest {
        return JsonObjectRequest(
            Request.Method.POST,
            BASE_URL + path,
            body,
            responseListener,
            errorListener
        )
    }

    private fun putRequest(
        path: String,
        body: JSONObject,
        responseListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ): JsonObjectRequest {
        return JsonObjectRequest(
            Request.Method.PUT,
            BASE_URL + path,
            body,
            responseListener,
            errorListener
        )
    }

    private fun getBgColor(index:Int): String {
        return if (index % 2 == 0) "#FFFFFF" else "#F1F1F1"
    }
}