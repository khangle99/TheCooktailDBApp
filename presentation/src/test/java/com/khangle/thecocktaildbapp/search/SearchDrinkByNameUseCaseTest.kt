package com.khangle.thecocktaildbapp.search

import com.google.common.truth.Truth
import com.khangle.domain.model.Drink
import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.repository.TheCockTailDBRepository
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCase
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCaseImp
import com.khangle.domain.usecase.SearchDrinkByNameUseCase
import com.khangle.domain.usecase.SearchDrinkByNameUseCaseImp
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchDrinkByNameUseCaseTest {
    var objectUnderTest: SearchDrinkByNameUseCase? = null

    @MockK
    lateinit var theCockTailDBRepository: TheCockTailDBRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = SearchDrinkByNameUseCaseImp(theCockTailDBRepository)
    }

    @Test
    fun test_invoke() = runBlocking {
        val drink = Drink("","","")
        val drinkList = listOf(drink)
        coEvery { theCockTailDBRepository.query("query") } returns  drinkList
        val result: List<Drink> = objectUnderTest!!.invoke("query")
        coVerify {  theCockTailDBRepository.query("query") }
        Truth.assertThat(result).isSameInstanceAs(drinkList)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}