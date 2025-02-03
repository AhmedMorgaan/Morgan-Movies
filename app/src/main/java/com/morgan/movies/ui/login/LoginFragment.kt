package com.morgan.movies.ui.login

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.morgan.movies.R
import com.morgan.movies.databinding.FragmentLoginTapBinding
import com.morgan.movies.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login_tap.et_email_login
import kotlinx.android.synthetic.main.fragment_login_tap.tv_email_login

class LoginFragment : BaseFragment<FragmentLoginTapBinding, LoginViewModel>(R.layout.fragment_login_tap) {

    override val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
       // binding.setLifecycleOwner { this.lifecycle }

        viewModel.authUser.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }


        val slideInLeft = AnimationUtils.loadAnimation(context,R.anim.slide_in_left)

//        val tvEmail = root.findViewById<TextView>(R.id.tv_email_login)
//        val etEmail = root.findViewById<EditText>(R.id.et_email_login)


//        tv_email_login.startAnimation(slideInLeft)
        et_email_login.startAnimation(slideInLeft)


        tv_email_login.translationX = 800f
//        et_email_login.translationX = 800f
//        tv_password_login.translationX = 800f
//        et_password_login.translationX = 800f
//        tv_forget_password_login.translationX = 800f
//        btn_login.translationX = 800f


        tv_email_login.alpha = 0f
//        et_email_login.alpha = 0f
//        tv_password_login.alpha = 0f
//        et_password_login.alpha = 0f
//        tv_forget_password_login.alpha = 0f
//        btn_login.alpha = 0f


        tv_email_login.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(300).start()
//        et_email_login.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(300).start()
//        tv_password_login.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()
//        et_password_login.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()
//        tv_forget_password_login.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(600).start()
//        btn_login.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(700).start()

    }

    override fun getViewBinding(v: View): FragmentLoginTapBinding {
        return FragmentLoginTapBinding.bind(v)
    }
}