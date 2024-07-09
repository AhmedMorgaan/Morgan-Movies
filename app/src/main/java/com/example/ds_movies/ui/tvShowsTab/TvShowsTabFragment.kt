package com.example.ds_movies.ui.tvShowsTab

import android.view.View
import androidx.fragment.app.viewModels
import com.example.ds_movies.ui.base.BaseFragment
import com.example.ds_movies.R
import com.example.ds_movies.databinding.FragmentTvShowsTabBinding


class TvShowsTabFragment : BaseFragment<FragmentTvShowsTabBinding, TvShowsTabViewModel>(R.layout.fragment_tv_shows_tab) {

    override val viewModel: TvShowsTabViewModel by viewModels()

    override fun getViewBinding(v: View): FragmentTvShowsTabBinding {
        return FragmentTvShowsTabBinding.bind(v)
    }

}