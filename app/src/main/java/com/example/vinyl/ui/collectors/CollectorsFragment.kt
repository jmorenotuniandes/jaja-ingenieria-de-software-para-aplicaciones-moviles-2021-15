package com.example.vinyl.ui.collectors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyl.R
import com.example.vinyl.adapters.CollectorAdapter
import com.example.vinyl.databinding.FragmentCollectorsBinding
import com.example.vinyl.model.dto.Collector
import com.example.vinyl.viewmodel.CollectorsViewModel

class CollectorsFragment : Fragment() {

    private var _binding: FragmentCollectorsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var collectorsViewModel: CollectorsViewModel
    private var viewModelAdapter: CollectorAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCollectorsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModelAdapter = CollectorAdapter(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.collectorsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        collectorsViewModel = ViewModelProvider(this, CollectorsViewModel.Factory(activity.application))
            .get(CollectorsViewModel::class.java)
        collectorsViewModel.collectors.observe(viewLifecycleOwner, Observer<List<Collector>> {
            it.apply {
                viewModelAdapter!!.collectors = this
            }
        })
        collectorsViewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> {
                isNetworkError -> if(isNetworkError) onNetworkError()
        })
        activity.actionBar?.title = getString(R.string.title_collectors)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!collectorsViewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            collectorsViewModel.onNetworkErrorShown()
        }
    }

    fun goToCollectorDetails(collector: Collector) {
        findNavController()
            .navigate(CollectorsFragmentDirections.actionCollectorsToCollectorDetails(collector))
    }
}