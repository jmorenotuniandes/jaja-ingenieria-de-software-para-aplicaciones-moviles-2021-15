package com.example.vinyl.ui.artists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.example.vinyl.databinding.FragmentArtistsDetailsBinding

class ArtistDetailsFragment : Fragment() {

    private var binding: FragmentArtistsDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArtistsDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ArtistDetailsFragmentArgs by navArgs()

        args.let {
            val collector = it.artistDetails
            (requireActivity() as AppCompatActivity).supportActionBar?.title = collector.name
            binding!!.txtArtistName.text = collector.name
            // TODO: Setup the rest of the elements of this view
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}