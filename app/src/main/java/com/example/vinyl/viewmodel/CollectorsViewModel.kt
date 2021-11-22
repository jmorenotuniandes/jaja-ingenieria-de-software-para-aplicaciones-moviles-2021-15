package com.example.vinyl.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.vinyl.model.dto.Album
import com.example.vinyl.model.dto.Collector
import com.example.vinyl.repository.CollectorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CollectorsViewModel(application: Application) : AndroidViewModel(application) {

    private val _collectors = MutableLiveData<List<Collector>>()
    val collectors: LiveData<List<Collector>>
        get() = _collectors

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var collectorRepository = CollectorRepository(application)

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        try {
            viewModelScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.IO) {
                    var data = collectorRepository.refreshData()
                    _collectors.postValue(data)

                    data.forEachIndexed{ index,collector ->
                        val albums = mutableListOf<Album>()
                        collector.collectorAlbums.forEachIndexed { index2, album ->
                            albums.add(index2, collectorRepository.refreshAlbumsDetailsData(album.albumId))
                        }
                        data[index].collectorAlbums = albums
                        _collectors.postValue(data)
                    }
                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
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
            if (modelClass.isAssignableFrom(CollectorsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}