/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mikkipastel.blog.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mikkipastel.blog.model.ResultResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

/**
 * Executes business logic synchronously or asynchronously using Coroutines.
 */
abstract class CoroutineUseCase<Type, in Params>(
    private val coroutineDispatcher: CoroutineDispatcher
) where Type : Any {

    abstract suspend fun run(params: Params, needFresh: Boolean = true): ResultResponse<Type>

    open operator fun invoke(
            context: CoroutineContext,
            params: Params
    ): LiveData<ResultResponse<Type>> = liveData(context + coroutineDispatcher) {
        val result = run(params)
        emit(result)
    }
}