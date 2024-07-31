package com.example.ds_movies.ui.movieDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.ds_movies.R
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE_DETAILS
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.databinding.FragmentMovieDetailsBinding
import com.example.ds_movies.ui.base.BaseFragment
import com.example.ds_movies.ui.movieDetails.adapter.MovieDetailsAdapter
import com.example.ds_movies.ui.moviesTab.adapter.MoviesListAdapterPaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding, MoviesDetailsViewModel>(R.layout.fragment_movie_details) {

    override val viewModel: MoviesDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = arguments?.getParcelable(MOVIE)as MovieItem?
        initMovieDetails(movie)
    }

    private fun initMovieDetails(movie:MovieItem?){
        val adapter = MovieDetailsAdapter(movie,viewModel)
        binding.moviesRecyclerView.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(binding.moviesRecyclerView)
        val similarAdapter = MoviesListAdapterPaging()
        lifecycleScope.launch {
            viewModel.getSimilarMovies(movie?.id).collect {
                similarAdapter.submitData(it)
            }
        }
        adapter.initSimilarAdapter(similarAdapter)
        adapter.onItemClickListener = object : MovieDetailsAdapter.OnItemClickListener{
            override fun onItemClick(pos: Int, movie: MovieItem?) {
                val bundle = Bundle()
                bundle.putParcelable(MOVIE_DETAILS,movie)
                findNavController().navigate(R.id.action_movieDetailsFragment_to_videoTrailerFragment,bundle)
            }
        }
    }


    override fun getViewBinding(v: View): FragmentMovieDetailsBinding {
        return FragmentMovieDetailsBinding.bind(v)
    }

}