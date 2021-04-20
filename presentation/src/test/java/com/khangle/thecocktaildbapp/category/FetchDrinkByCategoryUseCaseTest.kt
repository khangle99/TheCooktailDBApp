package com.khangle.thecocktaildbapp.category

import com.google.common.truth.Truth
import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.repository.TheCockTailDBRepository
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCase
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCaseImp
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCase
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCaseImp
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class FetchDrinkByCategoryUseCaseTest {
    var objectUnderTest: FetchDrinkByCategoryUseCase? = null

    @MockK
    lateinit var theCockTailDBRepository: TheCockTailDBRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = FetchDrinkByCategoryUseCaseImp(theCockTailDBRepository)
    }

    @Test
    fun test_fetchDrinkByCategory_returnFilterDrinkList() = runBlocking {
        //given
        val drink = FilterResultDrink("","","")
        val drinkList = listOf(drink)
        coEvery { theCockTailDBRepository.fetchDrinkByCategory("category") } returns  drinkList
        //when
        val result: List<FilterResultDrink> = objectUnderTest!!.invoke("category")
        //then
        coVerify {  theCockTailDBRepository.fetchDrinkByCategory("category") }
        Truth.assertThat(result).isSameInstanceAs(drinkList)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}