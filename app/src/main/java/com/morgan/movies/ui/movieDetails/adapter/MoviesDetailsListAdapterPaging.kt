package com.morgan.movies.ui.movieDetails.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.morgan.movies.R
import com.morgan.movies.core.SharedPreference
import com.morgan.movies.core.utils.Constant
import com.morgan.movies.data.models.CategoryResponse
import com.morgan.movies.data.models.MovieItem
import com.morgan.movies.databinding.ItemMovieDetailsBinding
import com.morgan.movies.ui.movieDetails.MoviesDetailsViewModel
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
    var isArLanguage = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemMovieDetailsBinding.inflate(
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
            onItemClickListener?.onItemClick(position, currentItem)
        }
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(pos: Int, movie: MovieItem?)
    }

    inner class MyViewHolder(var binding: ItemMovieDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @OptIn(DelicateCoroutinesApi::class)
        @SuppressLint("SetTextI18n")
        fun onBind(moviesItem: MovieItem?) {
            binding.model = moviesItem
            Glide.with(binding.root)
                .load(Constant.BASE_POSTER_IMAGE_URL + moviesItem?.posterPath)
                .placeholder(R.drawable.image_place_holder)
                .into(binding.movieImage)

            val rate = String.format("%.1f", moviesItem?.voteAverage)
            binding.movieVoteRate.text = if (rate == "0.0") "N/A" else rate

            val categoriesList = Gson().fromJson<CategoryResponse>(
                SharedPreference.getString(Constant.CATEGORIES_DATA, ""),
                object : TypeToken<CategoryResponse>() {}.type
            ).genres
            val categoriesFilter = categoriesList.filter {
                moviesItem?.genreIds!!.contains(it.id)
            }.map {
                it.name
            }
            val categories = categoriesFilter.toString().replace("[", "").replace("]", "")
            binding.movieCategories.text = categories

            binding.btnLang.setOnClickListener {
                isArLanguage = !isArLanguage
                if (isArLanguage) {
                    GlobalScope.launch(Dispatchers.IO) {
                        val newMovieItem = viewModel.getArabicMovieDetails(moviesItem?.id)
                        withContext(Dispatchers.Main) {
                            if (newMovieItem?.overview.isNullOrEmpty()){
                                binding.movieDescription.text = "للأسف لا يوجد ترجمة للغة العربية "
                            }else {
                                binding.movieDescription.text = newMovieItem?.overview
                            }
                            binding.txtLanguage.text = "English"
                        }
                    }
                } else {
                    binding.movieDescription.text = moviesItem?.overview
                    binding.txtLanguage.text = "Arabic"
                }

            }
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