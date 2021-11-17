package com.example.vinyl.ui.artists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinyl.R
import com.example.vinyl.adapters.ArtistDetailsAdapter
import com.example.vinyl.databinding.FragmentArtistsDetailsBinding
import com.example.vinyl.model.dto.Artist
import com.example.vinyl.viewmodel.ArtistsViewModel
import androidx.lifecycle.Observer


class ArtistDetailsFragment : Fragment() {

    private var _binding: FragmentArtistsDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ArtistsViewModel
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

/*


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
        activity.actionBar?.title = getString(R.string.title_albums)
        viewModel = ViewModelProvider(this, AlbumViewModel.Factory(activity.application)).get(AlbumViewModel::class.java)
        viewModel.albums.observe(viewLifecycleOwner, Observer<List<Album>> {
            it.apply {
                viewModelAdapter!!.albums = this
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ArtistDetailsFragmentArgs by navArgs()


/*        viewModel = ViewModelProvider(this, ArtistsViewModel.Factory(requireActivity().application)).get(ArtistsViewModel::class.java)
        viewModel.currentArtist.observe(viewLifecycleOwner, Observer<Artist> {

            (requireActivity() as AppCompatActivity).supportActionBar?.title = it.name
            binding!!.txtArtistName.text = it.name
            binding!!.txtArtistDetails.text = it.description

            Glide.with(this)
                .load(it.image.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .into(binding!!.artistDetailsCover)
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })*/
            args.let {
               val artist = it.artistDetails
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
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}