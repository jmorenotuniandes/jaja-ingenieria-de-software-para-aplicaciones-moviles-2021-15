package com.example.vinyl.ui.albums

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinyl.R
import com.example.vinyl.databinding.FragmentAlbumsDetailsBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.vinyl.adapters.SongDetailsAdapter
import com.example.vinyl.model.dto.Album
import com.example.vinyl.viewmodel.AlbumsViewModel


class AlbumDetailsFragment : Fragment() {

    private var _binding: FragmentAlbumsDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private var viewModelAdapter: SongDetailsAdapter? = null
    private lateinit var addTrackButton: Button
    private lateinit var albumsViewModel: AlbumsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumsDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModelAdapter = SongDetailsAdapter()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        albumsViewModel = ViewModelProvider(this, AlbumsViewModel.Factory(activity.application))
            .get(AlbumsViewModel::class.java)

        albumsViewModel.albumDetails.observe(viewLifecycleOwner, {
            albumDetails: Album -> if (albumDetails != null) loadAlbumDetails(albumDetails)
        })

        val args: AlbumDetailsFragmentArgs by navArgs()
        recyclerView = binding.rvAlbumsDetailedSongs
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        addTrackButton = binding.addNewTrackBtn

        args.let {
            val album = it.albumDetails
            (requireActivity() as AppCompatActivity).supportActionBar?.title = album.name
            loadAlbumDetails(album)
            // Load the latest information from endpoint
            albumsViewModel.getAlbumDetails(album)
        }
    }

    private fun loadAlbumDetails(album:Album) {
        binding.txtAlbumTitle.text = album.name
        binding.txtAlbumDetails.text = album.description

        viewModelAdapter!!.songs = album.songs

        if (album.cover != null) {
            Glide.with(this)
                .load(album.cover!!.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .into(binding.albumDetailsCover)
        }

        addTrackButton.setOnClickListener(View.OnClickListener {
            findNavController()
                .navigate(AlbumDetailsFragmentDirections.actionNavigationAlbumsDetailsToTrackToAlbumFragment(album))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}