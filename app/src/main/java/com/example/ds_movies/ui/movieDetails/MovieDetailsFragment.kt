package com.example.ds_movies.ui.movieDetails

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ds_movies.R
import com.example.ds_movies.core.utils.Constant
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE_DETAILS
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.databinding.FragmentMovieDetailsBinding
import com.example.ds_movies.ui.base.BaseFragment
import com.example.ds_movies.ui.movieDetails.adapter.MovieCastsAdapter
import com.example.ds_movies.ui.moviesTab.adapter.MoviesListAdapterPaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding, MoviesDetailsViewModel>(R.layout.fragment_movie_details) {

    override val viewModel: MoviesDetailsViewModel by viewModels()
    private var isArLanguage = false
    private var isFavorite = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = arguments?.getParcelable(MOVIE)as MovieItem?
        initMovieDetails(movie)
    }

    private fun initMovieDetails(movie:MovieItem?) {
        initMovieCast(movie?.id)
        handelMovieTrailerClick(movie)
        initRecommendationMoviesAdapter(movie)
        binding.apply {
            model = movie
            Glide.with(binding.root)
                .load(Constant.BASE_POSTER_IMAGE_URL + movie?.posterPath)
                .placeholder(R.drawable.image_place_holder)
                .into(movieImage)
            val rate = String.format("%.1f", movie?.voteAverage)
            movieVoteRate.text = if (rate == "0.0") "N/A" else rate

            movieCategories.text = viewModel.getCategoriesNames(movie?.genreIds)

            btnLang.setOnClickListener {
                isArLanguage = !isArLanguage
                if (isArLanguage) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val newMovieItem = viewModel.getArabicMovieDetails(movie?.id)
                        withContext(Dispatchers.Main) {
                            if (newMovieItem?.overview.isNullOrEmpty()){
                                movieDescription.text = getString(R.string.no_translate_to_arabic)
                            }else {
                                movieDescription.text = newMovieItem?.overview
                            }
                            txtLanguage.text = resources.getString(R.string.english)
                        }
                    }
                } else {
                    movieDescription.text = movie?.overview
                    txtLanguage.text = resources.getString(R.string.arabic)
                }
            }
            if (movie != null) {
                if (viewModel.isMovieExist(movie)){
                    isFavorite = true
                    favoriteIcon.setImageResource(R.drawable.ic_favorite)
                }
                else{
                    isFavorite = false
                    favoriteIcon.setImageResource(R.drawable.ic_non_favorite)
                }
            }
            favoriteIcon.setOnClickListener {
                isFavorite = !isFavorite
                if (movie != null) {
                    if (isFavorite) {
                        viewModel.addFavoriteMovie(movie)
                        favoriteIcon.setImageResource(R.drawable.ic_favorite)
                    } else {
                        viewModel.removeMovieFromFavorites(movie)
                        favoriteIcon.setImageResource(R.drawable.ic_non_favorite)
                    }
                }
            }
        }


    }

    private fun initRecommendationMoviesAdapter(movie:MovieItem?){
        val similarAdapter = MoviesListAdapterPaging()
        lifecycleScope.launch {
            viewModel.getRecommendationMovies(movie?.id).collect {
                similarAdapter.submitData(it)
            }
        }
        binding.recommendationMoviesRecyclerview.adapter = similarAdapter
        lifecycleScope.launch {
            similarAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.mainProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                if (loadStates.refresh !is LoadState.Loading && similarAdapter.itemCount == 0){
                    binding.recommendationNoMovies.visibility = View.VISIBLE
                }else{
                    binding.recommendationNoMovies.visibility = View.GONE
                }
            }
        }
        similarAdapter.onItemClickListener = object : MoviesListAdapterPaging.OnItemClickListener{
            override fun onItemClick(pos: Int, movie: MovieItem?) {
                initMovieDetails(movie)
                binding.mainNestedScrollView.scrollTo(0,0)
            }
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
        binding.recommendationMoviesRecyclerview.addOnItemTouchListener(mSimilarScrollChangeListener)

    }
    private fun initMovieCast(movieId:Int?) {

        lifecycleScope.launch(Dispatchers.IO) {
            val castList = viewModel.getMovieCast(movieId = movieId)
            val adapter = MovieCastsAdapter(castList)
            withContext(Dispatchers.Main) {
                binding.castRecycler.adapter = adapter
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
        binding.castRecycler.addOnItemTouchListener(mScrollChangeListener)

    }
    private fun handelMovieTrailerClick(movie:MovieItem?) {
        binding.btnMovieTrailer.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(MOVIE_DETAILS,movie)
            findNavController().navigate(R.id.action_movieDetailsFragment_to_videoTrailerFragment,bundle)
        }
    }







    override fun getViewBinding(v: View): FragmentMovieDetailsBinding {
        return FragmentMovieDetailsBinding.bind(v)
    }

}