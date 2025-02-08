package com.morgan.movies.core

import android.content.Context
import android.content.SharedPreferences

class SharedPreference {
    companion object{
        private lateinit var sharedPreferences: SharedPreferences
        private val preferenceName = "Morgan-Preferences"
       // val keyID = "key_id"

        fun init (context : Context){
            sharedPreferences = context.getSharedPreferences(preferenceName,Context.MODE_PRIVATE)
        }
        fun saveString (key:String,value:String?) {
            val editor = sharedPreferences.edit()
            editor.putString(key,value)
            editor.apply()
        }
        fun saveInt (key:String,value:Int) {
            val editor = sharedPreferences.edit()
            editor.putInt(key,value)
            editor.apply()
        }
        fun removeAllData(){
            sharedPreferences.edit().clear().apply()
        }
        fun getString(key: String,defValue:String?) = sharedPreferences.getString(key,defValue)
        fun getInt(key: String,defValue:Int) = sharedPreferences.getInt(key,defValue)
    }
}