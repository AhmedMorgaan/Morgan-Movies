package com.morgan.movies.ui.login

import androidx.lifecycle.MutableLiveData
import com.morgan.movies.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : BaseViewModel() {

     val email = MutableLiveData<String>()
     val password = MutableLiveData<String>()
     val emailError = MutableLiveData<Boolean>(false)
     val passwordError = MutableLiveData<Boolean>(false)
     val errorMessageEmail = MutableLiveData<String>()
     val errorMessagePassword = MutableLiveData<String>()
     val authUser = MutableLiveData<FirebaseUser>()


     private var auth : FirebaseAuth


     init {
          auth = FirebaseAuth.getInstance()
          if (auth.currentUser!=null){
               authUser.value = auth.currentUser
          }
     }

     fun login (){
          emailError.value = false
          passwordError.value = false

          if(isValidData()){
               progressBar.value = true
               auth.signInWithEmailAndPassword(email.value ?:"",password.value ?:"")
                    .addOnCompleteListener { task ->
                         if (task.isSuccessful){
                              progressBar.value = false
                              authUser.value = auth.currentUser

                         }
                         else {
                              showMessage.value = task.exception?.localizedMessage
                              progressBar.value = false
                         }
                    }
          }

     }

     private fun isValidData():Boolean{
          var isValid = true
          if(email.value.isNullOrBlank()){
               //show error
               errorMessageEmail.value = "Please enter a valid email"
               emailError.value = true
               isValid = false
          }
          if(password.value.isNullOrEmpty()){
               // show error
               errorMessagePassword.value = "Please enter a valid password"
               passwordError.value = true
               isValid = false
          }
          return isValid
     }
}