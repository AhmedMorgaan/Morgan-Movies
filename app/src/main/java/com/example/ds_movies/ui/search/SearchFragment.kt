package com.example.ds_movies.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ds_movies.R
import com.example.ds_movies.core.SharedPreference
import com.example.ds_movies.core.utils.Constant.Companion.MOVIE
import com.example.ds_movies.core.utils.Constant.Companion.SUGGESTIONS_LIST
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.databinding.FragmentSearchBinding
import com.example.ds_movies.ui.base.BaseFragment
import com.example.ds_movies.ui.search.adapter.SearchMoviesListAdapterPaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mancj.materialsearchbar.MaterialSearchBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(R.layout.fragment_search)  {

    override val viewModel: SearchViewModel by viewModels()
    private lateinit var suggestionsList :MutableList<*>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchBar()
        handelBackPress()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSearchBar(){
        binding.searchBar.openSearch()
        getLastSuggestionsListFromStorage()
        binding.searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener{
            override fun onSearchStateChanged(enabled: Boolean) {
                saveSuggestionsList()
            }
            override fun onSearchConfirmed(text: CharSequence?) {
                saveSuggestionsList()
                binding.searchBar.clearFocus()
                hideKeyboard()
            }
            override fun onButtonClicked(buttonCode: Int) {
            }
        })
        binding.searchBar.addTextChangeListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                getSearchResult(s.toString())

            }
        })
        binding.searchResultRecyclerview.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                if(binding.searchBar.isSuggestionsVisible){
                    binding.searchBar.hideSuggestionsList()
                    binding.searchBar.clearFocus()
                    hideKeyboard()
                }

            }
            false
        }
    }
    private fun getSearchResult(query:String){
        val adapter = SearchMoviesListAdapterPaging()
        binding.apply {
            lifecycleScope.launch {
                viewModel.getSearchResult(query).collect{
                    adapter.submitData(it)

                }
            }
        }
        binding.searchResultRecyclerview.adapter = adapter
        handelSuggestionItemClick(adapter)
    }

    private fun handelSuggestionItemClick(adapterPaging: SearchMoviesListAdapterPaging){
        adapterPaging.onItemClickListener = object :SearchMoviesListAdapterPaging.OnItemClickListener{
            override fun onItemClick(pos: Int, movie: MovieItem?) {
                val bundle = Bundle()
                bundle.putParcelable(MOVIE,movie)
                findNavController().navigate(R.id.action_searchFragment_to_movieDetailsFragment,bundle)
            }
        }
    }

    private fun  getLastSuggestionsListFromStorage(){
        val lastList =  Gson().fromJson<MutableList<*>>(
            SharedPreference.getString(SUGGESTIONS_LIST,""),
            object : TypeToken<MutableList<*>>() {}.type)
        if(lastList!=null){
            binding.searchBar.lastSuggestions = lastList
            suggestionsList = lastList
        }
    }

    private fun saveSuggestionsList(){
        suggestionsList = binding.searchBar.lastSuggestions as MutableList<*>
        SharedPreference.saveString(SUGGESTIONS_LIST,Gson().toJson(suggestionsList))
    }

    override fun onStop() {
        super.onStop()
        saveSuggestionsList()
    }

    private fun handelBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (binding.searchBar.isSuggestionsVisible) {
                // if the player is in fullscreen, exit fullscreen
                binding.searchBar.hideSuggestionsList()
            }
            else {
                findNavController().popBackStack()
            }
        }
    }

    override fun getViewBinding(v: View): FragmentSearchBinding {
        return FragmentSearchBinding.bind(v)
    }
}