package com.morgan.movies.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.morgan.movies.R


class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        btn_Create_new_account.setOnClickListener {
//            findNavController().navigate(R.id.action_welcomeFragment_to_signUpFragment)
//        }
//        btn_signin.setOnClickListener {
//            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
//        }
    }


}