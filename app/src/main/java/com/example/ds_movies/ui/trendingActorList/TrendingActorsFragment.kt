package com.example.ds_movies.ui.trendingActorList

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.ds_movies.R
import com.example.ds_movies.core.utils.Constant.Companion.ACTOR
import com.example.ds_movies.data.models.ActorItem
import com.example.ds_movies.databinding.FragmentTrendingActorsBinding
import com.example.ds_movies.ui.base.BaseFragment
import com.example.ds_movies.ui.trendingActorList.adapter.TrendingActorsListAdapterPaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrendingActorsFragment : BaseFragment<FragmentTrendingActorsBinding,TrendingActorsViewModel>(R.layout.fragment_trending_actors) {

    override val viewModel: TrendingActorsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handelBackAndSearchClick()
        initTrendingActorsRecyclerview()
    }

    private fun initTrendingActorsRecyclerview(){
        val adapter = TrendingActorsListAdapterPaging()
        binding.apply {
            lifecycleScope.launch {
                viewModel.trendingActorsListPaging.collect {
                    adapter.submitData(it)
                }
            }
        }
        binding.actorsRecyclerview.adapter = adapter
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.mainProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                if (loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0){
                    binding.noActors.visibility = View.VISIBLE
                }else{
                    binding.noActors.visibility = View.GONE
                }
            }
        }
        handelActorItemClick(adapter)

    }
    private fun handelActorItemClick(adapter: TrendingActorsListAdapterPaging){
        adapter.onItemClickListener = object : TrendingActorsListAdapterPaging.OnItemClickListener{
            override fun onItemClick(pos: Int, actor: ActorItem?) {
                val bundle = Bundle()
                bundle.putParcelable(ACTOR,actor)
                findNavController().navigate(R.id.action_trendingActorsFragment_to_actorDetailsFragment,bundle)
            }
        }
    }

    private fun handelBackAndSearchClick() {
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.actorSearchIcon.setOnClickListener {
            findNavController().navigate(R.id.action_trendingActorsFragment_to_actorsSearchFragment)
        }
    }
    override fun getViewBinding(v: View): FragmentTrendingActorsBinding {
      return  FragmentTrendingActorsBinding.bind(v)
    }
}