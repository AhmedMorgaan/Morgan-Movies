package com.example.ds_movies.ui.moviesTab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ds_movies.core.utils.Constant
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.databinding.ItemMovieCardBinding

class MoviesListAdapter(val items: MutableList<MovieItem>?) : RecyclerView.Adapter<MoviesListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemMovieCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
       return items?.size?:0
    }

    inner class MyViewHolder(var binding: ItemMovieCardBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind(position :Int){
            val movie = items?.get(position)
            binding.model = movie
            Glide.with(itemView.context)
                .load("${Constant.BASE_POSTER_IMAGE_URL}${movie?.posterPath}")
                .into(binding.movieImage)

           binding.movieVoteRate.text = String.format("%.1f", movie?.voteAverage)
           // binding.progressBar.visibility = View.VISIBLE
        }
    }
}