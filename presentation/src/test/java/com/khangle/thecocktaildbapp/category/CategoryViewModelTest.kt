package com.khangle.thecocktaildbapp.category

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.khangle.domain.interceptor.NoConnectivityException
import com.khangle.domain.model.Category
import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.FetchCategoryListUseCase
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCase
import com.khangle.thecocktaildbapp.util.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
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
        objectUnderTest = CategoryViewModel(
            testCoroutineRule.testCoroutineDispatcher,
            fetchCategoryListUseCase,
            fetchDrinkByCategoryUseCase
        )
    }

    @Test
    fun test_fetchCategoryList_returnCategoryList() = testCoroutineRule.runBlockingTest {
        //given
        val categoryCapture = mutableListOf<Resource<List<Category>>>()
        val category = Category("")
        val categoryList = listOf(category)
        objectUnderTest!!.category.observeForever(categoryObserver)
        every { categoryObserver.onChanged(capture(categoryCapture)) } answers {}
        coEvery { fetchCategoryListUseCase() } returns categoryList
        // when
        objectUnderTest!!.fetchCategoryList()
        //then
        coVerify(exactly = 1) { fetchCategoryListUseCase() }
        verify(exactly = 2) { categoryObserver.onChanged(any()) }
        assertThat(categoryCapture[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(categoryCapture[1]).isInstanceOf(Resource.Success::class.java)
        assertThat(categoryCapture[1].data).isSameInstanceAs(categoryList)
        confirmVerified(fetchCategoryListUseCase, categoryObserver)
        objectUnderTest!!.category.removeObserver(categoryObserver)
    }


    @Test
    fun test_fetchCategoryList_throwException() = testCoroutineRule.runBlockingTest {
        //given
        val categoryCapture = mutableListOf<Resource<List<Category>>>()
        val exception = NoConnectivityException()
        objectUnderTest!!.category.observeForever(categoryObserver)
        every { categoryObserver.onChanged(capture(categoryCapture)) } answers {}
        coEvery { fetchCategoryListUseCase() } throws exception
        // when
        objectUnderTest!!.fetchCategoryList()
        //then
        coVerify(exactly = 1) { fetchCategoryListUseCase() }
        verify(exactly = 2) { categoryObserver.onChanged(any()) }
        assertThat(categoryCapture[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(categoryCapture[1]).isInstanceOf(Resource.Error::class.java)
        assertThat(categoryCapture[1].throwable).isInstanceOf(NoConnectivityException::class.java)
        confirmVerified(fetchCategoryListUseCase, categoryObserver)
        objectUnderTest!!.category.removeObserver(categoryObserver)
    }


    @Test
    fun test_fetchDrinkByCategory_returnFilterDrinkList() = testCoroutineRule.runBlockingTest {
        //given
        val drinkCapture = mutableListOf<Resource<List<FilterResultDrink>>>()
        val drink = FilterResultDrink("", "", "")
        val drinkList = listOf(drink)
        objectUnderTest!!.drinks.observeForever(drinkObserver)
        every { drinkObserver.onChanged(capture(drinkCapture)) } answers {}
        coEvery { fetchDrinkByCategoryUseCase("id") } returns drinkList
        //when
        objectUnderTest!!.fetchDrinkByCategory("id")
        // then
        coVerify(exactly = 1) { fetchDrinkByCategoryUseCase("id") }
        verify(exactly = 2) { drinkObserver.onChanged(any()) }
        assertThat(drinkCapture[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(drinkCapture[1]).isInstanceOf(Resource.Success::class.java)
        assertThat(drinkCapture[1].data).isSameInstanceAs(drinkList)
        confirmVerified(fetchDrinkByCategoryUseCase, drinkObserver)
        objectUnderTest!!.drinks.removeObserver(drinkObserver)
    }


    @Test
    fun test_fetchDrinkByCategory_throwException() = testCoroutineRule.runBlockingTest {
        //given
        val drinkCapture = mutableListOf<Resource<List<FilterResultDrink>>>()
        val exception = NoConnectivityException()
        objectUnderTest!!.drinks.observeForever(drinkObserver)
        every { drinkObserver.onChanged(capture(drinkCapture)) } answers {}
        coEvery { fetchDrinkByCategoryUseCase("id") } throws exception
        //when
        objectUnderTest!!.fetchDrinkByCategory("id")
        // then
        coVerify(exactly = 1) { fetchDrinkByCategoryUseCase("id") }
        verify(exactly = 2) { drinkObserver.onChanged(any()) }
        assertThat(drinkCapture[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(drinkCapture[1]).isInstanceOf(Resource.Error::class.java)
        assertThat(drinkCapture[1].throwable).isInstanceOf(NoConnectivityException::class.java)
        confirmVerified(fetchDrinkByCategoryUseCase, drinkObserver)
        objectUnderTest!!.drinks.removeObserver(drinkObserver)
    }


    @After
    fun teardown() {
        objectUnderTest = null
    }
}