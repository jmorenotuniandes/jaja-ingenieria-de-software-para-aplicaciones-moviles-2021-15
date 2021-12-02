package com.example.vinyl.ui.albums

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.vinyl.R
import com.example.vinyl.databinding.FragmentTrackToAlbumBinding
import com.example.vinyl.model.dto.Album
import com.example.vinyl.model.dto.Song
import com.example.vinyl.viewmodel.AlbumsViewModel
import com.example.vinyl.viewmodel.TrackToAlbumViewModel

class TrackToAlbumFragment : Fragment() {

    private var _binding: FragmentTrackToAlbumBinding? = null
    private val binding get() = _binding!!

    private lateinit var trackToAlbumViewModel: TrackToAlbumViewModel
    private val albumsViewModel: AlbumsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        trackToAlbumViewModel = ViewModelProvider(this, TrackToAlbumViewModel.Factory(activity.application))
            .get(TrackToAlbumViewModel::class.java)

        trackToAlbumViewModel.eventNetworkError.observe(viewLifecycleOwner, {
            isNetworkError -> if (isNetworkError) onNetworkError()
        })

        trackToAlbumViewModel.eventNetworkSuccess.observe(viewLifecycleOwner, {
            isSuccess -> if (isSuccess) onSubmitSuccess()
        })
    }

    private fun onNetworkError() {
        if(!trackToAlbumViewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            trackToAlbumViewModel.onNetworkErrorShown()
        }
    }

    private fun onSubmitSuccess() {
        if(!trackToAlbumViewModel.isSuccessShown.value!!) {
            Toast.makeText(activity, "Song Saved", Toast.LENGTH_LONG).show()
            trackToAlbumViewModel.onSuccessShown()
            binding.txtSongName.text?.clear()
            binding.txtSongDuration.text?.clear()
            binding.txtSongName.requestFocus()
            albumsViewModel.refreshDataFromNetwork()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrackToAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: TrackToAlbumFragmentArgs by navArgs()
        super.onViewCreated(view, savedInstanceState)

        args.let {
            val album = it.album
            binding.addTrackAlbumName.text = "${getString(R.string.add_track_to_album_desc)} ${album.name}"

            binding.btnAddSongAlbum.setOnClickListener {
                addSongToAlbum(album)
            }
        }
    }

    private fun addSongToAlbum(album: Album) {
        val newSong = Song(
            name = binding.txtSongName.text.toString(),
            duration = binding.txtSongDuration.text.toString()
        )
        trackToAlbumViewModel.postSongToAlbum(album, newSong)
    }

}