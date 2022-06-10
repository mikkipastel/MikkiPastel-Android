package com.mikkipastel.blogservice.repository

import com.mikkipastel.blogservice.model.ErrorResponse
import com.mikkipastel.blogservice.model.ResultResponse
import retrofit2.Response

fun <T> Response<T>.toNetworkResult(): ResultResponse<T> {
    try {
        if (this.isSuccessful) {
            this.body()?.let {
                return ResultResponse.Success(it)
            } ?: run {
                return if (this.code() == 204) {
                    ResultResponse.Success(null)
                } else {
                    ResultResponse.Error(
                        ErrorResponse(
                            this.message()
                        )
                    )
                }
            }
        } else {
            val msg = this.errorBody()?.string()
            val errorMsg = if (msg.isNullOrEmpty()) {
                this.message()
            } else {
                msg
            }
            return ResultResponse.Error(
                ErrorResponse(
                    errorMsg,
                    Exception(errorMsg)
                )
            )
        }
    } catch (e: Exception) {
        return ResultResponse.Error(
            ErrorResponse(
                "unknown error",
                e
            )
        )
    }
}