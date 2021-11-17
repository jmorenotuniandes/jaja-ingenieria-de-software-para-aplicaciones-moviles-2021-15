package com.example.vinyl.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinyl.R
import com.example.vinyl.databinding.FragmentArtistsDetailsBinding
import com.example.vinyl.model.dto.Artist

class ArtistDetailsAdapter: RecyclerView.Adapter<ArtistDetailsAdapter.ArtistDetailsViewHolder>() {

    var artists: List<Artist> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistDetailsViewHolder {
        val withDataBinding: FragmentArtistsDetailsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ArtistDetailsViewHolder.LAYOUT,
            parent,
            false
        )
        return ArtistDetailsViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ArtistDetailsViewHolder, position: Int) {
        holder.viewDataBinding?.also {
            it.artistDetails = artists[position]
        }
        holder.bind(artists[position])
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    class ArtistDetailsViewHolder(val viewDataBinding: FragmentArtistsDetailsBinding?) :
        RecyclerView.ViewHolder(viewDataBinding?.root!!) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.fragment_artists_details
        }
        fun bind(artist: Artist) {
            Glide.with(itemView)
                .load(artist.image.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .into(viewDataBinding!!.artistDetailsCover)
        }
    }
}