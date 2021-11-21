package com.example.vinyl.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyl.R
import com.example.vinyl.databinding.AlbumItemBinding
import com.example.vinyl.model.dto.Album
import com.example.vinyl.ui.albums.AlbumsFragment

class AlbumAdapter (private val albumsFragment: AlbumsFragment): RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(val viewDataBinding: AlbumItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.album_item
        }
    }

    var albums: List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val withDataBinding: AlbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AlbumViewHolder.LAYOUT,
            parent,
            false
        )
        return AlbumViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        val linearLayout = holder.itemView.findViewById<LinearLayout>(R.id.album_item_wrapper)
        linearLayout.setBackgroundColor(Color.parseColor(album.bgColor.toString()))
        holder.viewDataBinding.also {
            it.album = album
        }
        holder.viewDataBinding.root.setOnClickListener {
            albumsFragment.goToAlbumDetails(album)
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }
}