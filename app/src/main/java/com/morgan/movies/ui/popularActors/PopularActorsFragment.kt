package com.morgan.movies.ui.popularActors

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.morgan.movies.R
import com.morgan.movies.core.utils.Constant.Companion.ACTOR
import com.morgan.movies.data.models.ActorItem
import com.morgan.movies.databinding.FragmentPopularActorsBinding
import com.morgan.movies.ui.base.BaseFragment
import com.morgan.movies.ui.popularActors.adapter.PopularActorsListAdapterPaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PopularActorsFragment : BaseFragment<FragmentPopularActorsBinding,PopularActorsViewModel>(R.layout.fragment_popular_actors) {

    override val viewModel: PopularActorsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handelBackAndSearchClick()
        initPopularActorsRecyclerview()
    }

    private fun initPopularActorsRecyclerview(){
        val adapter = PopularActorsListAdapterPaging()
        binding.apply {
            lifecycleScope.launch {
                viewModel.popularActorsListPaging.collect {
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
    private fun handelActorItemClick(adapter: PopularActorsListAdapterPaging){
        adapter.onItemClickListener = object : PopularActorsListAdapterPaging.OnItemClickListener{
            override fun onItemClick(pos: Int, actor: ActorItem?) {
                val bundle = Bundle()
                bundle.putParcelable(ACTOR,actor)
                findNavController().navigate(R.id.action_popularActorsFragment_to_actorDetailsFragment,bundle)
            }
        }
    }

    private fun handelBackAndSearchClick() {
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.actorSearchIcon.setOnClickListener {
            findNavController().navigate(R.id.action_popularActorsFragment_to_actorsSearchFragment)
        }
    }
    override fun getViewBinding(v: View): FragmentPopularActorsBinding {
      return  FragmentPopularActorsBinding.bind(v)
    }
}