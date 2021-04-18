package com.khangle.domain.usecase

import com.khangle.domain.model.Quote
import com.khangle.domain.repository.GoQuoteRepository
import javax.inject.Inject


interface GetRandomQuoteUseCase {
    suspend operator fun invoke(): Quote
}
class GetRandomQuoteUseCaseImp @Inject constructor(private val goQuoteRepository: GoQuoteRepository): GetRandomQuoteUseCase {
    override suspend fun invoke(): Quote {
       return goQuoteRepository.getRandomQuote()
    }

}