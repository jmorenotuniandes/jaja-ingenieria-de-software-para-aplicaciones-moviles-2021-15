package com.example.vinyl.ui.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyl.adapters.AlbumAdapter
import com.example.vinyl.adapters.ArtistAdapter
import com.example.vinyl.databinding.FragmentArtistsBinding
import com.example.vinyl.model.dto.Artist
import com.example.vinyl.viewmodel.ArtistsViewModel
import androidx.navigation.fragment.findNavController

class ArtistsFragment : Fragment() {

    private var _binding: FragmentArtistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var artistsViewModel: ArtistsViewModel
    private var viewModelAdapter: ArtistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtistsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModelAdapter = ArtistAdapter(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.artistsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity){
            "You can only access the viewModel after onActivityCreated()"
        }
        artistsViewModel = ViewModelProvider(this, ArtistsViewModel.Factory(activity.application))
            .get(ArtistsViewModel::class.java)
        artistsViewModel.artists.observe(viewLifecycleOwner, Observer<List<Artist>>{
            it.apply{
                viewModelAdapter!!.artist = this
            }
        })
        artistsViewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean>{
            isNetworkError -> if (isNetworkError) onNetworkError()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError(){
        if(!artistsViewModel.isNetworkErrorShown.value!!){
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            artistsViewModel.onNetworkErrorShown()
        }
    }

    fun goToArtistDetails(artist: Artist) {
        findNavController()
            .navigate(ArtistsFragmentDirections.actionArtistsToArtistsDetails(artist))
    }
}