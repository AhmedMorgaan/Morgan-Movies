package com.example.ds_movies.ui.actorsSearch

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.ds_movies.R
import com.example.ds_movies.core.SharedPreference
import com.example.ds_movies.core.utils.Constant.Companion.ACTOR
import com.example.ds_movies.core.utils.Constant.Companion.ACTORS_SUGGESTIONS_LIST
import com.example.ds_movies.data.models.ActorItem
import com.example.ds_movies.databinding.FragmentActorsSearchBinding
import com.example.ds_movies.ui.actorsSearch.adapter.ActorsSearchListAdapterPaging
import com.example.ds_movies.ui.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mancj.materialsearchbar.MaterialSearchBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ActorsSearchFragment : BaseFragment<FragmentActorsSearchBinding, ActorsSearchViewModel>(R.layout.fragment_actors_search)  {

    override val viewModel: ActorsSearchViewModel by viewModels()
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
                getActorsSearchResult(s.toString())

            }
        })
        binding.actorsSearchRecyclerview.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                if(binding.searchBar.isSuggestionsVisible){
                    binding.searchBar.hideSuggestionsList()
                    binding.searchBar.clearFocus()
                }
                hideKeyboard()
            }
            false
        }
    }
    private fun getActorsSearchResult(query:String){
        val adapter = ActorsSearchListAdapterPaging()
        binding.apply {
            lifecycleScope.launch {
                viewModel.getActorsSearchResult(query).collect{
                    adapter.submitData(it)

                }
            }
        }
        binding.actorsSearchRecyclerview.adapter = adapter
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.mainProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                if (loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0){
                    binding.searchNoActors.visibility = View.VISIBLE
                }else{
                    binding.searchNoActors.visibility = View.GONE
                }
            }
        }
        handelSearchItemClick(adapter)
    }

    private fun handelSearchItemClick(adapterPaging: ActorsSearchListAdapterPaging){
        adapterPaging.onItemClickListener = object : ActorsSearchListAdapterPaging.OnItemClickListener{
            override fun onItemClick(pos: Int, actorItem: ActorItem?) {
                val bundle = Bundle()
                val actorItemInstance = ActorItem(false,1,actorItem!!.id,null,"","","","",0.0,"")
                bundle.putParcelable(ACTOR,actorItemInstance)
                findNavController().navigate(R.id.action_actorsSearchFragment_to_actorDetailsFragment,bundle)
            }
        }
    }

    private fun  getLastSuggestionsListFromStorage(){
        val lastList =  Gson().fromJson<MutableList<*>>(
            SharedPreference.getString(ACTORS_SUGGESTIONS_LIST,""),
            object : TypeToken<MutableList<*>>() {}.type)
        if(lastList!=null){
            binding.searchBar.lastSuggestions = lastList
            suggestionsList = lastList
        }
    }

    private fun saveSuggestionsList(){
        suggestionsList = binding.searchBar.lastSuggestions as MutableList<*>
        SharedPreference.saveString(ACTORS_SUGGESTIONS_LIST,Gson().toJson(suggestionsList))
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

    override fun getViewBinding(v: View): FragmentActorsSearchBinding {
        return FragmentActorsSearchBinding.bind(v)
    }
}