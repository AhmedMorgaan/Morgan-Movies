package com.example.d_note.Base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.ds_movies.ui.base.BaseActivity
import com.example.ds_movies.ui.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_movies_tab.view.main_progress_bar

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
            if (it) {
                binding.root.main_progress_bar.visibility = View.VISIBLE
            } else {
                binding.root.main_progress_bar.visibility = View.GONE
            }
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