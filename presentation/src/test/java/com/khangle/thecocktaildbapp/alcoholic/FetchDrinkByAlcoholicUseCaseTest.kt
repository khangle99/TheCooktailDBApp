package com.khangle.thecocktaildbapp.alcoholic

import com.google.common.truth.Truth.assertThat
import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.repository.TheCockTailDBRepository
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCase
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCaseImp
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class FetchDrinkByAlcoholicUseCaseTest {
    var objectUnderTest: FetchDrinkByAlcoholicUseCase? = null
    
    @MockK
    lateinit var theCockTailDBRepository: TheCockTailDBRepository
    
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = FetchDrinkByAlcoholicUseCaseImp(theCockTailDBRepository)
    }
    
    @Test
    fun test_fetchDrinkByAlcoholic_returnFilterDrinkList() = runBlocking {
        //given
        val drink = FilterResultDrink("","","")
        val drinkList = listOf(drink)
        coEvery { theCockTailDBRepository.fetchDrinkByAlcoholic("alcoholic") } returns  drinkList
        //when
        val result: List<FilterResultDrink> = objectUnderTest!!.invoke("alcoholic")
        //then
        coVerify {  theCockTailDBRepository.fetchDrinkByAlcoholic("alcoholic") }
        assertThat(result).isSameInstanceAs(drinkList)
    }
    
    @After
    fun teardown() {
        objectUnderTest = null
    }
}