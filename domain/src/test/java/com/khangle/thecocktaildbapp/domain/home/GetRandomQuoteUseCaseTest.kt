package com.khangle.thecocktaildbapp.domain.home

import com.google.common.truth.Truth
import com.khangle.thecocktaildbapp.domain.model.Quote
import com.khangle.thecocktaildbapp.domain.model.Resource
import com.khangle.thecocktaildbapp.domain.repository.GoQuoteRepository
import com.khangle.thecocktaildbapp.domain.usecase.GetRandomQuoteUseCase
import com.khangle.thecocktaildbapp.domain.usecase.GetRandomQuoteUseCaseImp
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetRandomQuoteUseCaseTest {
    var objectUnderTest: GetRandomQuoteUseCase? = null

    @MockK
    lateinit var quoteRepository: GoQuoteRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = GetRandomQuoteUseCaseImp(quoteRepository)
    }

    @Test
    fun test_getRandomQuote_returnQuoteFlow() {
        //given
        val sampleFlow = flow<Resource<List<Quote>>> { emit(Resource.Success(data = emptyList())) }
        every { quoteRepository.getRandomQuote(true) } returns sampleFlow
        //when
        val flow: Flow<Resource<List<Quote>>> = objectUnderTest!!.invoke(true)
        //then
        verify(exactly = 1) { quoteRepository.getRandomQuote(true) }
        Truth.assertThat(flow).isSameInstanceAs(sampleFlow)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}