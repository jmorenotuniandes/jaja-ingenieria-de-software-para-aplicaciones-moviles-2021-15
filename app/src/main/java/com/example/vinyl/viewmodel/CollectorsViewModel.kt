package com.example.vinyl.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.vinyl.model.dto.Collector
import com.example.vinyl.repository.CollectorRepository
import com.example.vinyls_jetpack_application.network.NetworkServiceAdapter

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
        collectorRepository.refreshData({
            _collectors.postValue(it)
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
            if (modelClass.isAssignableFrom(CollectorsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}