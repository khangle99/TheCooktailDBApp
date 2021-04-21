package com.khangle.thecocktaildbapp.data.util

import com.khangle.thecocktaildbapp.domain.model.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val db = query()
    val data = db.first()
    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))
        try {
            saveFetchResult(fetch())
            db.map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            db.map { Resource.Error(throwable, it) }
        }
    } else {
        db.map { Resource.Success(it) }
    }
    emitAll(flow)
}