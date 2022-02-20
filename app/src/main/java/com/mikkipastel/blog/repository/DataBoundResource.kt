package com.mikkipastel.blog.repository

import com.mikkipastel.blog.model.ErrorResponse
import com.mikkipastel.blog.model.ResultResponse
import retrofit2.Response

abstract class DataBoundResource<ResultType, ResponseType> {

    protected var result: ResultResponse<ResultType> = ResultResponse.Error(
        ErrorResponse()
    )

    suspend fun asResult(): ResultResponse<ResultType> {
        val dbSource = loadFromDb()
        val shouldFetch = shouldFetch(dbSource)
        if (shouldFetch) {
            fetchFromNetwork(dbSource)
        } else {
            return ResultResponse.Success(dbSource)
        }
        return result
    }

    protected abstract suspend fun fetchFromNetwork(dbSource: ResultType?)

    protected abstract suspend fun saveCallResult(item: ResultType)

    protected abstract suspend fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun loadFromDb(): ResultType?

    protected abstract suspend fun createCall(): Response<ResponseType>

    protected abstract suspend fun convertToResultType(response: ResponseType): ResultType

}
