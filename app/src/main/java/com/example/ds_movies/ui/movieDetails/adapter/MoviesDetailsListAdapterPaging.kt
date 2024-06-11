package com.example.ds_movies.ui.movieDetails.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ds_movies.R
import com.example.ds_movies.core.SharedPreference
import com.example.ds_movies.core.utils.Constant
import com.example.ds_movies.data.models.CategoryResponse
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.databinding.ItemMovieBinding
import com.example.ds_movies.ui.movieDetails.MoviesDetailsViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesDetailsListAdapterPaging(
    private var viewModel: MoviesDetailsViewModel
) : PagingDataAdapter<MovieItem, MoviesDetailsListAdapterPaging.MyViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
        holder.setIsRecyclable(false)
        GlobalScope.launch(Dispatchers.IO) {
            val castList = viewModel.getMovieCast(movieId = currentItem?.id)
            val adapter = MovieCastsAdapter(castList)
            withContext(Dispatchers.Main) {
                holder.binding.castRecycler.adapter = adapter
            }
        }
        val mScrollChangeListener = object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> {
                        rv.parent.requestDisallowInterceptTouchEvent(true)
                    }
                }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }
        holder.binding.castRecycler.addOnItemTouchListener(mScrollChangeListener)
        holder.binding.btnMovieTrailer.setOnClickListener {
            onItemClickListener?.onItemClick(position,currentItem)
        }
    }

    var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(pos: Int, movie: MovieItem?)
    }

    inner class MyViewHolder(var binding: ItemMovieBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind(moviesItem :MovieItem?){
            binding.model = moviesItem
            Glide.with(binding.root)
                .load(Constant.BASE_POSTER_IMAGE_URL +moviesItem?.posterPath)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.movieImage)

            binding.movieVoteRate.text = String.format("%.1f", moviesItem?.voteAverage)

            val categoriesList = Gson().fromJson<CategoryResponse>(
                SharedPreference.getString(Constant.CATEGORIES_DATA, ""),
                object : TypeToken<CategoryResponse>() {}.type
            ).genres

            val categoriesFilter =  categoriesList.filter {
                moviesItem?.genreIds!!.contains(it.id)
                // it.id in movie?.genreIds
            }.map {
                it.name
            }

            binding.movieCategories.text = categoriesFilter.toString()
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