package com.mikkipastel.blog.model

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ResultResponse<out R> {

    data class Success<out T>(val data: T?) : ResultResponse<T>()
    data class Error(val errorResponse: ErrorResponse?) : ResultResponse<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$errorResponse]"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val ResultResponse<*>.succeeded
    get() = this is ResultResponse.Success && data != null