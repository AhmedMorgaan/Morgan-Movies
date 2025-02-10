package com.morgan.movies.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.morgan.movies.R

abstract class BaseFragment<T : ViewDataBinding , VM : BaseViewModel>(resId :Int) : Fragment(resId) {
     private var baseActivity : BaseActivity<*,*>? = null

    lateinit var binding :T
   protected abstract fun getViewBinding(v : View):T
   protected abstract val viewModel :VM

    override fun onAttach(context: Context) {
        if (context is BaseActivity<*, *>) {
            this.baseActivity = context
        }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewBinding(view)
        showHideProgressBar()
        showHideMassage()
    }

    private fun showHideProgressBar(){
        viewModel.progressBar.observe(viewLifecycleOwner) {
            val mainProgressBar = binding.root.findViewById<ProgressBar>(R.id.main_progress_bar)
            if (it) {
                mainProgressBar.visibility = View.VISIBLE
            } else {
                mainProgressBar.visibility = View.GONE
            }
        }
    }

    fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
    }

    private fun showHideMassage(){
        viewModel.showMessage.observe(viewLifecycleOwner) {
            if (baseActivity != null) {
                baseActivity!!.showMessage(
                    "Error",
                    it,
                    "ok",
                    { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    },
                    null,
                    null,
                    false
                )
            }

        }
    }








}