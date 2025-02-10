package com.morgan.movies.ui.moviesTab

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.filter
import com.google.android.material.tabs.TabLayout
import com.morgan.movies.R
import com.morgan.movies.core.utils.Constant.Companion.GENRE_ID
import com.morgan.movies.core.utils.Constant.Companion.MOVIE
import com.morgan.movies.core.utils.Constant.Companion.MOVIE_TYPE
import com.morgan.movies.core.utils.Constant.Companion.NOW_PLAYING
import com.morgan.movies.core.utils.Constant.Companion.POPULAR
import com.morgan.movies.core.utils.Constant.Companion.TOP_RATED
import com.morgan.movies.core.utils.Constant.Companion.TRENDING
import com.morgan.movies.core.utils.Constant.Companion.UP_COMING
import com.morgan.movies.core.utils.NetworkHelper
import com.morgan.movies.data.models.Genre
import com.morgan.movies.data.models.MovieItem
import com.morgan.movies.databinding.FragmentMoviesTabBinding
import com.morgan.movies.ui.base.BaseFragment
import com.morgan.movies.ui.moviesTab.adapter.MoviesListAdapterPaging
import com.morgan.movies.ui.moviesTab.adapter.TrendingMoviesListAdapterPaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MoviesTabFragment : BaseFragment<FragmentMoviesTabBinding, MoviesTabViewModel>(R.layout.fragment_movies_tab) {

    override val viewModel: MoviesTabViewModel by viewModels()
     var tabInfo :Genre? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGenresTabs()
        initTrendingRecyclerViewPaging()
        handelSeeMoreClicks()
        handelCategoryNameClicks()
        handelSwipeRefresh()
        handelHideProgressBar()
    }

    private fun handelHideProgressBar() {
        Handler().postDelayed({
            if (binding.topRatedProgressBar.isVisible && binding.popularProgressBar.isVisible &&
                binding.nowPlayingProgressBar.isVisible && binding.upComingProgressBar.isVisible){

                binding.topRatedProgressBar.visibility = View.GONE
                binding.topRatedErrorMessage.visibility = View.VISIBLE

                binding.popularProgressBar.visibility = View.GONE
                binding.popularErrorMessage.visibility = View.VISIBLE

                binding.nowPlayingProgressBar.visibility = View.GONE
                binding.nowPlayingErrorMessage.visibility = View.VISIBLE

                binding.upComingProgressBar.visibility = View.GONE
                binding.upComingErrorMessage.visibility = View.VISIBLE

            }
        },4000)
    }
    private fun handelSwipeRefresh(){
        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(requireContext(),R.color.Red))
        binding.swipeRefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(),R.color.black_op))
        binding.swipeRefresh.setOnRefreshListener {
            if (NetworkHelper().isConnected(requireContext())){
                if(binding.genresTabs.tabCount == 0){
                    viewModel.getMoviesCategories()
                }
                initTrendingRecyclerViewPaging()
                binding.swipeRefresh.isRefreshing = false
            }else{
                viewModel.showMessage.postValue(requireContext().getString(R.string.no_internet_connection))
                binding.swipeRefresh.isRefreshing = false
            }
        }

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
            for (i in 0 until binding.genresTabs.tabCount) {
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
                tabInfo = genre
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
                if (loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0 && !binding.trendingErrorMessage.isVisible){
                    binding.trendingNoMovies.visibility = View.VISIBLE
                }else{
                    binding.trendingNoMovies.visibility = View.GONE
                }
            }
        }
        if (adapter.itemCount==0){
            adapter.retry()
        }
        adapter.onItemClickListener = object : TrendingMoviesListAdapterPaging.OnItemClickListener{
            override fun onItemClick(pos: Int, movie: MovieItem?) {
                val bundle = Bundle()
                bundle.putParcelable(MOVIE,movie)
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
                if (loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0 && !binding.topRatedErrorMessage.isVisible){
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
                if (loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0 && !binding.popularErrorMessage.isVisible){
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
                if (loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0 && !binding.nowPlayingErrorMessage.isVisible){
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
                if (loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0 && !binding.upComingErrorMessage.isVisible){
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
                bundle.putParcelable(MOVIE,movie)
                findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment,bundle)
            }
        }
    }
    private fun handelSeeMoreClicks(){
        binding.apply {
                txtTrendingSeeMore.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putString(MOVIE_TYPE, TRENDING)
                        findNavController().navigate(
                            R.id.action_homeFragment_to_categoryMoviesListFragment,
                            bundle
                        )
                }
                txtTopRatedSeeMore.setOnClickListener {
                    if (isHasAccess()) {
                        val bundle = Bundle()
                        bundle.putString(MOVIE_TYPE, TOP_RATED)
                        if (tabInfo?.id != 0) {
                            bundle.putInt(GENRE_ID, tabInfo!!.id)
                        }
                        findNavController().navigate(
                            R.id.action_homeFragment_to_categoryMoviesListFragment,
                            bundle
                        )
                    }
                }
                txtPopularSeeMore.setOnClickListener {
                    if (isHasAccess()) {
                        val bundle = Bundle()
                        bundle.putString(MOVIE_TYPE, POPULAR)
                        if (tabInfo?.id != 0) {
                            bundle.putInt(GENRE_ID, tabInfo!!.id)
                        }
                        findNavController().navigate(
                            R.id.action_homeFragment_to_categoryMoviesListFragment,
                            bundle
                        )
                    }
                }
                txtNowPlayingSeeMore.setOnClickListener {
                    if (isHasAccess()) {
                        val bundle = Bundle()
                        bundle.putString(MOVIE_TYPE, NOW_PLAYING)
                        if (tabInfo?.id != 0) {
                            bundle.putInt(GENRE_ID, tabInfo!!.id)
                        }
                        findNavController().navigate(
                            R.id.action_homeFragment_to_categoryMoviesListFragment,
                            bundle
                        )
                    }
                }
                txtUpComingSeeMore.setOnClickListener {
                    if (isHasAccess()) {
                        val bundle = Bundle()
                        bundle.putString(MOVIE_TYPE, UP_COMING)
                        if (tabInfo?.id != 0) {
                            bundle.putInt(GENRE_ID, tabInfo!!.id)
                        }
                        findNavController().navigate(
                            R.id.action_homeFragment_to_categoryMoviesListFragment,
                            bundle
                        )
                    }
                }
        }
    }
    private fun handelCategoryNameClicks(){
        binding.apply {
                txtTrending.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString(MOVIE_TYPE, TRENDING)
                    findNavController().navigate(
                        R.id.action_homeFragment_to_categoryMoviesListFragment,
                        bundle
                    )
                }
                txtTopRated.setOnClickListener {
                    if (isHasAccess()) {
                        val bundle = Bundle()
                        bundle.putString(MOVIE_TYPE, TOP_RATED)
                        if (tabInfo?.id != 0) {
                            bundle.putInt(GENRE_ID, tabInfo!!.id)
                        }
                        findNavController().navigate(
                            R.id.action_homeFragment_to_categoryMoviesListFragment,
                            bundle
                        )
                    }
                }
                txtPopular.setOnClickListener {
                    if (isHasAccess()) {
                        val bundle = Bundle()
                        bundle.putString(MOVIE_TYPE, POPULAR)
                        if (tabInfo?.id != 0) {
                            bundle.putInt(GENRE_ID, tabInfo!!.id)
                        }
                        findNavController().navigate(
                            R.id.action_homeFragment_to_categoryMoviesListFragment,
                            bundle
                        )
                    }
                }
                txtNowPlaying.setOnClickListener {
                    if (isHasAccess()) {
                        val bundle = Bundle()
                        bundle.putString(MOVIE_TYPE, NOW_PLAYING)
                        if (tabInfo?.id != 0) {
                            bundle.putInt(GENRE_ID, tabInfo!!.id)
                        }
                        findNavController().navigate(
                            R.id.action_homeFragment_to_categoryMoviesListFragment,
                            bundle
                        )
                    }
                }
                txtUpComing.setOnClickListener {
                    if (isHasAccess()) {
                        val bundle = Bundle()
                        bundle.putString(MOVIE_TYPE, UP_COMING)
                        if (tabInfo?.id != 0) {
                            bundle.putInt(GENRE_ID, tabInfo!!.id)
                        }
                        findNavController().navigate(
                            R.id.action_homeFragment_to_categoryMoviesListFragment,
                            bundle
                        )
                    }
                }
        }
    }
    private fun isHasAccess():Boolean {
        var bool = true
        if (!NetworkHelper().isConnected(requireContext()) && tabInfo == null){
            viewModel.showMessage.postValue(requireContext().getString(R.string.no_internet_connection))
            bool = false
        }
        return bool
    }

    override fun getViewBinding(v: View): FragmentMoviesTabBinding {
        return FragmentMoviesTabBinding.bind(v)
    }
}