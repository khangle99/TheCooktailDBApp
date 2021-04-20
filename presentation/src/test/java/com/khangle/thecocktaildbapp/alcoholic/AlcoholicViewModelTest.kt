package com.khangle.thecocktaildbapp.alcoholic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth
import com.khangle.domain.model.Alcoholic
import com.khangle.domain.model.Category
import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.FetchAlcoholicListUseCase
import com.khangle.domain.usecase.FetchCategoryListUseCase
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCase
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCase
import com.khangle.thecocktaildbapp.category.CategoryViewModel
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

class AlcoholicViewModelTest {
    var objectUnderTest: AlcoholicViewModel? = null

    @MockK
    lateinit var fetchAlcoholicListUseCase: FetchAlcoholicListUseCase
    @MockK
    lateinit var fetchDrinkByAlcoholicUseCase: FetchDrinkByAlcoholicUseCase

    @MockK
    lateinit var drinkObserver: Observer<Resource<List<FilterResultDrink>>>

    @MockK
    lateinit var alcoholicObserver: Observer<Resource<List<Alcoholic>>>

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = AlcoholicViewModel(fetchAlcoholicListUseCase, fetchDrinkByAlcoholicUseCase)
    }

    @Test
    fun test_fetchAlcoholicList() = runBlocking {
        testCoroutineRule.runBlockingTest {
            //given
            val alcoholic = Alcoholic("")
            val alcoholicList = listOf(alcoholic)
            objectUnderTest!!.alcoholicList.observeForever(alcoholicObserver)
            every { alcoholicObserver.onChanged(any()) } answers {}
            coEvery { fetchAlcoholicListUseCase() } returns alcoholicList
            // when
            objectUnderTest!!.fetchAlcoholicList()
            //then
            coVerify { fetchAlcoholicListUseCase() }
            Truth.assertThat(objectUnderTest!!.alcoholicList.getOrAwaitValue().data).isSameInstanceAs(alcoholicList)
            objectUnderTest!!.alcoholicList.removeObserver(alcoholicObserver)
        }
    }

    @Test
    fun test_fetchDrinkByAlcoholic() {
        testCoroutineRule.runBlockingTest {
            //given
            val drink = FilterResultDrink("","","")
            val drinkList = listOf(drink)
            objectUnderTest!!.drinks.observeForever(drinkObserver)
            every { drinkObserver.onChanged(any()) } answers {}
            coEvery { fetchDrinkByAlcoholicUseCase("id") } returns drinkList
            //when
            objectUnderTest!!.fetchDrinkByAlcoholic("id")
            // then
            coVerify { fetchDrinkByAlcoholicUseCase("id") }
            Truth.assertThat(objectUnderTest!!.drinks.getOrAwaitValue().data).isSameInstanceAs(drinkList)
            objectUnderTest!!.drinks.removeObserver(drinkObserver)
        }
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}