package com.example.ds_movies.ui

import androidx.activity.viewModels
import com.example.ds_movies.R
import com.example.ds_movies.databinding.ActivityMainBinding
import com.example.ds_movies.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding,MainActivityViewModel>() {

    private val mainViewModel : MainActivityViewModel by viewModels()

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun generateViewModel(): MainActivityViewModel {
        return mainViewModel
    }

}