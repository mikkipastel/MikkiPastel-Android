package com.mikkipastel.blogservice.repository

import com.mikkipastel.blogservice.model.ErrorResponse
import com.mikkipastel.blogservice.model.ResultResponse

abstract class DirectNetworkBoundResource<ResultType, ResponseType> :
    DataBoundResource<ResultType, ResponseType>() {

    override suspend fun shouldFetch(data: ResultType?): Boolean = true

    override suspend fun fetchFromNetwork(dbSource: ResultType?) {
        val response: ResultResponse<ResponseType> = try {
            createCall().toNetworkResult()
        } catch (e: Exception) {
            ResultResponse.Error(
                ErrorResponse(
                    e.localizedMessage,
                    e
                )
            )
        }

        when (response) {
            is ResultResponse.Success -> {
                response.data?.let {
                    val data: ResultType = convertToResultType(it)
                    saveCallResult(data)
                    result = ResultResponse.Success(data)
                } ?: run {
                    result = ResultResponse.Success(null)
                }
            }
            is ResultResponse.Error -> {
                result = response
            }
        }
    }

    override suspend fun loadFromDb(): ResultType? {
        return null
    }

    override suspend fun saveCallResult(item: ResultType) = Unit
}