package com.morgan.movies.ui.tvShowsTab

import android.view.View
import androidx.fragment.app.viewModels
import com.morgan.movies.ui.base.BaseFragment
import com.morgan.movies.R
import com.morgan.movies.databinding.FragmentTvShowsTabBinding


class TvShowsTabFragment : BaseFragment<FragmentTvShowsTabBinding, TvShowsTabViewModel>(R.layout.fragment_tv_shows_tab) {

    override val viewModel: TvShowsTabViewModel by viewModels()

    override fun getViewBinding(v: View): FragmentTvShowsTabBinding {
        return FragmentTvShowsTabBinding.bind(v)
    }

}