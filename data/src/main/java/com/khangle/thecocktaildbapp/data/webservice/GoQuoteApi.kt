package com.khangle.thecocktaildbapp.data.webservice

import com.khangle.thecocktaildbapp.domain.model.QuoteResponse
import retrofit2.http.GET

interface GoQuoteBaseApi {
    suspend fun fetchRandomQuote(): QuoteResponse
}
interface GoQuoteApi : GoQuoteBaseApi {
    @GET("random?count=1")
    override suspend fun fetchRandomQuote(): QuoteResponse
}