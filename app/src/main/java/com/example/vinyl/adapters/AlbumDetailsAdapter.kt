package com.example.vinyl.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyl.R
import com.example.vinyl.databinding.AlbumDetailsItemBinding
import com.example.vinyl.model.dto.Album

class AlbumDetailsAdapter: RecyclerView.Adapter<AlbumDetailsAdapter.ArtistDetailsViewHolder>() {
    class ArtistDetailsViewHolder(val viewDataBinding: AlbumDetailsItemBinding?) :
        RecyclerView.ViewHolder(viewDataBinding?.root!!) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.album_details_item
        }
    }
    var albums = listOf <Album>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistDetailsViewHolder {
        val withDataBinding: AlbumDetailsItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ArtistDetailsViewHolder.LAYOUT,
            parent,
            false
        )
        return ArtistDetailsViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ArtistDetailsViewHolder, position: Int) {
        val album = albums[position]
        holder.viewDataBinding?.also {
            it.album = album
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }


}