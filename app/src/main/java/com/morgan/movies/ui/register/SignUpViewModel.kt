package com.morgan.movies.ui.register

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.morgan.movies.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : BaseViewModel() {

    val fName = MutableLiveData<String>()
    val lName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    val fNameError = MutableLiveData<Boolean>()
    val lNameError = MutableLiveData<Boolean>()
    val emailError = MutableLiveData<Boolean>()
    val passwordError = MutableLiveData<Boolean>()
    val confirmPasswordError = MutableLiveData<Boolean>()
    val registerSuccess = MutableLiveData<Boolean>()

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    fun register(){

        if (isValidData()){
            progressBar.value=true
            auth.createUserWithEmailAndPassword(email.value?:"",password.value?:"")
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        progressBar.value = false
                        registerSuccess.value = true

                    }
                    else{
                        progressBar.value = false
                        showMessage.value = it.exception?.localizedMessage
                    }
                }

        }
    }

    private fun isValidData():Boolean{
        var isValid = true

        if(!fName.value.isNullOrEmpty() && !fName.value.equals("null",true)
            && fName.value!!.trim().isNotEmpty() && fName.value!!.length >= 3) { null }
        else{ fNameError.value=true
            isValid = false }

        if(!lName.value.isNullOrEmpty() && !lName.value.equals("null",true)
            && lName.value!!.trim().isNotEmpty() && lName.value!!.length >= 3) { null }
        else{ lNameError.value=true
            isValid = false }

        if(!email.value.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) { null }
        else{ emailError.value=true
            isValid = false }

        if(!password.value.isNullOrEmpty() && !password.value.equals("null",true)
            && password.value!!.trim().isNotEmpty() && password.value!!.length >= 8){ null }
        else{ passwordError.value=true
            isValid = false }

        if ( !confirmPassword.value.isNullOrEmpty() && !confirmPassword.value.equals("null",true)
            && confirmPassword.value!!.trim().isNotEmpty() && confirmPassword.value!!.length >= 8
            && password.value == confirmPassword.value ) { null }
        else{
            confirmPasswordError.value= true
            isValid = false }

        return isValid
    }
}