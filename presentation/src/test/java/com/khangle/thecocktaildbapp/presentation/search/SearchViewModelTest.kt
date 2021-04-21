package com.khangle.thecocktaildbapp.presentation.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.khangle.thecocktaildbapp.domain.interceptor.NoConnectivityException
import com.khangle.thecocktaildbapp.domain.model.Drink
import com.khangle.thecocktaildbapp.domain.model.Resource
import com.khangle.thecocktaildbapp.domain.usecase.SearchDrinkByNameUseCase
import com.khangle.thecocktaildbapp.presentation.util.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
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
        objectUnderTest =
            SearchViewModel(testCoroutineRule.testCoroutineDispatcher, searchDrinkByNameUseCase)
    }

    @Test
    fun test_query_returnDrinkList() = testCoroutineRule.runBlockingTest {
        //given
        val list = mutableListOf<Resource<List<Drink>>>()
        val drink = Drink()
        val drinkList = listOf(drink)
        objectUnderTest!!.drinks.observeForever(drinkObserver)
        every { drinkObserver.onChanged(capture(list)) } answers {}
        coEvery { searchDrinkByNameUseCase("query") } returns drinkList
        //when
        objectUnderTest!!.queryStr("query")
        //then
        coVerify(exactly = 1) { searchDrinkByNameUseCase("query") }
        verify(exactly = 2) { drinkObserver.onChanged(any()) }
        assertThat(list[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(list[1]).isInstanceOf(Resource.Success::class.java)
        assertThat(list[1].data).isSameInstanceAs(drinkList)
        confirmVerified(searchDrinkByNameUseCase, drinkObserver)
        objectUnderTest!!.drinks.removeObserver(drinkObserver)
    }


    @Test
    fun test_query_throwException() = testCoroutineRule.runBlockingTest {
        //given
        val list = mutableListOf<Resource<List<Drink>>>()
        val exception = NoConnectivityException()
        objectUnderTest!!.drinks.observeForever(drinkObserver)
        every { drinkObserver.onChanged(capture(list)) } answers {}
        coEvery { searchDrinkByNameUseCase("query") } throws exception
        //when
        objectUnderTest!!.queryStr("query")
        //then
        coVerify(exactly = 1) { searchDrinkByNameUseCase("query") }
        verify(exactly = 2) { drinkObserver.onChanged(any()) }
        assertThat(list[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(list[1]).isInstanceOf(Resource.Error::class.java)
        assertThat(list[1].throwable).isInstanceOf(NoConnectivityException::class.java)
        confirmVerified(searchDrinkByNameUseCase, drinkObserver)
        objectUnderTest!!.drinks.removeObserver(drinkObserver)
    }


    @After
    fun teardown() {
        objectUnderTest = null
    }
}