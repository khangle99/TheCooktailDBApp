package com.khangle.thecocktaildbapp.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.khangle.domain.model.Drink
import com.khangle.domain.model.Quote
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.GetRandomDrinkUseCase
import com.khangle.domain.usecase.GetRandomQuoteUseCase
import com.khangle.thecocktaildbapp.util.TestCoroutineRule
import com.khangle.thecocktaildbapp.util.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    var objectUnderTest: HomeViewModel? = null

    @MockK
    lateinit var randomDrinkUseCase: GetRandomDrinkUseCase
    @MockK
    lateinit var randomQuoteUseCase: GetRandomQuoteUseCase
    @MockK
    lateinit var drinkObserver: Observer<Resource<List<Drink>>>
    @MockK
    lateinit var quoteObserver: Observer<Resource<List<Quote>>>

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = HomeViewModel(randomDrinkUseCase, randomQuoteUseCase)
    }

    @Test
    fun test_refresh_force() = runBlocking {
        testCoroutineRule.runBlockingTest {
            //given
            objectUnderTest!!.randomDrink.observeForever(drinkObserver)
            objectUnderTest!!.randomQuote.observeForever(quoteObserver)
            val drink = Drink()
            val listDrink = listOf(drink)
            val quote = Quote("", "")
            val listQuote = listOf(quote)
            every { drinkObserver.onChanged(any()) } answers {}
            every { quoteObserver.onChanged(any()) } answers {}
            val drinkSuccess = Resource.Success(data = listDrink)
            val quoteSuccess = Resource.Success(data = listQuote)
            every { randomDrinkUseCase(true) } returns flow { emit(drinkSuccess) }
            every { randomQuoteUseCase(true) } returns flow { emit(quoteSuccess) }
            // when
            objectUnderTest!!.refresh(true)
            //then
            verify { randomDrinkUseCase(true) }
            verify { randomQuoteUseCase(true) }
            assertThat(objectUnderTest!!.randomDrink.getOrAwaitValue(5)).isSameInstanceAs(drinkSuccess)
            assertThat(objectUnderTest!!.randomQuote.getOrAwaitValue(5)).isSameInstanceAs(quoteSuccess)
            objectUnderTest!!.randomDrink.removeObserver(drinkObserver)
            objectUnderTest!!.randomQuote.removeObserver(quoteObserver)
        }
    }

    @Test
    fun test_refresh_not_force() = runBlocking {
        testCoroutineRule.runBlockingTest {
            //given
            objectUnderTest!!.randomDrink.observeForever(drinkObserver)
            objectUnderTest!!.randomQuote.observeForever(quoteObserver)
            val drink = Drink()
            val listDrink = listOf(drink)
            val quote = Quote("", "")
            val listQuote = listOf(quote)
            every { drinkObserver.onChanged(any()) } answers {}
            every { quoteObserver.onChanged(any()) } answers {}
            val drinkSuccess = Resource.Success(data = listDrink)
            val quoteSuccess = Resource.Success(data = listQuote)
            every { randomDrinkUseCase(false) } returns flow { emit(drinkSuccess) }
            every { randomQuoteUseCase(false) } returns flow { emit(quoteSuccess) }
            // when
            objectUnderTest!!.refresh(false)
            //then
            verify { randomDrinkUseCase(false) }
            verify { randomQuoteUseCase(false) }
            assertThat(objectUnderTest!!.randomDrink.getOrAwaitValue(5)).isSameInstanceAs(drinkSuccess)
            assertThat(objectUnderTest!!.randomQuote.getOrAwaitValue(5)).isSameInstanceAs(quoteSuccess)
            objectUnderTest!!.randomDrink.removeObserver(drinkObserver)
            objectUnderTest!!.randomQuote.removeObserver(quoteObserver)
        }
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}