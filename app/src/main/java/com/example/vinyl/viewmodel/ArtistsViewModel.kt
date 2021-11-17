package com.example.vinyl.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.vinyl.model.dto.Artist
import com.example.vinyl.repository.ArtistRepository

class ArtistsViewModel (application: Application) : AndroidViewModel(application) {

    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>>
        get() = _artists
/*
    private val _currentArtist = MutableLiveData<Artist>()
    val currentArtist: LiveData<Artist>
        get() = _currentArtist*/

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var artistRepository = ArtistRepository(application)

    init {
        refreshDataFromNetwork()
    }

   /* fun setCurrentArtist(artist: Artist){
        _currentArtist.postValue(artist)
    }
*/
    private fun refreshDataFromNetwork(){
        artistRepository.refreshData({
                _artists.postValue(it)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            },
            {
                _eventNetworkError.value = true
            }
        )
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}