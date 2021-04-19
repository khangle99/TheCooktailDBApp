package com.khangle.thecocktaildbapp.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.khangle.domain.model.Drink
import com.khangle.domain.model.Quote
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.GetRandomDrinkUseCase
import com.khangle.domain.usecase.GetRandomQuoteUseCase
import com.khangle.thecocktaildbapp.util.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flow
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

    @get:Rule
    public val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = HomeViewModel(randomDrinkUseCase, randomQuoteUseCase)
    }

    @Test
    fun test_refresh_force() {
        val drink = Drink()
        val listDrink = listOf(drink)
        val quote = Quote("", "")
        val listQuote = listOf(quote)
//        objectUnderTest!!.randomDrink.observeForever({
//
//        })
//        objectUnderTest!!.randomQuote.observeForever({
//
//        })
        every { randomDrinkUseCase(true) } returns flow { emit(Resource.Success(data = listDrink)) }
        every { randomQuoteUseCase(true) } returns flow { emit(Resource.Success(data = listQuote)) }
        objectUnderTest!!.refresh(true)
        verify {   randomDrinkUseCase(true) }
        verify {  randomQuoteUseCase(true) }
        assertThat(objectUnderTest!!.randomDrink.getOrAwaitValue(5).data).isSameInstanceAs(listDrink)
        assertThat(objectUnderTest!!.randomQuote.getOrAwaitValue(5).data).isSameInstanceAs(listQuote)
    }

    @Test
    fun test_refresh_not_force() {
        val drink = Drink()
        val listDrink = listOf(drink)
        val quote = Quote("", "")
        val listQuote = listOf(quote)
//        objectUnderTest!!.randomDrink.observeForever({
//
//        })
//        objectUnderTest!!.randomQuote.observeForever({
//
//        })
        every { randomDrinkUseCase(false) } returns flow { emit(Resource.Success(data = listDrink)) }
        every { randomQuoteUseCase(false) } returns flow { emit(Resource.Success(data = listQuote)) }
        objectUnderTest!!.refresh(false)
        verify {   randomDrinkUseCase(false) }
        verify {  randomQuoteUseCase(false) }
        assertThat(objectUnderTest!!.randomDrink.getOrAwaitValue(5).data).isSameInstanceAs(listDrink)
        assertThat(objectUnderTest!!.randomQuote.getOrAwaitValue(5).data).isSameInstanceAs(listQuote)
    }

    @After
    fun teardown() {

    }
}