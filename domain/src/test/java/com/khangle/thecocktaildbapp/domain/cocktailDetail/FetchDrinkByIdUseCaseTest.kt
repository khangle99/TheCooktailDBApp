package com.khangle.thecocktaildbapp.domain.cocktailDetail

import com.google.common.truth.Truth.assertThat
import com.khangle.thecocktaildbapp.domain.model.Drink
import com.khangle.thecocktaildbapp.domain.repository.TheCockTailDBRepository
import com.khangle.thecocktaildbapp.domain.usecase.FetchDrinkByIdUseCase
import com.khangle.thecocktaildbapp.domain.usecase.FetchDrinkByIdUseCaseImp
import com.khangle.thecocktaildbapp.domain.util.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FetchDrinkByIdUseCaseTest {
    private var objectUnderTest: FetchDrinkByIdUseCase? = null
    @MockK
    lateinit var theCockTailDBRepository: TheCockTailDBRepository

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = FetchDrinkByIdUseCaseImp(theCockTailDBRepository)
    }
    @Test
    fun test_fetchDrinkByIdUseCase_returnDrink() = testCoroutineRule.runBlockingTest {
        //given
        val drink = Drink()
        coEvery { theCockTailDBRepository.fetchDrinkById("id") } returns drink
        //when
        val result = objectUnderTest!!.invoke("id")
        //then
        coVerify(exactly = 1) { theCockTailDBRepository.fetchDrinkById("id") }
        assertThat(result).isSameInstanceAs(drink)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}