package com.example.ds_movies.ui.moviesTab

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.filter
import com.example.d_note.Base.BaseFragment
import com.example.ds_movies.R
import com.example.ds_movies.core.utils.Constant
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE_TYPE
import com.example.ds_movies.core.utils.Constant.Companion.NOW_PLAYING
import com.example.ds_movies.core.utils.Constant.Companion.POPULAR
import com.example.ds_movies.core.utils.Constant.Companion.TOP_RATED
import com.example.ds_movies.core.utils.Constant.Companion.TRENDING
import com.example.ds_movies.core.utils.Constant.Companion.UP_COMING
import com.example.ds_movies.data.models.Genre
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.databinding.FragmentMoviesTabBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MoviesTabFragment : BaseFragment<FragmentMoviesTabBinding,MoviesTabViewModel>(R.layout.fragment_movies_tab) {

    override val viewModel: MoviesTabViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGenresTabs()
        initTrendingRecyclerViewPaging()
        handelSeeMoreClicks()
    }

    private fun initGenresTabs() {
        viewModel.getMoviesCategories()
        viewModel.genresList.observe(viewLifecycleOwner) {
            val list = it
            list.add(0, Genre(0, "All"))
            list.forEach { genre ->
                binding.genresTabs.addTab(
                    binding.genresTabs.newTab().setText(genre.name).setTag(genre)
                )
            }
            for (i in 0 until binding.genresTabs.getTabCount()) {
                val tab = (binding.genresTabs.getChildAt(0) as ViewGroup).getChildAt(i)
                val p = tab.layoutParams as MarginLayoutParams
                p.setMargins(0, 0, 20, 0)
                tab.requestLayout()
            }
        }

        binding.genresTabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val genre = tab?.tag as Genre
                initTopRatedRecyclerViewPaging(genre.id)
                initPopularRecyclerViewPaging(genre.id)
                initNowPlayingRecyclerViewPaging(genre.id)
                initUpComingRecyclerViewPaging(genre.id)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val genre = tab?.tag as Genre
                initTopRatedRecyclerViewPaging(genre.id)
                initPopularRecyclerViewPaging(genre.id)
                initNowPlayingRecyclerViewPaging(genre.id)
                initUpComingRecyclerViewPaging(genre.id)
            }
        })
         binding.genresTabs.getTabAt(0)?.select()
    }

    private fun initTrendingRecyclerViewPaging(){
        val adapter = TrendingMoviesListAdapterPaging()
        binding.apply {
            lifecycleScope.launch {
                viewModel.trendingMoviesListPaging.collect{
                    adapter.submitData(it)
                }
            }
        }
        binding.trendingRecyclerview.adapter = adapter

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.trendingProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.trendingErrorMessage.isVisible = loadStates.refresh is LoadState.Error
                if (loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0){
                    binding.trendingNoMovies.visibility = View.VISIBLE
                }else{
                    binding.trendingNoMovies.visibility = View.GONE
                }
            }
        }
        adapter.onItemClickListener = object : TrendingMoviesListAdapterPaging.OnItemClickListener{
            override fun onItemClick(pos: Int, movie: MovieItem?) {
                val bundle = Bundle()
                bundle.putParcelable(Constant.MOVIE,movie)
                bundle.putString(MOVIE_TYPE, Constant.MOVIE)
                findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment,bundle)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initTopRatedRecyclerViewPaging(genreId:Int){
        val adapter = MoviesListAdapterPaging()
        binding.apply {
            lifecycleScope.launch() {
                if(genreId !=0) {
                    viewModel.topRatedMoviesListPaging.map { pagingData ->
                        pagingData.filter {
                            it.genreIds!!.contains(genreId)
                        }
                    }.collect {
                        adapter.notifyDataSetChanged()
                        adapter.submitData(it)
                    }
                }else{
                    viewModel.topRatedMoviesListPaging.collect{
                        adapter.notifyDataSetChanged()
                        adapter.submitData(it)
                    }
                }
            }
        }
        binding.topRatedRecyclerview.adapter = adapter
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.topRatedProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.topRatedErrorMessage.isVisible = loadStates.refresh is LoadState.Error
                if (loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0){
                    binding.topRatedNoMovies.visibility = View.VISIBLE
                }else{
                    binding.topRatedNoMovies.visibility = View.GONE
                }
            }
        }
        handelItemClick(adapter)
//        lifecycleScope.launch {
//            //Your adapter's loadStateFlow here
//            adapter.loadStateFlow.
//            distinctUntilChangedBy {
//                it.refresh
//            }.collect {
//                //you get all the data here
//                val list = adapter.snapshot()
//                Log.e("snapshottt", "initTopRatedRecyclerViewPaging: $list", )
//            }
//        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun initPopularRecyclerViewPaging(genreId:Int){
        val adapter = MoviesListAdapterPaging()
        binding.apply {
            lifecycleScope.launch() {

                if(genreId !=0) {
                    viewModel.popularMoviesListPaging.map { pagingData ->
                        pagingData.filter {
                            it.genreIds!!.contains(genreId)
                        }
                    }.collect {
                        adapter.notifyDataSetChanged()
                        adapter.submitData(it)
                    }
                }else{
                    viewModel.popularMoviesListPaging.collect{
                        adapter.notifyDataSetChanged()
                        adapter.submitData(it)
                    }
                }
            }
        }
        binding.popularRecyclerview.adapter = adapter
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.popularProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.popularErrorMessage.isVisible = loadStates.refresh is LoadState.Error
                if (loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0){
                    binding.popularNoMovies.visibility = View.VISIBLE
                }else{
                    binding.popularNoMovies.visibility = View.GONE
                }
            }
        }
        handelItemClick(adapter)
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun initNowPlayingRecyclerViewPaging(genreId:Int){
        val adapter = MoviesListAdapterPaging()
        binding.apply {
            lifecycleScope.launch() {

                if(genreId !=0) {
                    viewModel.nowPlayingMoviesListPaging.map { pagingData ->
                        pagingData.filter {
                            it.genreIds!!.contains(genreId)
                        }
                    }.collect {
                        adapter.notifyDataSetChanged()
                        adapter.submitData(it)
                    }
                }else{
                    viewModel.nowPlayingMoviesListPaging.collect{
                        adapter.notifyDataSetChanged()
                        adapter.submitData(it)
                    }

                }
            }
        }
        binding.nowPlayingRecyclerview.adapter = adapter
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.nowPlayingProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.nowPlayingErrorMessage.isVisible = loadStates.refresh is LoadState.Error
                if (loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0){
                    binding.nowPlayingNoMovies.visibility = View.VISIBLE
                }else{
                    binding.nowPlayingNoMovies.visibility = View.GONE
                }
            }
        }
        handelItemClick(adapter)
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun initUpComingRecyclerViewPaging(genreId:Int){
        val adapter = MoviesListAdapterPaging()
        binding.apply {
            lifecycleScope.launch() {

                if(genreId !=0) {
                    viewModel.upComingMoviesListPaging.map { pagingData ->
                        pagingData.filter {
                            it.genreIds!!.contains(genreId)
                        }
                    }.collect {
                        adapter.notifyDataSetChanged()
                        adapter.submitData(it)
                    }
                }else{
                    viewModel.upComingMoviesListPaging.collect{
                        adapter.notifyDataSetChanged()
                        adapter.submitData(it)
                    }

                }
            }
        }
        binding.upComingRecyclerview.adapter = adapter
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.upComingProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.upComingErrorMessage.isVisible = loadStates.refresh is LoadState.Error
                if (loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0){
                    binding.upComingNoMovies.visibility = View.VISIBLE
                }else{
                    binding.upComingNoMovies.visibility = View.GONE
                }
            }
        }
        handelItemClick(adapter)
    }

    private fun handelItemClick(adapter: MoviesListAdapterPaging){
        adapter.onItemClickListener = object : MoviesListAdapterPaging.OnItemClickListener{
            override fun onItemClick(pos: Int, movie: MovieItem?) {
                val bundle = Bundle()
                bundle.putParcelable(Constant.MOVIE,movie)
                bundle.putString(MOVIE_TYPE, Constant.MOVIE)
                findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment,bundle)
            }
        }
    }
    private fun handelSeeMoreClicks(){
        binding.apply {
            txtTrendingSeeMore.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(MOVIE_TYPE, TRENDING)
                findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment,bundle)
            }
            txtTopRatedSeeMore.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(MOVIE_TYPE, TOP_RATED)
                findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment,bundle)
            }
            txtPopularSeeMore.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(MOVIE_TYPE, POPULAR)
                findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment,bundle)
            }
            txtNowPlayingSeeMore.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(MOVIE_TYPE, NOW_PLAYING)
                findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment,bundle)
            }
            txtUpComingSeeMore.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(MOVIE_TYPE, UP_COMING)
                findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment,bundle)
            }
        }

    }

    override fun getViewBinding(v: View): FragmentMoviesTabBinding {
        return FragmentMoviesTabBinding.bind(v)
    }
}