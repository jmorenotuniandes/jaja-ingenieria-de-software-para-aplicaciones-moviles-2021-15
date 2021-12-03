package com.example.vinyl.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.vinyl.model.dto.Album
import com.example.vinyl.repository.AlbumsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class AlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>>
        get() = _albums

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var _albumDetails = MutableLiveData<Album>()
    val albumDetails: LiveData<Album>
        get() = _albumDetails

    private var albumsRepository = AlbumsRepository(application)

    init {
        refreshDataFromNetwork()
    }

    fun refreshDataFromNetwork(loadForced:Boolean = false) {
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    val data = albumsRepository.refreshData(loadForced)
                    _albums.postValue(data)
                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            }
        } catch (e: Exception){
            Log.d("Error", e.toString())
            _eventNetworkError.value = true
        }
    }

    fun getAlbumDetails(album: Album) {
        try {
            viewModelScope.launch (Dispatchers.Default) {
                withContext(Dispatchers.IO) {
                    var albumDetails = albumsRepository.getAlbum(album)
                    if (albumDetails != null) {
                        _albumDetails.postValue(albumDetails!!)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
            _eventNetworkError.value = true
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}