package com.example.ds_movies.ui.categoryMoviesList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.filter
import com.example.ds_movies.ui.base.BaseFragment
import com.example.ds_movies.R
import com.example.ds_movies.core.utils.Constant
import com.example.ds_movies.core.utils.Constant.Companion.GENRE_ID
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE_TYPE
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.databinding.FragmentCategoryMoviesListBinding
import com.example.ds_movies.ui.categoryMoviesList.adapter.CategoryMoviesListAdapterPaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryMoviesListFragment : BaseFragment<FragmentCategoryMoviesListBinding, CategoryMoviesListViewModel>(R.layout.fragment_category_movies_list) {

    override val viewModel: CategoryMoviesListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieType = arguments?.getString(MOVIE_TYPE)
        val genreId = arguments?.getInt(GENRE_ID)
        initMovieRecyclerView(movieType,genreId)
    }

    private fun initMovieRecyclerView(movieType:String?,genreId:Int?){
        val adapter = CategoryMoviesListAdapterPaging()
        when(movieType){
            Constant.TRENDING -> {
                initTrendingMoviesList(adapter)
            }
            Constant.TOP_RATED ->{
                initTopRatedMoviesList(adapter,genreId)
            }
            Constant.POPULAR ->{
               initPopularMoviesList(adapter,genreId)
            }
            Constant.NOW_PLAYING ->{
                initNowPlayingMoviesList(adapter,genreId)
            }
            Constant.UP_COMING ->{
                initUpComingMoviesList(adapter,genreId)
            }
        }
        handelItemClick(adapter)
    }

    private fun handelItemClick(adapter: CategoryMoviesListAdapterPaging){
        adapter.onItemClickListener = object :CategoryMoviesListAdapterPaging.OnItemClickListener{
            override fun onItemClick(pos: Int, movie: MovieItem?) {
                val bundle = Bundle()
                bundle.putParcelable(MOVIE,movie)
                findNavController().navigate(R.id.action_categoryMoviesListFragment_to_movieDetailsFragment,bundle)
            }
        }
    }

    private fun initTrendingMoviesList(adapter: CategoryMoviesListAdapterPaging){
        binding.apply {
            lifecycleScope.launch {
                viewModel.trendingMoviesListPaging.collect {
                    adapter.submitData(it)
                }
            }
        }
        binding.categoryMoviesListRecyclerview.adapter = adapter
    }

    private fun initTopRatedMoviesList(adapter: CategoryMoviesListAdapterPaging,genreId:Int?){
        binding.apply {
            lifecycleScope.launch {
                if(genreId !=0) {
                    viewModel.topRatedMoviesListPaging.map { pagingData ->
                        pagingData.filter {
                            it.genreIds!!.contains(genreId)
                        }
                    }.collect {
                        adapter.submitData(it)
                    }
                }else{
                    viewModel.topRatedMoviesListPaging.collect{
                        adapter.submitData(it)
                    }
                }
            }
        }
        binding.categoryMoviesListRecyclerview.adapter = adapter
    }
    private fun initPopularMoviesList(adapter: CategoryMoviesListAdapterPaging,genreId:Int?){
        binding.apply {
            lifecycleScope.launch {
                if (genreId != 0) {
                    viewModel.popularMoviesListPaging.map { pagingData ->
                        pagingData.filter {
                            it.genreIds!!.contains(genreId)
                        }
                    }.collect{
                        adapter.submitData(it)
                    }
                } else {
                    viewModel.popularMoviesListPaging.collect {
                        adapter.submitData(it)
                    }
                }
            }
        }
        binding.categoryMoviesListRecyclerview.adapter = adapter
    }
    private fun initNowPlayingMoviesList(adapter: CategoryMoviesListAdapterPaging,genreId:Int?){
        binding.apply {
            lifecycleScope.launch {
                if(genreId !=0) {
                    viewModel.nowPlayingMoviesListPaging.map { pagingData ->
                        pagingData.filter {
                            it.genreIds!!.contains(genreId)
                        }
                    }.collect {
                        adapter.submitData(it)
                    }
                }else{
                    viewModel.nowPlayingMoviesListPaging.collect{
                        adapter.submitData(it)
                    }
                }
            }
        }
        binding.categoryMoviesListRecyclerview.adapter = adapter
    }
    private fun initUpComingMoviesList(adapter: CategoryMoviesListAdapterPaging,genreId:Int?){
        binding.apply {
            lifecycleScope.launch {
                if(genreId !=0) {
                    viewModel.upComingMoviesListPaging.map { pagingData ->
                        pagingData.filter {
                            it.genreIds!!.contains(genreId)
                        }
                    }.collect {
                        adapter.submitData(it)
                    }
                }else{
                    viewModel.upComingMoviesListPaging.collect{
                        adapter.submitData(it)
                    }
                }
            }
        }
        binding.categoryMoviesListRecyclerview.adapter = adapter
    }

    override fun getViewBinding(v: View): FragmentCategoryMoviesListBinding {
        return FragmentCategoryMoviesListBinding.bind(v)
    }


}