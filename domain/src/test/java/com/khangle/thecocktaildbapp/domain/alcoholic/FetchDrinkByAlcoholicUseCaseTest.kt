package com.khangle.thecocktaildbapp.domain.alcoholic

import com.google.common.truth.Truth.assertThat
import com.khangle.thecocktaildbapp.domain.model.FilterResultDrink
import com.khangle.thecocktaildbapp.domain.repository.TheCockTailDBRepository
import com.khangle.thecocktaildbapp.domain.usecase.FetchDrinkByAlcoholicUseCase
import com.khangle.thecocktaildbapp.domain.usecase.FetchDrinkByAlcoholicUseCaseImp
import com.khangle.thecocktaildbapp.domain.util.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FetchDrinkByAlcoholicUseCaseTest {
    var objectUnderTest: FetchDrinkByAlcoholicUseCase? = null

    @MockK
    lateinit var theCockTailDBRepository: TheCockTailDBRepository

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = FetchDrinkByAlcoholicUseCaseImp(theCockTailDBRepository)
    }

    @Test
    fun test_fetchDrinkByAlcoholic_returnFilterDrinkList() = testCoroutineRule.runBlockingTest {
        //given
        val drink = FilterResultDrink("", "", "")
        val drinkList = listOf(drink)
        coEvery { theCockTailDBRepository.fetchDrinkByAlcoholic("alcoholic") } returns drinkList
        //when
        val result: List<FilterResultDrink> = objectUnderTest!!.invoke("alcoholic")
        //then
        coVerify(exactly = 1) { theCockTailDBRepository.fetchDrinkByAlcoholic("alcoholic") }
        assertThat(result).isSameInstanceAs(drinkList)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}