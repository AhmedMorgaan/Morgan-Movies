package com.morgan.movies.data.base

data class BaseResponse<T>(
    val MessageCode: Int,
    val Obj: T,
    val Status: Boolean
)