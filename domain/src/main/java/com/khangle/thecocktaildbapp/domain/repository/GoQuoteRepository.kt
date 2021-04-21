package com.khangle.thecocktaildbapp.domain.repository

import com.khangle.thecocktaildbapp.domain.model.Quote
import com.khangle.thecocktaildbapp.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface GoQuoteRepository {
    fun getRandomQuote(forceRefresh: Boolean): Flow<Resource<List<Quote>>>
    companion object {
        const val timeout =  60 * 60 * 1000
    }
}
