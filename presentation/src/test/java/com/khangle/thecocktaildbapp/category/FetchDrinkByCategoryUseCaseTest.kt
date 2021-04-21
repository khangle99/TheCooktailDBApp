package com.khangle.thecocktaildbapp.category

import com.google.common.truth.Truth
import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.repository.TheCockTailDBRepository
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCase
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCaseImp
import com.khangle.thecocktaildbapp.util.TestCoroutineRule
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
class FetchDrinkByCategoryUseCaseTest {
    var objectUnderTest: FetchDrinkByCategoryUseCase? = null

    @MockK
    lateinit var theCockTailDBRepository: TheCockTailDBRepository

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = FetchDrinkByCategoryUseCaseImp(theCockTailDBRepository)
    }

    @Test
    fun test_fetchDrinkByCategory_returnFilterDrinkList() = testCoroutineRule.runBlockingTest {
        //given
        val drink = FilterResultDrink("", "", "")
        val drinkList = listOf(drink)
        coEvery { theCockTailDBRepository.fetchDrinkByCategory("category") } returns drinkList
        //when
        val result: List<FilterResultDrink> = objectUnderTest!!.invoke("category")
        //then
        coVerify(exactly = 1) { theCockTailDBRepository.fetchDrinkByCategory("category") }
        Truth.assertThat(result).isSameInstanceAs(drinkList)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}