package com.khangle.data.webservice

import com.khangle.domain.model.QuoteResponse
import retrofit2.http.GET

interface GoQuoteBaseApi {
    suspend fun getRandomQuote(): QuoteResponse
}
interface GoQuoteApi : GoQuoteBaseApi {
    @GET("random?count=1")
    override suspend fun getRandomQuote(): QuoteResponse
}