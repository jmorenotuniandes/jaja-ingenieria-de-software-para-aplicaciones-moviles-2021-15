package com.example.vinyl.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyl.R
import com.example.vinyl.databinding.SongDetailsItemBinding
import com.example.vinyl.model.dto.Song

class SongDetailsAdapter: RecyclerView.Adapter<SongDetailsAdapter.SongDetailsViewHolder>() {
    class SongDetailsViewHolder(val viewDataBinding: SongDetailsItemBinding?) :
        RecyclerView.ViewHolder(viewDataBinding?.root!!) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.song_details_item
        }
    }
    var songs = listOf <Song>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongDetailsViewHolder {
        val withDataBinding: SongDetailsItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            SongDetailsViewHolder.LAYOUT,
            parent,
            false
        )
        return SongDetailsViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: SongDetailsViewHolder, position: Int) {
        val album = songs[position]
        holder.viewDataBinding?.also {
            it.song = album
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }


}