package com.morgan.movies.ui.movieDetails.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.morgan.movies.R
import com.morgan.movies.core.utils.Constant.Companion.BASE_POSTER_IMAGE_URL
import com.morgan.movies.data.models.MovieItem
import com.morgan.movies.databinding.ItemMovieDetailsBinding
import com.morgan.movies.ui.movieDetails.MoviesDetailsViewModel
import com.morgan.movies.ui.moviesTab.adapter.MoviesListAdapterPaging
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
    var isFavorite = false
     var mSimilarAdapter :MoviesListAdapterPaging? = null
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

        if (mSimilarAdapter != null) {
            holder.binding.recommendationMoviesRecyclerview.adapter = mSimilarAdapter
        }
        val mSimilarScrollChangeListener = object : RecyclerView.OnItemTouchListener {
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
        holder.binding.recommendationMoviesRecyclerview.addOnItemTouchListener(mSimilarScrollChangeListener)
    }

    fun initSimilarAdapter(similarAdapter :MoviesListAdapterPaging) {
        mSimilarAdapter = similarAdapter
    }

    override fun getItemCount(): Int {
        return 1
    }

    var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(pos: Int, movie: MovieItem?)
    }

    inner class MyViewHolder(var binding: ItemMovieDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @OptIn(DelicateCoroutinesApi::class)
        @SuppressLint("SetTextI18n")
        fun onBind() {
            val movie = movie
            binding.model = movie
            Glide.with(binding.root)
                .load(BASE_POSTER_IMAGE_URL + movie?.posterPath)
                .placeholder(R.drawable.image_place_holder)
                .into(binding.movieImage)

            val rate = String.format("%.1f", movie?.voteAverage)
            binding.movieVoteRate.text = if (rate == "0.0") "N/A" else rate

            binding.movieCategories.text = viewModel.getCategoriesNames(movie?.genreIds)

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
            if (movie != null) {
                if (viewModel.isMovieExist(movie)){
                    isFavorite = true
                    binding.favoriteIcon.setImageResource(R.drawable.ic_favorite)
                }
            }
            binding.favoriteIcon.setOnClickListener {
                isFavorite = !isFavorite
                if (movie != null) {
                    if (isFavorite) {
                        viewModel.addFavoriteMovie(movie)
                        binding.favoriteIcon.setImageResource(R.drawable.ic_favorite)
                    } else {
                        viewModel.removeMovieFromFavorites(movie)
                        binding.favoriteIcon.setImageResource(R.drawable.ic_non_favorite)
                    }
                }
            }
        }
    }
}