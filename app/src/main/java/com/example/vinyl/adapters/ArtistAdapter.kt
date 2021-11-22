package com.example.vinyl.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyl.R
import com.example.vinyl.databinding.ArtistItemBinding
import com.example.vinyl.model.dto.Artist
import com.example.vinyl.ui.artists.ArtistsFragment

class ArtistAdapter (private val artistsFragment: ArtistsFragment) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    class ArtistViewHolder(val viewDataBinding: ArtistItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.artist_item
        }
    }

    var artist: List<Artist> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val withDataBinding: ArtistItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ArtistViewHolder.LAYOUT,
            parent,
            false
        )
        return ArtistViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artist[position]
        val linearLayout = holder.itemView.findViewById<LinearLayout>(R.id.artist_item_wrapper)
        linearLayout.setBackgroundColor(Color.parseColor(artist.bgColor.toString()))
        holder.viewDataBinding.also {
            it.artist = artist
        }
        holder.viewDataBinding.root.setOnClickListener {
            artistsFragment.goToArtistDetails(artist)
        }
    }

    override fun getItemCount(): Int {
        return artist.size
    }
}