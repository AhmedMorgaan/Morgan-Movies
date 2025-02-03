package com.morgan.movies.ui.actorDetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.morgan.movies.R
import com.morgan.movies.core.utils.Constant
import com.morgan.movies.data.models.MovieItem
import com.morgan.movies.databinding.ItemMovieSearchBinding

class ActorMoviesListAdapterPaging() : PagingDataAdapter<MovieItem, ActorMoviesListAdapterPaging.MyViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemMovieSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
        holder.setIsRecyclable(false)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position,currentItem)
        }
    }

    var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(pos: Int, movie: MovieItem?)
    }

    inner class MyViewHolder(var binding: ItemMovieSearchBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(moviesItem: MovieItem?) {
            binding.model = moviesItem
            Glide.with(binding.root)
                .load(Constant.BASE_POSTER_IMAGE_URL + moviesItem?.posterPath)
                .placeholder(R.drawable.image_place_holder)
                .into(binding.movieImage)

            val rate = String.format("%.1f", moviesItem?.voteAverage)
            binding.movieVoteRate.text = if (rate == "0.0") "N/A" else rate
        }
    }

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}