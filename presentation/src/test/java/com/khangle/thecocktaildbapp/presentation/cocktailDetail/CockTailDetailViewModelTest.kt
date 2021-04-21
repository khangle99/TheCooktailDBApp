package com.khangle.thecocktaildbapp.presentation.cocktailDetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.khangle.thecocktaildbapp.domain.interceptor.NoConnectivityException
import com.khangle.thecocktaildbapp.domain.model.Drink
import com.khangle.thecocktaildbapp.domain.model.Resource
import com.khangle.thecocktaildbapp.domain.usecase.FetchDrinkByIdUseCase
import com.khangle.thecocktaildbapp.presentation.util.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CockTailDetailViewModelTest {
    private var objectUnderTest: CockTailDetailViewModel? = null
    @MockK
    lateinit var drinkByIdUseCase: FetchDrinkByIdUseCase

    @MockK
    lateinit var drinkObserver: Observer<Resource<Drink>>

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = CockTailDetailViewModel(testCoroutineRule.testCoroutineDispatcher,drinkByIdUseCase)
    }

    @Test
    fun test_setDrink() {
        //given
        val drink = Drink()
        val slot = slot<Resource<Drink>>()
        objectUnderTest!!.drink.observeForever(drinkObserver)
        every { drinkObserver.onChanged(capture(slot)) } answers {}
        // when
        objectUnderTest!!.setDrink(drink)
        // then
        verify(exactly = 1) { drinkObserver.onChanged(any()) }
        assertThat(slot.captured).isInstanceOf(Resource.Success::class.java)
        assertThat(slot.captured.data).isSameInstanceAs(drink)
        objectUnderTest!!.drink.removeObserver(drinkObserver)
    }

    @Test
    fun test_fetchDrink_returnDrink() {
        //given
        val drink = Drink()
        val list = mutableListOf<Resource<Drink>>()
        objectUnderTest!!.drink.observeForever(drinkObserver)
        every { drinkObserver.onChanged(capture(list)) } answers {}
        coEvery { drinkByIdUseCase("id") } returns drink
        //when
        objectUnderTest!!.fetchDrink("id")
        //then
        coVerify(exactly = 1) { drinkByIdUseCase("id") }
        verify(exactly = 2) { drinkObserver.onChanged(any()) }
        assertThat(list[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(list[1]).isInstanceOf(Resource.Success::class.java)
        assertThat(list[1].data).isSameInstanceAs(drink)
        objectUnderTest!!.drink.removeObserver(drinkObserver)
    }

    @Test
    fun test_fetchDrink_throwException() {
        //given
        val exception = NoConnectivityException()
        val list = mutableListOf<Resource<Drink>>()
        objectUnderTest!!.drink.observeForever(drinkObserver)
        every { drinkObserver.onChanged(capture(list)) } answers {}
        coEvery { drinkByIdUseCase("id") } throws exception
        //when
        objectUnderTest!!.fetchDrink("id")
        //then
        verify(exactly = 2) { drinkObserver.onChanged(any()) }
        assertThat(list[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(list[1]).isInstanceOf(Resource.Error::class.java)
        assertThat(list[1].throwable).isSameInstanceAs(exception)
        objectUnderTest!!.drink.removeObserver(drinkObserver)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}