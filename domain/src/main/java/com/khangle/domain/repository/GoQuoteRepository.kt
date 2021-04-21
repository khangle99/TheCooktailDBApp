package com.khangle.domain.repository

import com.khangle.domain.model.Quote
import com.khangle.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface GoQuoteRepository {
    fun getRandomQuote(forceRefresh: Boolean): Flow<Resource<List<Quote>>>
    companion object {
        const val timeout =  60 * 60 * 1000
    }
}
