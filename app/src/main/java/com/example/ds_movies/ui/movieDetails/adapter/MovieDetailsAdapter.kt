package com.example.ds_movies.ui.movieDetails.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ds_movies.R
import com.example.ds_movies.core.SharedPreference
import com.example.ds_movies.core.utils.Constant.Companion.BASE_POSTER_IMAGE_URL
import com.example.ds_movies.core.utils.Constant.Companion.CATEGORIES_DATA
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

class MovieDetailsAdapter(
    private var movie: MovieItem?,
    private var viewModel: MoviesDetailsViewModel
) : RecyclerView.Adapter<MovieDetailsAdapter.MyViewHolder>() {
    var isArLanguage = false
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

        holder.onBind()
        GlobalScope.launch(Dispatchers.IO) {
            val castList = viewModel.getMovieCast(movieId = movie?.id)
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
            onItemClickListener?.onItemClick(position, movie)
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(pos: Int, movie: MovieItem?)
    }

    inner class MyViewHolder(var binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
            val movie = movie
            binding.model = movie
            Glide.with(binding.root)
                .load(BASE_POSTER_IMAGE_URL + movie?.posterPath)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.movieImage)

            binding.movieVoteRate.text = String.format("%.1f", movie?.voteAverage)

            val categoriesList = Gson().fromJson<CategoryResponse>(
                SharedPreference.getString(CATEGORIES_DATA, ""),
                object : TypeToken<CategoryResponse>() {}.type
            ).genres
            val categoriesFilter = categoriesList.filter {
                movie?.genreIds!!.contains(it.id)
            }.map {
                it.name
            }
            val categories = categoriesFilter.toString().replace("[", "").replace("]", "")
            binding.movieCategories.text = categories
            binding.btnLang.setOnClickListener {
                isArLanguage = !isArLanguage
                if (isArLanguage) {
                    GlobalScope.launch(Dispatchers.IO) {
                        val newMovieItem = viewModel.getArabicMovieDetails(movie?.id)
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
                    binding.movieDescription.text = movie?.overview
                    binding.txtLanguage.text = "Arabic"
                }
            }
        }
    }
}