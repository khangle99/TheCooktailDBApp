package com.khangle.thecocktaildbapp.category

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth
import com.khangle.domain.model.Category
import com.khangle.domain.model.Drink
import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.FetchCategoryListUseCase
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCase
import com.khangle.domain.usecase.SearchDrinkByNameUseCase
import com.khangle.thecocktaildbapp.search.SearchViewModel
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

class CategoryViewModelTest {
    var objectUnderTest: CategoryViewModel? = null

    @MockK
    lateinit var fetchCategoryListUseCase: FetchCategoryListUseCase
    @MockK
    lateinit var fetchDrinkByCategoryUseCase: FetchDrinkByCategoryUseCase

    @MockK
    lateinit var drinkObserver: Observer<Resource<List<FilterResultDrink>>>

    @MockK
    lateinit var categoryObserver: Observer<Resource<List<Category>>>

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = CategoryViewModel(fetchCategoryListUseCase, fetchDrinkByCategoryUseCase)
    }

    @Test
    fun test_fetchCategoryList() = runBlocking {
        testCoroutineRule.runBlockingTest {
            //given
           val category = Category("")
            val categoryList = listOf(category)
            objectUnderTest!!.category.observeForever(categoryObserver)
            every { categoryObserver.onChanged(any()) } answers {}
            coEvery { fetchCategoryListUseCase() } returns categoryList
            // when
            objectUnderTest!!.fetchCategoryList()
            //then
            coVerify { fetchCategoryListUseCase() }
            Truth.assertThat(objectUnderTest!!.category.getOrAwaitValue().data).isSameInstanceAs(categoryList)
            objectUnderTest!!.category.removeObserver(categoryObserver)
        }
    }

    @Test
    fun test_fetchDrinkByCategory() {
        testCoroutineRule.runBlockingTest {
            //given
            val drink = FilterResultDrink("","","")
            val drinkList = listOf(drink)
            objectUnderTest!!.drinks.observeForever(drinkObserver)
            every { drinkObserver.onChanged(any()) } answers {}
            coEvery { fetchDrinkByCategoryUseCase("id") } returns drinkList
            //when
            objectUnderTest!!.fetchDrinkByCategory("id")
            // then
            coVerify { fetchDrinkByCategoryUseCase("id") }
            Truth.assertThat(objectUnderTest!!.drinks.getOrAwaitValue().data).isSameInstanceAs(drinkList)
            objectUnderTest!!.drinks.removeObserver(drinkObserver)
        }
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}