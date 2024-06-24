package com.example.ds_movies.ui.movieDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.d_note.Base.BaseFragment
import com.example.ds_movies.R
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE_DETAILS
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE_TYPE
import com.example.ds_movies.core.utils.Constant.Companion.NOW_PLAYING
import com.example.ds_movies.core.utils.Constant.Companion.POPULAR
import com.example.ds_movies.core.utils.Constant.Companion.TOP_RATED
import com.example.ds_movies.core.utils.Constant.Companion.TRENDING
import com.example.ds_movies.core.utils.Constant.Companion.UP_COMING
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.databinding.FragmentMovieDetailsBinding
import com.example.ds_movies.ui.movieDetails.adapter.MovieDetailsAdapter
import com.example.ds_movies.ui.movieDetails.adapter.MoviesDetailsListAdapterPaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding, MoviesDetailsViewModel>(R.layout.fragment_movie_details) {

    override val viewModel: MoviesDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieType = arguments?.getString(MOVIE_TYPE)
        initMovieRecyclerView(movieType)

    }

    private fun initMovieRecyclerView(movieType:String?){
        val adapter = MoviesDetailsListAdapterPaging(viewModel)
        when(movieType){
            TRENDING -> {
                initTrendingMoviesList(adapter)
            }
            TOP_RATED ->{
                initTopRatedMoviesList(adapter)
            }
            POPULAR ->{
                initPopularMoviesList(adapter)
            }
            NOW_PLAYING ->{
                initNowPlayingMoviesList(adapter)
            }
            UP_COMING ->{
                initUpComingMoviesList(adapter)
            }
            MOVIE ->{
                initMovieDetails()
            }
        }
        handelYoutubeTrailerClick(adapter)
    }

    private fun handelYoutubeTrailerClick(adapter: MoviesDetailsListAdapterPaging){
        adapter.onItemClickListener = object : MoviesDetailsListAdapterPaging.OnItemClickListener{
            override fun onItemClick(pos: Int, movie: MovieItem?) {
                val bundle = Bundle()
                bundle.putParcelable(MOVIE_DETAILS,movie)
                findNavController().navigate(R.id.action_movieDetailsFragment_to_videoTrailerFragment,bundle)
            }
        }
    }
    private fun initMovieDetails(){
        val movie = arguments?.getParcelable(MOVIE)as MovieItem?
        val adapter = MovieDetailsAdapter(movie,viewModel)
        binding.moviesRecyclerView.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(binding.moviesRecyclerView)
        adapter.onItemClickListener = object : MovieDetailsAdapter.OnItemClickListener{
            override fun onItemClick(pos: Int, movie: MovieItem?) {
                val bundle = Bundle()
                bundle.putParcelable(MOVIE_DETAILS,movie)
                findNavController().navigate(R.id.action_movieDetailsFragment_to_videoTrailerFragment,bundle)
            }
        }
    }
    private fun initTrendingMoviesList(adapter: MoviesDetailsListAdapterPaging){
        binding.apply {
            lifecycleScope.launch {
                viewModel.trendingMoviesListPaging.collect{
                    adapter.submitData(it)
                }
            }
        }
        binding.moviesRecyclerView.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(binding.moviesRecyclerView)
    }
    private fun initTopRatedMoviesList(adapter: MoviesDetailsListAdapterPaging){
        binding.apply {
            lifecycleScope.launch {
                viewModel.topRatedMoviesListPaging.collect{
                    adapter.submitData(it)
                }
            }
        }
        binding.moviesRecyclerView.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(binding.moviesRecyclerView)
    }
    private fun initPopularMoviesList(adapter: MoviesDetailsListAdapterPaging){
        binding.apply {
            lifecycleScope.launch {
                viewModel.popularMoviesListPaging.collect{
                    adapter.submitData(it)
                }
            }
        }
        binding.moviesRecyclerView.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(binding.moviesRecyclerView)
    }
    private fun initNowPlayingMoviesList(adapter: MoviesDetailsListAdapterPaging){
        binding.apply {
            lifecycleScope.launch {
                viewModel.nowPlayingMoviesListPaging.collect{
                    adapter.submitData(it)
                }
            }
        }
        binding.moviesRecyclerView.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(binding.moviesRecyclerView)
}
    private fun initUpComingMoviesList(adapter: MoviesDetailsListAdapterPaging){
        binding.apply {
            lifecycleScope.launch {
                viewModel.upComingMoviesListPaging.collect{
                    adapter.submitData(it)
                }
            }
        }
        binding.moviesRecyclerView.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(binding.moviesRecyclerView)
    }

    override fun getViewBinding(v: View): FragmentMovieDetailsBinding {
        return FragmentMovieDetailsBinding.bind(v)
    }

}