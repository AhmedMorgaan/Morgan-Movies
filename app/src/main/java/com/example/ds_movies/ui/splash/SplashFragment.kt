package com.example.ds_movies.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ds_movies.R
import com.example.ds_movies.databinding.FragmentSplashBinding
import com.example.ds_movies.ui.base.BaseFragment


class SplashFragment : BaseFragment<FragmentSplashBinding,SplashViewModel>(R.layout.fragment_splash) {
    override val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAnim = AnimationUtils.loadAnimation(context,R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(context,R.anim.bottom_animation)
        binding.splashImage1.animation = topAnim
        binding.splashImage2.animation = bottomAnim

        Handler().postDelayed({
        findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }, 3000)
    }
    override fun getViewBinding(v: View): FragmentSplashBinding {
        return FragmentSplashBinding.bind(v)
    }
}