package com.example.ds_movies.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.ds_movies.R
import com.example.ds_movies.core.utils.Utils
import com.example.ds_movies.databinding.ActivityMainBinding
import com.example.ds_movies.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding,MainActivityViewModel>() {

    private val mainViewModel : MainActivityViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils().hideSystemUI(window,R.id.main_view)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun generateViewModel(): MainActivityViewModel {
        return mainViewModel
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()
        Utils().hideSystemUI(window,R.id.main_view)
    }

}