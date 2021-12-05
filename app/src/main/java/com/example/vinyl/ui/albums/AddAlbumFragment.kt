package com.example.vinyl.ui.albums

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.vinyl.viewmodel.AddAlbumViewModel
import com.example.vinyl.databinding.AddAlbumFragmentBinding
import com.example.vinyl.model.dto.Album
import com.example.vinyl.model.dto.Song

class AddAlbumFragment : Fragment() {

    private var _binding: AddAlbumFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var albumsViewModel: AddAlbumViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddAlbumFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        albumsViewModel = ViewModelProvider(this, AddAlbumViewModel.Factory(activity.application))
            .get(AddAlbumViewModel::class.java)

        albumsViewModel.eventNetworkError.observe(viewLifecycleOwner, {
                isNetworkError -> if (isNetworkError) onNetworkError()
        })

        albumsViewModel.eventNetworkSuccess.observe(viewLifecycleOwner, {
                isSuccess -> if (isSuccess) onSubmitSuccess()
        })
    }

    private fun onNetworkError() {
        if(!albumsViewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            albumsViewModel.onNetworkErrorShown()
        }
    }

    private fun onSubmitSuccess() {
        if(!albumsViewModel.isSuccessShown.value!!) {
            Toast.makeText(activity, "Album Saved", Toast.LENGTH_LONG).show()
            albumsViewModel.onSuccessShown()
            binding.txtAddAlbumName.text?.clear()
            binding.txtAddAlbumCover.text?.clear()
            binding.txtAddAlbumCalendar.text?.clear()
            binding.txtAddAlbumDescription.text?.clear()
            binding.txtAddAlbumGender.text?.clear()
            binding.txtAddAlbumRecordLabel.text?.clear()
            binding.txtAddAlbumName.requestFocus()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddAlbum.setOnClickListener {
            addAlbum()
        }
    }

    private fun addAlbum() {
        val newAlbum = Album(
            albumId = 0,
            name = binding.txtAddAlbumName.text.toString(),
            cover = binding.txtAddAlbumCover.text.toString(),
            releaseDate = binding.txtAddAlbumCalendar.text.toString(),
            description = binding.txtAddAlbumDescription.text.toString(),
            gender = binding.txtAddAlbumGender.text.toString(),
            recordLabel = binding.txtAddAlbumRecordLabel.text?.toString(),
            songs = mutableListOf<Song>(),
        )
        albumsViewModel.postAlbum(newAlbum)
    }
}