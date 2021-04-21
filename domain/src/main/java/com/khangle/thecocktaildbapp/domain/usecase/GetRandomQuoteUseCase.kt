package com.khangle.thecocktaildbapp.domain.usecase

import com.khangle.thecocktaildbapp.domain.model.Quote
import com.khangle.thecocktaildbapp.domain.model.Resource
import com.khangle.thecocktaildbapp.domain.repository.GoQuoteRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject


interface GetRandomQuoteUseCase {
    operator fun invoke(forceRefresh: Boolean): Flow<Resource<List<Quote>>>
}

class GetRandomQuoteUseCaseImp @Inject constructor(private val goQuoteRepository: GoQuoteRepository) :
    GetRandomQuoteUseCase {
    override fun invoke(forceRefresh: Boolean): Flow<Resource<List<Quote>>> {
        return goQuoteRepository.getRandomQuote(forceRefresh)
    }
}