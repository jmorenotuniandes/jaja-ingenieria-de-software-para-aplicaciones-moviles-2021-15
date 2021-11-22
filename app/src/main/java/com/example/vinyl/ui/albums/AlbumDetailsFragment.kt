package com.example.vinyl.ui.albums

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinyl.R
import com.example.vinyl.databinding.FragmentAlbumsDetailsBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.vinyl.adapters.SongDetailsAdapter


class AlbumDetailsFragment : Fragment() {

    private var _binding: FragmentAlbumsDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private var viewModelAdapter: SongDetailsAdapter? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: AlbumDetailsFragmentArgs by navArgs()
        recyclerView = binding.rvAlbumsDetailedSongs
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        args.let {
            val album = it.albumDetails
            viewModelAdapter!!.songs = album.songs
            (requireActivity() as AppCompatActivity).supportActionBar?.title = album.name
            binding.txtAlbumTitle.text = album.name
            binding.txtAlbumDetails.text = album.description

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
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}