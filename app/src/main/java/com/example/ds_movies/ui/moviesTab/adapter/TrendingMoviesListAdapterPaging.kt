package com.example.ds_movies.ui.moviesTab.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ds_movies.core.utils.Constant
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.databinding.ItemTrendingMovieCardBinding

class TrendingMoviesListAdapterPaging() : PagingDataAdapter<MovieItem, TrendingMoviesListAdapterPaging.MyViewHolder>(
    diffCallBack
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemTrendingMovieCardBinding.inflate(
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

    inner class MyViewHolder(var binding: ItemTrendingMovieCardBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind(moviesItem :MovieItem?){
            binding.model = moviesItem
            Glide.with(itemView.context)
                .load("${Constant.BASE_POSTER_IMAGE_URL}${moviesItem?.posterPath}")
                .into(binding.movieImage)
            binding.movieVoteRate.text = String.format("%.1f", moviesItem?.voteAverage)
        }
    }

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<MovieItem>(){
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}