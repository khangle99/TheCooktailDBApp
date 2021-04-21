package com.khangle.thecocktaildbapp.domain.search

import com.google.common.truth.Truth
import com.khangle.thecocktaildbapp.domain.model.Drink
import com.khangle.thecocktaildbapp.domain.repository.TheCockTailDBRepository
import com.khangle.thecocktaildbapp.domain.usecase.SearchDrinkByNameUseCase
import com.khangle.thecocktaildbapp.domain.usecase.SearchDrinkByNameUseCaseImp
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
class SearchDrinkByNameUseCaseTest {
    var objectUnderTest: SearchDrinkByNameUseCase? = null

    @MockK
    lateinit var theCockTailDBRepository: TheCockTailDBRepository

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = SearchDrinkByNameUseCaseImp(theCockTailDBRepository)
    }

    @Test
    fun test_searchDrinkByName_returnDrinkList() = testCoroutineRule.runBlockingTest {
        //given
        val drink = Drink("", "", "")
        val drinkList = listOf(drink)
        coEvery { theCockTailDBRepository.query("query") } returns drinkList
        //when
        val result: List<Drink> = objectUnderTest!!.invoke("query")
        //then
        coVerify(exactly = 1) { theCockTailDBRepository.query("query") }
        Truth.assertThat(result).isSameInstanceAs(drinkList)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}