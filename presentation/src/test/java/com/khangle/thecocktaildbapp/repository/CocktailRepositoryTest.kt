package com.khangle.thecocktaildbapp.repository

import com.google.common.truth.Truth.assertThat
import com.khangle.data.db.CockTailDatabase
import com.khangle.data.repository.TheCocktailDBRepositoryImp
import com.khangle.data.webservice.TheCockTailDBBaseApi
import com.khangle.domain.model.*
import com.khangle.domain.repository.TheCockTailDBRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class CocktailRepositoryTest {
     var objectUnderTest: TheCockTailDBRepository? = null
    @MockK
    lateinit var theCockTailDBBaseApi: TheCockTailDBBaseApi

    @MockK
    lateinit var cocktailDatabase: CockTailDatabase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = TheCocktailDBRepositoryImp(theCockTailDBBaseApi,cocktailDatabase)
    }
    @Test
    fun test_fetchDrinkById_returnDrink() = runBlocking {
        val drink = Drink()
        val response = DrinkDetailResponse(drinks = listOf(drink))
        coEvery { theCockTailDBBaseApi.fetchDrinkById("id") } returns response
        val drinkAns: Drink = objectUnderTest!!.fetchDrinkById("id")
        coVerify { theCockTailDBBaseApi.fetchDrinkById("id") }
        assertThat(drinkAns).isSameInstanceAs(drink)
    }

    @Test
    fun test_query_returnListDrink() = runBlocking {
        val drink = Drink()
        val response = DrinkDetailResponse(drinks = listOf(drink))
        coEvery { theCockTailDBBaseApi.search("id") } returns response
        val query: List<Drink> = objectUnderTest!!.query("id")
        coVerify { theCockTailDBBaseApi.search("id") }
        assertThat(query).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_fetchAlcoholicList_returnAlcoholicList() = runBlocking {
        val alcoholic = Alcoholic("")
        val response = AlcoholicListResponse(drinks = listOf(alcoholic))
        coEvery { theCockTailDBBaseApi.fetchAlcoholicList() } returns response
        val alcoholicList = objectUnderTest!!.fetchAlcoholicList()
        coVerify { theCockTailDBBaseApi.fetchAlcoholicList() }
        assertThat(alcoholicList).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_fetchCategoryList_returnCategoryList() = runBlocking {
        val alcoholic = Category("")
        val response = CategoryListResponse(drinks = listOf(alcoholic))
        coEvery { theCockTailDBBaseApi.fetchCategoryList() } returns response
        val categoryList = objectUnderTest!!.fetchCategoryList()
        coVerify { theCockTailDBBaseApi.fetchCategoryList() }
        assertThat(categoryList).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_fetchDrinkByCategory_returnFilterDrinkList() = runBlocking{
        val filterResultDrink = FilterResultDrink("","","")
        val response = FilterResultResponse(drinks = listOf(filterResultDrink))
        coEvery { theCockTailDBBaseApi.fetchDrinksByCategory("category") } returns  response
        val listResult: List<FilterResultDrink> = objectUnderTest!!.fetchDrinkByCategory("category")
        coVerify { theCockTailDBBaseApi.fetchDrinksByCategory("category") }
        assertThat(listResult).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_fetchDrinkByAlcoholic_returnFilterDrinkList() = runBlocking {
        val filterResultDrink = FilterResultDrink("","","")
        val response = FilterResultResponse(drinks = listOf(filterResultDrink))
        coEvery { theCockTailDBBaseApi.fetchDrinksByAlcoholic("alcoholic") } returns  response
        val listResult: List<FilterResultDrink> = objectUnderTest!!.fetchDrinkByAlcoholic("alcoholic")
        coVerify { theCockTailDBBaseApi.fetchDrinksByAlcoholic("alcoholic") }
        assertThat(listResult).isSameInstanceAs(response.drinks)
    }


    @After
    fun teardown() {
        objectUnderTest = null
    }
}