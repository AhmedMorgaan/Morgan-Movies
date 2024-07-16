package com.example.ds_movies.ui.favoriteMovies

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ds_movies.R
import com.example.ds_movies.core.SharedPreference
import com.example.ds_movies.core.utils.Constant
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.databinding.FragmentFavoriteMoviesBinding
import com.example.ds_movies.ui.base.BaseFragment
import com.example.ds_movies.ui.favoriteMovies.adapter.FavoriteMoviesAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMoviesFragment : BaseFragment<FragmentFavoriteMoviesBinding,FavoriteMoviesViewModel>(R.layout.fragment_favorite_movies) {

    override val viewModel: FavoriteMoviesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handelBackClick()
        initFavoriteRecyclerview()
    }

    private fun initFavoriteRecyclerview(){
        val currentList = Gson().fromJson<MutableList<MovieItem>>(
            SharedPreference.getString(Constant.FAVORITE_LIST,""),
            object : TypeToken<MutableList<MovieItem>>() {}.type)
        Log.e("FAVORITE_LIST", "onBind: $currentList", )
        if (currentList.isNullOrEmpty()){
            binding.favoriteNoMovies.visibility = View.VISIBLE
        }else{
            if (binding.favoriteNoMovies.visibility == View.VISIBLE){
                binding.favoriteNoMovies.visibility = View.GONE
            }
            val adapter = FavoriteMoviesAdapter(currentList)
            binding.favoritesRecyclerview.adapter = adapter
            handelMovieClick(adapter)
        }

    }
    private fun handelMovieClick(adapter: FavoriteMoviesAdapter){
        adapter.onItemClickListener = object : FavoriteMoviesAdapter.OnItemClickListener{
            override fun onItemClick(pos: Int, movie: MovieItem?) {
                val bundle = Bundle()
                bundle.putParcelable(Constant.MOVIE,movie)
                findNavController().navigate(R.id.action_favoriteMoviesFragment_to_movieDetailsFragment,bundle)
            }
        }
    }

    private fun handelBackClick() {
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    override fun getViewBinding(v: View): FragmentFavoriteMoviesBinding {
      return  FragmentFavoriteMoviesBinding.bind(v)
    }
}