package com.example.vinyl.ui.collectors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.example.vinyl.databinding.FragmentCollectorDetailsBinding


class CollectorDetailsFragment : Fragment() {

    private var binding: FragmentCollectorDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollectorDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: CollectorDetailsFragmentArgs by navArgs()

        args.let {
            val collector = it.collectorDetails
            (requireActivity() as AppCompatActivity).supportActionBar?.title = collector.name
            binding!!.txtCollectorName.text = collector.name
            binding!!.txtCollectorCollection.text = "Collection: ${0}"
            binding!!.txtCollectorMail.text = "Email: ${collector.email}"
            binding!!.txtCollectorPhone.text = "Phone: ${collector.telephone}"

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}