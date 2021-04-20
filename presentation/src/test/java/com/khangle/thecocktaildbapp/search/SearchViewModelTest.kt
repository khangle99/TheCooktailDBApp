package com.khangle.thecocktaildbapp.search

import com.google.common.truth.Truth
import com.khangle.domain.model.Drink
import com.khangle.domain.repository.TheCockTailDBRepository
import com.khangle.domain.usecase.SearchDrinkByNameUseCase
import com.khangle.domain.usecase.SearchDrinkByNameUseCaseImp
import com.khangle.thecocktaildbapp.util.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {
    var objectUnderTest: SearchViewModel? = null

    @MockK
    lateinit var searchDrinkByNameUseCase: SearchDrinkByNameUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = SearchViewModel(searchDrinkByNameUseCase)
    }

    @Test
    fun test_invoke() = runBlocking {
        val drink = Drink("","","")
        val drinkList = listOf(drink)
        coEvery { searchDrinkByNameUseCase.invoke("query") } returns  drinkList
       objectUnderTest!!.queryStr("query")
        coVerify {  searchDrinkByNameUseCase.invoke("query") }
        Truth.assertThat(objectUnderTest!!.drinks.getOrAwaitValue(5).data).isSameInstanceAs(drinkList)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}