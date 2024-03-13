/*
 *
 *   Created by Yehia Ahmed on 3/5/22, 2:09 AM
 *   Copyright (c) 2022 . All rights reserved.
 *   Last modified 3/5/22, 12:09 AM
 *
 */
package com.ds.morganmovies.core.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity

fun Context.toast(uiText: String) {
    Toast.makeText(this, uiText, Toast.LENGTH_SHORT).show()
}
fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.shareEvent(eventLink: String?){
    val i = Intent(Intent.ACTION_SEND)
    i.type = "text/plain"
    i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
    i.putExtra(Intent.EXTRA_TEXT, eventLink)
    startActivity(Intent.createChooser(i, "Share URL"))
}