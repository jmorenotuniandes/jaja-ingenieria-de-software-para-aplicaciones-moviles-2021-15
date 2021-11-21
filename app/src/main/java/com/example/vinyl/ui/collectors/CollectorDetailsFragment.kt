package com.example.vinyl.ui.collectors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyl.adapters.AlbumDetailsAdapter
import com.example.vinyl.databinding.FragmentCollectorDetailsBinding


class CollectorDetailsFragment : Fragment() {

    private var _binding: FragmentCollectorDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private var viewModelAdapter: AlbumDetailsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectorDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModelAdapter = AlbumDetailsAdapter()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: CollectorDetailsFragmentArgs by navArgs()
        recyclerView = binding.rvCollectorAlbums
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        args.let {
            val collector = it.collectorDetails
            viewModelAdapter!!.albums = collector.collectorAlbums
            (requireActivity() as AppCompatActivity).supportActionBar?.title = collector.name
            binding.tvCollectorName.text = collector.name
            binding.tvCollectorSizeCollection.text = "Coleccion: ${collector.collectorAlbums.size} albumes"
            binding.tvCollectorEmail.text = "Correo: ${collector.email}"
            binding.tvCollectorPhone.text = "Tel: ${collector.telephone}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}