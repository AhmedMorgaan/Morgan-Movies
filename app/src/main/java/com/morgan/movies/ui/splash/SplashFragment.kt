package com.morgan.movies.ui.splash

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.morgan.movies.R
import com.morgan.movies.core.utils.Utils
import com.morgan.movies.databinding.FragmentSplashBinding
import com.morgan.movies.ui.base.BaseFragment


class SplashFragment : BaseFragment<FragmentSplashBinding,SplashViewModel>(R.layout.fragment_splash) {
    override val viewModel: SplashViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Utils().hideSystemUI(requireActivity().window,R.id.main_view)
        val topAnim = AnimationUtils.loadAnimation(context,R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(context,R.anim.bottom_animation)
        val leftAnim = AnimationUtils.loadAnimation(context,R.anim.slide_in_left)
        binding.splashLogo.animation = topAnim
        binding.splashImage1.animation = leftAnim
        binding.splashImage2.animation = bottomAnim

        viewModel.requestFirebaseTokenWithoutLogin()
        Handler().postDelayed({
        findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }, 5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils().showSystemUI(requireActivity().window,R.id.main_view)
    }
    override fun getViewBinding(v: View): FragmentSplashBinding {
        return FragmentSplashBinding.bind(v)
    }
}