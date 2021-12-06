package com.example.vinyl.model.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vinyl.model.dto.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkServiceAdapter constructor(context: Context) {
    companion object {
        const val BASE_URL = "https://back-vinyls-populated.herokuapp.com/"
      
        // const val BASE_URL = "https://back-vynils-jsvanegaso.herokuapp.com/"
        private var instance: NetworkServiceAdapter? = null
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

    suspend fun getAlbums() = suspendCoroutine<List<Album>> { cont ->
        requestQueue.add(getRequest("albums",
                { response ->
                    val resp = JSONArray(response)
                    val albums = mutableListOf<Album>()

                    var albumObject: JSONObject?
                    var songsArray: JSONArray?

                    var songObject: JSONObject?
                    var song: Song?

                    for (i in 0 until resp.length()) {
                        val songs = mutableListOf<Song>()
                        albumObject = resp.getJSONObject(i)
                        songsArray = albumObject.getJSONArray("tracks")

                        for (j in 0 until songsArray.length()){
                            songObject = songsArray.getJSONObject(j)
                            song = Song(
                                name = songObject.getString("name"),
                                duration = songObject.getString("duration")
                            )
                            songs.add(j, song)
                        }

                        albums.add(i, Album(
                            albumId = albumObject.getInt("id"),
                            name = albumObject.getString("name"),
                            description = albumObject.getString("description"),
                            cover = albumObject.getString("cover"),
                            bgColor = getBgColor(i),
                            songs = songs
                        ))
                    }
                    cont.resume(albums)
                },
                {
                    cont.resumeWithException(it)
                })
        )
    }

    suspend fun getCollectors() = suspendCoroutine<List<Collector>> { cont ->
        requestQueue.add(getRequest("collectors",
            { response ->
                val resp = JSONArray(response)
                val collectors = mutableListOf<Collector>()

                var collectorObject: JSONObject
                var collectorAlbumsArray: JSONArray

                var collectorAlbumObject: JSONObject
                var collectorAlbum: Album


                for (i in 0 until resp.length()) {
                    val collectorAlbums = mutableListOf<Album>()
                    collectorObject = resp.getJSONObject(i)
                    collectorAlbumsArray = collectorObject.getJSONArray("collectorAlbums")

                    for (j in 0 until collectorAlbumsArray.length()){
                        collectorAlbumObject = collectorAlbumsArray.getJSONObject(j)
                        collectorAlbum = Album(
                            albumId = collectorAlbumObject.getInt("id"),
                            name = "",
                            description="",
                            releaseDate= "",
                            songs = mutableListOf<Song>()
                        )
                        collectorAlbums.add(j,collectorAlbum)
                    }
                    collectors.add(i, Collector(
                        collectorId = collectorObject.getInt("id"),
                        name = collectorObject.getString("name"),
                        telephone = collectorObject.getString("telephone"),
                        email = collectorObject.getString("email"),
                        bgColor = getBgColor(i),
                        collectorAlbums =  collectorAlbums
                    ))
                }
                cont.resume(collectors)
            },
            {
                cont.resumeWithException(it)
            }
        ))
    }

    suspend fun getAlbum( albumId:Int) = suspendCoroutine<Album> {cont ->
        requestQueue.add(getRequest("albums/$albumId",
            { response ->
                val albumObject = JSONObject(response)
                val songs = mutableListOf<Song>()
                val jsonSongs = albumObject.getJSONArray("tracks")
                for (i in 0 until jsonSongs.length()) {
                    val track = jsonSongs.getJSONObject(i)
                    songs.add(Song(
                        name = track.getString("name"),
                        duration = track.getString("duration")
                    ))
                }

                val album = Album(
                    albumId= albumObject.getInt("id"),
                    name = albumObject.getString("name"),
                    description=albumObject.getString("description"),
                    releaseDate= albumObject.getString("releaseDate").take(4),
                    songs = songs
                )
                cont.resume(album)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun addSongToAlbum(song: Song, album:Album) = suspendCoroutine<Boolean> { cont ->
        val bodyArgs = mutableMapOf("name" to song.name, "duration" to song.duration)
        val body = JSONObject(bodyArgs as Map<*, *>?)
        requestQueue.add(
            postRequest("albums/${album.albumId}/tracks",
                body,
                { response ->
                    cont.resume(true)
                },
                {
                    cont.resumeWithException(it)
                }
            )
        )
    }

    suspend fun addAlbum(album:Album) = suspendCoroutine<Boolean> { cont ->
        val bodyArgs = mutableMapOf("name" to album.name,
            "cover" to album.cover,
            "releaseDate" to album.releaseDate,
            "description" to album.description,
            "genre" to album.genre,
            "recordLabel" to album.recordLabel)
        val body = JSONObject(bodyArgs as Map<*, *>?)
        requestQueue.add(
            postRequest("albums",
                body,
                { response ->
                    cont.resume(true)
                    Log.d("Se guardó con éxito!", response.toString())
                },
                {
                    cont.resumeWithException(it)
                }
            )
        )
    }

    suspend fun getArtists() = suspendCoroutine<List<Artist>>{cont ->
        requestQueue.add(
            getRequest("musicians",
                { response ->
                    val resp = JSONArray(response)
                    val artists = mutableListOf<Artist>()

                    var artistObject: JSONObject
                    var albumsArray: JSONArray

                    var albumObject: JSONObject
                    var album: Album

                    for (i in 0 until resp.length()) {
                        val albums = mutableListOf<Album>()
                        artistObject = resp.getJSONObject(i)
                        albumsArray = artistObject.getJSONArray("albums")

                        for( j in 0 until albumsArray.length()){
                            albumObject = albumsArray.getJSONObject(j)
                            album = Album(
                                albumId = albumObject.getInt("id"),
                                name = albumObject.getString("name"),
                                description=albumObject.getString("description"),
                                releaseDate= albumObject.getString("releaseDate").take(4),
                                songs = mutableListOf<Song>()
                            )
                            albums.add(j,album)
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
                    cont.resume(artists)
                },
                {
                    cont.resumeWithException(it)
                })
        )
    }

    suspend fun getComments(albumId: Int) = suspendCoroutine<List<Comment>> { cont ->
        requestQueue.add(
            getRequest("albums/$albumId/comments",
                { response ->
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
                    cont.resume(list)
                },
                {
                    cont.resumeWithException(it)
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