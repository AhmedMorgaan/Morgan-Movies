package com.morgan.movies.ui.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.morgan.movies.ui.base.BaseFragment
import com.morgan.movies.R
import com.morgan.movies.databinding.FragmentSignUpTapBinding

class SignUpFragment :
    BaseFragment<FragmentSignUpTapBinding, SignUpViewModel>(R.layout.fragment_sign_up_tap) {

    override val viewModel by viewModels<SignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        // binding.setLifecycleOwner { this.lifecycle }

        binding.apply {
            btnSignUp.setOnClickListener {
                viewModel.register()
            }
            viewModel.registerSuccess.observe(viewLifecycleOwner) {
//                if (it) {
////                    val intent = Intent(context, SomaActivity::class.java)
////                    startActivity(intent)
//                }
            }

            viewModel.fNameError.observe(viewLifecycleOwner) {
                if (it) {
                    etFirstName.error = "Not valid name"
                }
            }
            viewModel.lNameError.observe(viewLifecycleOwner) {
                if (it) {
                    etLastName.error = "Not valid name"
                }
            }
            viewModel.emailError.observe(viewLifecycleOwner) {
                if (it) {
                    etEmailSignUp.error = "Please enter a valid email"
                }
            }
            viewModel.passwordError.observe(viewLifecycleOwner) {
                if (it) {
                    etPasswordSignUp.error = "Password required minimum 8 characters"
                }
            }
            viewModel.confirmPasswordError.observe(viewLifecycleOwner) {
                if (it) {
                    etConfirmPasswordSignUp.error = "Confirm password is wrong"
                }
            }
        }
    }

    override fun getViewBinding(v: View): FragmentSignUpTapBinding {
        return FragmentSignUpTapBinding.bind(v)
    }
}