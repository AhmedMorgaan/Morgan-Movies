/*
 *
 *   Created by Yehia Ahmed on 3/5/22, 2:09 AM
 *   Copyright (c) 2022 . All rights reserved.
 *   Last modified 3/5/22, 12:09 AM
 *
 */
package com.ds.morganmovies.core.utils

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.registerDateToSend(year: Int, month: Int, day: Int): String {
    set(year, month, day)
    return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(time)
}

fun Calendar.registerViewingDate(year: Int, month: Int, day: Int): String {
    set(year, month, day)
    return SimpleDateFormat("dd / MM / yyyy", Locale.ENGLISH).format(time)
}