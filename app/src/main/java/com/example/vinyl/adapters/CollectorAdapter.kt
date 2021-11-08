package com.example.vinyl.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyl.R
import com.example.vinyl.databinding.CollectorItemBinding
import com.example.vinyl.model.dto.Collector

class CollectorAdapter : RecyclerView.Adapter<CollectorAdapter.CollectorViewHolder>() {

    class CollectorViewHolder(val viewDataBinding: CollectorItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.collector_item
        }
    }

    var collectors: List<Collector> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val withDataBinding: CollectorItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CollectorViewHolder.LAYOUT,
            parent,
            false
        )
        return CollectorViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        val collector = collectors[position]
        val linearLayout = holder.itemView.findViewById<LinearLayout>(R.id.collector_item_wrapper)
        linearLayout.setBackgroundColor(Color.parseColor(collector.bgColor.toString()))
        holder.viewDataBinding.also {
            it.collector = collector
        }
        holder.viewDataBinding.root.setOnClickListener {
            Log.d("COLLECTOR_TAG", "ON CLICK LISTENER")
        }
    }

    override fun getItemCount(): Int {
        return collectors.size
    }
}