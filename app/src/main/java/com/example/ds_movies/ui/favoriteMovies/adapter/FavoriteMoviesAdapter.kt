package com.example.ds_movies.ui.favoriteMovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ds_movies.core.utils.Constant
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.databinding.ItemMovieSearchBinding

class FavoriteMoviesAdapter(
    private var movies: MutableList<MovieItem>?,
) : RecyclerView.Adapter<FavoriteMoviesAdapter.MyViewHolder>() {

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
        holder.onBind(position)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position,movies?.get(position))
        }
    }

    override fun getItemCount(): Int {
        return movies?.size ?:0
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(pos: Int, movie: MovieItem?)
    }

    inner class MyViewHolder(var binding: ItemMovieSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val movie = movies?.get(position)
            binding.model = movie
            Glide.with(itemView.context)
                .load("${Constant.BASE_POSTER_IMAGE_URL}${movie?.posterPath}")
                .into(binding.movieImage)
            val rate = String.format("%.1f", movie?.voteAverage)
            binding.movieVoteRate.text = if (rate == "0.0") "N/A" else rate

        }
    }
}