package com.khangle.thecocktaildbapp.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.khangle.domain.model.Drink
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.SearchDrinkByNameUseCase
import com.khangle.thecocktaildbapp.util.TestCoroutineRule
import com.khangle.thecocktaildbapp.util.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {
    var objectUnderTest: SearchViewModel? = null

    @MockK
    lateinit var searchDrinkByNameUseCase: SearchDrinkByNameUseCase

    @MockK
    lateinit var drinkObserver: Observer<Resource<List<Drink>>>

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = SearchViewModel(searchDrinkByNameUseCase)
    }

    @Test
    fun test_query() = runBlocking {
        testCoroutineRule.runBlockingTest {
            //given
            val drink = Drink()
            val drinkList = listOf(drink)
            objectUnderTest!!.drinks.observeForever(drinkObserver)
            every { drinkObserver.onChanged(any()) } answers {}
            coEvery { searchDrinkByNameUseCase("query") } returns drinkList
            //when
            objectUnderTest!!.queryStr("query")
            //then
            coVerify { searchDrinkByNameUseCase("query") }
            assertThat(objectUnderTest!!.drinks.getOrAwaitValue().data).isSameInstanceAs(drinkList)
            objectUnderTest!!.drinks.removeObserver(drinkObserver)
        }
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}