package com.khangle.thecocktaildbapp.home

import com.google.common.truth.Truth
import com.khangle.domain.model.Drink
import com.khangle.domain.model.Quote
import com.khangle.domain.model.Resource
import com.khangle.domain.repository.GoQuoteRepository
import com.khangle.domain.repository.TheCockTailDBRepository
import com.khangle.domain.usecase.GetRandomDrinkUseCase
import com.khangle.domain.usecase.GetRandomDrinkUseCaseImp
import com.khangle.domain.usecase.GetRandomQuoteUseCase
import com.khangle.domain.usecase.GetRandomQuoteUseCaseImp
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetRandomDrinkUseCaseTest {
    var objectUnderTest: GetRandomDrinkUseCase? = null
    @MockK
    lateinit var cocktailRepository: TheCockTailDBRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = GetRandomDrinkUseCaseImp(cocktailRepository)

    }
    @Test
    fun test_getRandomQuote_returnFlow() {
        //given
        val sampleFlow = flow<Resource<List<Drink>>> {  emit(Resource.Success(data = emptyList())) }
        every { cocktailRepository.getRandomDrink(true) } returns sampleFlow
        //when
        val flow: Flow<Resource<List<Drink>>> = objectUnderTest!!.invoke(true)
        //then
        verify { cocktailRepository.getRandomDrink(true) }
        Truth.assertThat(flow).isSameInstanceAs(sampleFlow)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}