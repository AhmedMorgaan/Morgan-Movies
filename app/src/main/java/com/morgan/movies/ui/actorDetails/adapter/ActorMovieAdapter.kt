package com.morgan.movies.ui.actorDetails.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.morgan.movies.R
import com.morgan.movies.core.utils.Constant
import com.morgan.movies.data.models.MovieItem
import com.morgan.movies.databinding.ItemMovieSearchBinding

class ActorMovieAdapter(
     var actorMovies: MutableList<MovieItem>?,
) : RecyclerView.Adapter<ActorMovieAdapter.MyViewHolder>() {
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
            onItemClickListener?.onItemClick(position,actorMovies?.get(position))
        }
    }

    override fun getItemCount(): Int {
        return actorMovies?.size ?:0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMoviesList(newActorMovies: MutableList<MovieItem>?){
        actorMovies = newActorMovies
        notifyDataSetChanged()
    }

    var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(pos: Int, actorMovies: MovieItem?)
    }

    inner class MyViewHolder(var binding: ItemMovieSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun onBind(position: Int){
            val moviesItem = actorMovies?.get(position)
            binding.model = moviesItem
            if (moviesItem?.posterPath != null){
                Glide.with(itemView.context)
                    .load("${Constant.BASE_POSTER_IMAGE_URL}${moviesItem.posterPath}")
                    .placeholder(R.drawable.image_place_holder)
                    .into(binding.movieImage)
            }

            val rate = String.format("%.1f", moviesItem?.voteAverage)
            binding.movieVoteRate.text = if (rate == "0.0") "N/A" else rate
        }
    }
}