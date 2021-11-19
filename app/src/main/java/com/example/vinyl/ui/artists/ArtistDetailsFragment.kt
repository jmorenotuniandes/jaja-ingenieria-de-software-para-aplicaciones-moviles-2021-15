package com.example.vinyl.ui.artists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinyl.R
import com.example.vinyl.adapters.ArtistDetailsAdapter
import com.example.vinyl.databinding.FragmentArtistsDetailsBinding
import com.example.vinyl.viewmodel.ArtistsViewModel
import androidx.recyclerview.widget.LinearLayoutManager


class ArtistDetailsFragment : Fragment() {

    private var _binding: FragmentArtistsDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var artistsViewModel: ArtistsViewModel
    private var viewModelAdapter: ArtistDetailsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtistsDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModelAdapter = ArtistDetailsAdapter()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: ArtistDetailsFragmentArgs by navArgs()
        recyclerView = binding.artistDetailedAlbumsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        args.let {
           val artist = it.artistDetails
            viewModelAdapter!!.albums = artist.albums
            (requireActivity() as AppCompatActivity).supportActionBar?.title = artist.name
            binding!!.txtArtistName.text = artist.name
            binding!!.txtArtistDetails.text = artist.description

            Glide.with(this)
                .load(artist.image.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .into(binding!!.artistDetailsCover)
        }
    }

    private fun onNetworkError() {
        if(!artistsViewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            artistsViewModel.onNetworkErrorShown()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}