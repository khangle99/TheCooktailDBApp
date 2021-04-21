package com.khangle.thecocktaildbapp.alcoholic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.khangle.domain.interceptor.NoConnectivityException
import com.khangle.domain.model.Alcoholic
import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.FetchAlcoholicListUseCase
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCase
import com.khangle.thecocktaildbapp.util.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
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
        objectUnderTest = AlcoholicViewModel(
            testCoroutineRule.testCoroutineDispatcher,
            fetchAlcoholicListUseCase,
            fetchDrinkByAlcoholicUseCase
        )
    }

    @Test
    fun test_fetchAlcoholicList_returnAlcoholicList() = testCoroutineRule.runBlockingTest {
        //given
        val alcoholicCapture = mutableListOf<Resource<List<Alcoholic>>>()
        val alcoholic = Alcoholic("")
        val alcoholicList = listOf(alcoholic)
        objectUnderTest!!.alcoholicList.observeForever(alcoholicObserver)
        every { alcoholicObserver.onChanged(capture(alcoholicCapture)) } answers {}
        coEvery { fetchAlcoholicListUseCase() } returns alcoholicList
        // when
        objectUnderTest!!.fetchAlcoholicList()
        //then
        coVerify(exactly = 1) { fetchAlcoholicListUseCase() }
        verify(exactly = 2) { alcoholicObserver.onChanged(any()) }
        assertThat(alcoholicCapture[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(alcoholicCapture[1]).isInstanceOf(Resource.Success::class.java)
        assertThat(alcoholicCapture[1].data).isSameInstanceAs(alcoholicList)
        confirmVerified(fetchAlcoholicListUseCase, alcoholicObserver)
        objectUnderTest!!.alcoholicList.removeObserver(alcoholicObserver)
    }

    @Test
    fun test_fetchAlcoholicList_throwException() = testCoroutineRule.runBlockingTest {
        //given
        val alcoholicCapture = mutableListOf<Resource<List<Alcoholic>>>()
        val exception = NoConnectivityException()
        objectUnderTest!!.alcoholicList.observeForever(alcoholicObserver)
        every { alcoholicObserver.onChanged(capture(alcoholicCapture)) } answers {}
        coEvery { fetchAlcoholicListUseCase() } throws exception
        // when
        objectUnderTest!!.fetchAlcoholicList()
        //then
        coVerify(exactly = 1) { fetchAlcoholicListUseCase() }
        verify(exactly = 2) { alcoholicObserver.onChanged(any()) }
        assertThat(alcoholicCapture[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(alcoholicCapture[1]).isInstanceOf(Resource.Error::class.java)
        assertThat(alcoholicCapture[1].throwable).isInstanceOf(NoConnectivityException::class.java)
        confirmVerified(fetchAlcoholicListUseCase, alcoholicObserver)
        objectUnderTest!!.alcoholicList.removeObserver(alcoholicObserver)
    }

    @Test
    fun test_fetchDrinkByAlcoholic_returnFilterDrinkList() = testCoroutineRule.runBlockingTest {
        //given
        val drinkCapture = mutableListOf<Resource<List<FilterResultDrink>>>()
        val drink = FilterResultDrink("", "", "")
        val drinkList = listOf(drink)
        objectUnderTest!!.drinks.observeForever(drinkObserver)
        every { drinkObserver.onChanged(capture(drinkCapture)) } answers {}
        coEvery { fetchDrinkByAlcoholicUseCase("id") } returns drinkList
        //when
        objectUnderTest!!.fetchDrinkByAlcoholic("id")
        // then
        coVerify(exactly = 1) { fetchDrinkByAlcoholicUseCase("id") }
        verify(exactly = 2) { drinkObserver.onChanged(any()) }
        assertThat(drinkCapture[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(drinkCapture[1]).isInstanceOf(Resource.Success::class.java)
        assertThat(drinkCapture[1].data).isSameInstanceAs(drinkList)
        confirmVerified(fetchDrinkByAlcoholicUseCase, drinkObserver)
        objectUnderTest!!.drinks.removeObserver(drinkObserver)
    }

    @Test
    fun test_fetchDrinkByAlcoholic_throwException() = testCoroutineRule.runBlockingTest {
        //given
        val drinkCapture = mutableListOf<Resource<List<FilterResultDrink>>>()
        val exception = NoConnectivityException()
        objectUnderTest!!.drinks.observeForever(drinkObserver)
        every { drinkObserver.onChanged(capture(drinkCapture)) } answers {}
        coEvery { fetchDrinkByAlcoholicUseCase("id") } throws exception
        //when
        objectUnderTest!!.fetchDrinkByAlcoholic("id")
        // then
        coVerify(exactly = 1) { fetchDrinkByAlcoholicUseCase("id") }
        verify(exactly = 2) { drinkObserver.onChanged(any()) }
        assertThat(drinkCapture[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(drinkCapture[1]).isInstanceOf(Resource.Error::class.java)
        assertThat(drinkCapture[1].throwable).isInstanceOf(NoConnectivityException::class.java)
        confirmVerified(fetchDrinkByAlcoholicUseCase, drinkObserver)
        objectUnderTest!!.drinks.removeObserver(drinkObserver)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}