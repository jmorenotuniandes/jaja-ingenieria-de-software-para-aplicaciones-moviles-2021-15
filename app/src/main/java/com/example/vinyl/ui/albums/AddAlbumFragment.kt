package com.example.vinyl.ui.albums

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vinyl.viewmodel.AddAlbumViewModel
import com.example.vinyl.R

class AddAlbumFragment : Fragment() {

    companion object {
        fun newInstance() = AddAlbumFragment()
    }

    private lateinit var viewModel: AddAlbumViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_album_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddAlbumViewModel::class.java)
        // TODO: Use the ViewModel
    }

}