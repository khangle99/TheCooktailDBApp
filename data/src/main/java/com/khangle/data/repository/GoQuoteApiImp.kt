package com.khangle.data.repository

import com.khangle.data.webservice.GoQuoteBaseApi
import com.khangle.domain.model.Quote
import com.khangle.domain.repository.GoQuoteRepository
import javax.inject.Inject

class GoQuoteRepositoryImp @Inject constructor(private val goQuoteBaseApi: GoQuoteBaseApi): GoQuoteRepository {
    override suspend fun getRandomQuote(): Quote {
        return goQuoteBaseApi.getRandomQuote().quotes[0]
    }

}