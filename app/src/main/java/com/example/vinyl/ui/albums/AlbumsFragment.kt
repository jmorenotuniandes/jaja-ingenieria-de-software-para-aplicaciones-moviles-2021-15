package com.example.vinyl.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyl.R
import com.example.vinyl.adapters.AlbumAdapter
import com.example.vinyl.databinding.FragmentAlbumsBinding
import com.example.vinyl.model.dto.Album
import com.example.vinyl.viewmodel.AlbumsViewModel

class AlbumsFragment : Fragment() {

    private var _binding: FragmentAlbumsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var albumsViewModel: AlbumsViewModel
    private var viewModelAdapter: AlbumAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModelAdapter = AlbumAdapter(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.albumsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        albumsViewModel = ViewModelProvider(this, AlbumsViewModel.Factory(activity.application))
            .get(AlbumsViewModel::class.java)
        albumsViewModel.albums.observe(viewLifecycleOwner, Observer<List<Album>> {
            it.apply {
                viewModelAdapter!!.albums = this
            }
        })
        albumsViewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> {
                isNetworkError -> if(isNetworkError) onNetworkError()
        })
        activity.actionBar?.title = getString(R.string.title_albums)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!albumsViewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            albumsViewModel.onNetworkErrorShown()
        }
    }

    fun goToAlbumDetails(album: Album) {
        findNavController()
            .navigate(AlbumsFragmentDirections.actionAlbumsToAlbumsDetails(album))
    }
}