package com.example.ds_movies.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding , VM : BaseViewModel> : AppCompatActivity() {

    lateinit var activity :AppCompatActivity
    lateinit var dataBinding : T
    lateinit var viewModel : VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        dataBinding = DataBindingUtil.setContentView(this , getLayoutId())
        viewModel  = generateViewModel()
    }

    abstract fun getLayoutId():Int
    abstract fun generateViewModel(): VM

    @SuppressLint("SuspiciousIndentation")
    fun showMessage(title:String?,
                    message:String?,
                    posActionName:String?,
                    posAction:DialogInterface.OnClickListener?,
                    negActionName:String?,
                    negAction:DialogInterface.OnClickListener?,
                    isCancelable:Boolean){
        val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle(title)
            dialogBuilder.setMessage(message)
            dialogBuilder.setPositiveButton(posActionName,posAction)
            dialogBuilder.setNegativeButton(negActionName,negAction)
            dialogBuilder.setCancelable(isCancelable)
            dialogBuilder.show()

    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun showMessage(title:Int?,
                    message:Int?,
                    posActionName:Int?,
                    posAction:DialogInterface.OnClickListener?,
                    negActionName:Int?,
                    negAction:DialogInterface.OnClickListener?,
                    isCancelable:Boolean){
        val dialogBuilder = AlertDialog.Builder(this)
        if (title!=null)
        dialogBuilder.setTitle(title)
        if (message!=null)
        dialogBuilder.setMessage(message)
        if (posActionName !=null)
        dialogBuilder.setPositiveButton(posActionName,posAction)
        if (negActionName !=null)
        dialogBuilder.setNegativeButton(negActionName,negAction)
        dialogBuilder.setCancelable(isCancelable)
        dialogBuilder.show()

    }
}