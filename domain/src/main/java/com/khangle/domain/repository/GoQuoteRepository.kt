package com.khangle.domain.repository

import com.khangle.domain.model.Quote

interface GoQuoteRepository {
    suspend fun getRandomQuote(): Quote
}
