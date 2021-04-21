package com.khangle.thecocktaildbapp.domain.home

import com.google.common.truth.Truth
import com.khangle.thecocktaildbapp.domain.model.Drink
import com.khangle.thecocktaildbapp.domain.model.Resource
import com.khangle.thecocktaildbapp.domain.repository.TheCockTailDBRepository
import com.khangle.thecocktaildbapp.domain.usecase.GetRandomDrinkUseCase
import com.khangle.thecocktaildbapp.domain.usecase.GetRandomDrinkUseCaseImp
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
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
    fun test_getRandomDrink_returnDrinkFlow() {
        //given
        val sampleFlow = flow<Resource<List<Drink>>> {  emit(Resource.Success(data = emptyList())) }
        every { cocktailRepository.getRandomDrink(true) } returns sampleFlow
        //when
        val flow: Flow<Resource<List<Drink>>> = objectUnderTest!!.invoke(true)
        //then
        verify(exactly = 1) { cocktailRepository.getRandomDrink(true) }
        Truth.assertThat(flow).isSameInstanceAs(sampleFlow)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}