package com.khangle.thecocktaildbapp.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.khangle.thecocktaildbapp.domain.model.Drink
import com.khangle.thecocktaildbapp.domain.model.Quote
import com.khangle.thecocktaildbapp.domain.model.Resource
import com.khangle.thecocktaildbapp.domain.usecase.GetRandomDrinkUseCase
import com.khangle.thecocktaildbapp.domain.usecase.GetRandomQuoteUseCase
import com.khangle.thecocktaildbapp.presentation.util.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
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
        objectUnderTest = HomeViewModel(
            testCoroutineRule.testCoroutineDispatcher,
            randomDrinkUseCase,
            randomQuoteUseCase
        )
    }

    @Test
    fun test_refresh_force() = testCoroutineRule.runBlockingTest {
        //given
        val drinkCapture = mutableListOf<Resource<List<Drink>>>()
        val quoteCapture = mutableListOf<Resource<List<Quote>>>()
        objectUnderTest!!.randomDrink.observeForever(drinkObserver)
        objectUnderTest!!.randomQuote.observeForever(quoteObserver)
        val drink = Drink()
        val drinkList = listOf(drink)
        val quote = Quote("", "")
        val quoteList = listOf(quote)
        every { drinkObserver.onChanged(capture(drinkCapture)) } answers {}
        every { quoteObserver.onChanged(capture(quoteCapture)) } answers {}
        val drinkSuccess = Resource.Success(data = drinkList)
        val quoteSuccess = Resource.Success(data = quoteList)
        every { randomDrinkUseCase(true) } returns flow { emit(drinkSuccess) }
        every { randomQuoteUseCase(true) } returns flow { emit(quoteSuccess) }
        // when
        objectUnderTest!!.refresh(true)
        //then
        verify(exactly = 1) { randomDrinkUseCase(true) }
        verify(exactly = 1) { randomQuoteUseCase(true) }
        verify(exactly = 1) { quoteObserver.onChanged(any()) }
        verify(exactly = 1) { drinkObserver.onChanged(any()) }
        assertThat(drinkCapture[0]).isInstanceOf(Resource.Success::class.java)
        assertThat(drinkCapture[0].data).isSameInstanceAs(drinkList)
        assertThat(quoteCapture[0]).isInstanceOf(Resource.Success::class.java)
        assertThat(quoteCapture[0].data).isSameInstanceAs(quoteList)
        confirmVerified(drinkObserver, quoteObserver, randomDrinkUseCase, randomQuoteUseCase)
        objectUnderTest!!.randomDrink.removeObserver(drinkObserver)
        objectUnderTest!!.randomQuote.removeObserver(quoteObserver)
    }

    @Test
    fun test_refresh_not_force() = testCoroutineRule.runBlockingTest {
        //given
        val drinkCapture = mutableListOf<Resource<List<Drink>>>()
        val quoteCapture = mutableListOf<Resource<List<Quote>>>()
        objectUnderTest!!.randomDrink.observeForever(drinkObserver)
        objectUnderTest!!.randomQuote.observeForever(quoteObserver)
        val drink = Drink()
        val drinkList = listOf(drink)
        val quote = Quote("", "")
        val quoteList = listOf(quote)
        every { drinkObserver.onChanged(capture(drinkCapture)) } answers {}
        every { quoteObserver.onChanged(capture(quoteCapture)) } answers {}
        val drinkSuccess = Resource.Success(data = drinkList)
        val quoteSuccess = Resource.Success(data = quoteList)
        every { randomDrinkUseCase(false) } returns flow { emit(drinkSuccess) }
        every { randomQuoteUseCase(false) } returns flow { emit(quoteSuccess) }
        // when
        objectUnderTest!!.refresh(false)
        //then
        verify(exactly = 1) { randomDrinkUseCase(false) }
        verify(exactly = 1) { randomQuoteUseCase(false) }
        verify(exactly = 1) { quoteObserver.onChanged(any()) }
        verify(exactly = 1) { drinkObserver.onChanged(any()) }
        assertThat(drinkCapture[0]).isInstanceOf(Resource.Success::class.java)
        assertThat(drinkCapture[0].data).isSameInstanceAs(drinkList)
        assertThat(quoteCapture[0]).isInstanceOf(Resource.Success::class.java)
        assertThat(quoteCapture[0].data).isSameInstanceAs(quoteList)
        confirmVerified(drinkObserver, quoteObserver, randomDrinkUseCase, randomQuoteUseCase)
        objectUnderTest!!.randomDrink.removeObserver(drinkObserver)
        objectUnderTest!!.randomQuote.removeObserver(quoteObserver)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}