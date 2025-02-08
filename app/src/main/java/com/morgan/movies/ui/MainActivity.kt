package com.morgan.movies.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.morgan.movies.R
import com.morgan.movies.core.utils.Permission
import com.morgan.movies.databinding.ActivityMainBinding
import com.morgan.movies.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding,MainActivityViewModel>() {

    private val mainViewModel : MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            Permission.verifyNotificationPermission(this)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun generateViewModel(): MainActivityViewModel {
        return mainViewModel
    }

}