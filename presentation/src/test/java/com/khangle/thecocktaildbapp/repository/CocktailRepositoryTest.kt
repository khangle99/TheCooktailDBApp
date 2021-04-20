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
        //given
        val drink = Drink()
        val response = DrinkDetailResponse(drinks = listOf(drink))
        coEvery { theCockTailDBBaseApi.fetchDrinkById("id") } returns response
        //when
        val drinkAns: Drink = objectUnderTest!!.fetchDrinkById("id")
        //then
        coVerify { theCockTailDBBaseApi.fetchDrinkById("id") }
        assertThat(drinkAns).isSameInstanceAs(drink)
    }

    @Test
    fun test_query_returnListDrink() = runBlocking {
        //given
        val drink = Drink()
        val response = DrinkDetailResponse(drinks = listOf(drink))
        coEvery { theCockTailDBBaseApi.search("id") } returns response
        //when
        val query: List<Drink> = objectUnderTest!!.query("id")
        //then
        coVerify { theCockTailDBBaseApi.search("id") }
        assertThat(query).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_fetchAlcoholicList_returnAlcoholicList() = runBlocking {
        //given
        val alcoholic = Alcoholic("")
        val response = AlcoholicListResponse(drinks = listOf(alcoholic))
        coEvery { theCockTailDBBaseApi.fetchAlcoholicList() } returns response
        //when
        val alcoholicList = objectUnderTest!!.fetchAlcoholicList()
        //then
        coVerify { theCockTailDBBaseApi.fetchAlcoholicList() }
        assertThat(alcoholicList).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_fetchCategoryList_returnCategoryList() = runBlocking {
        //given
        val alcoholic = Category("")
        val response = CategoryListResponse(drinks = listOf(alcoholic))
        coEvery { theCockTailDBBaseApi.fetchCategoryList() } returns response
        //when
        val categoryList = objectUnderTest!!.fetchCategoryList()
        //then
        coVerify { theCockTailDBBaseApi.fetchCategoryList() }
        assertThat(categoryList).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_fetchDrinkByCategory_returnFilterDrinkList() = runBlocking{
        //given
        val filterResultDrink = FilterResultDrink("","","")
        val response = FilterResultResponse(drinks = listOf(filterResultDrink))
        coEvery { theCockTailDBBaseApi.fetchDrinksByCategory("category") } returns  response
        //when
        val listResult: List<FilterResultDrink> = objectUnderTest!!.fetchDrinkByCategory("category")
        //then
        coVerify { theCockTailDBBaseApi.fetchDrinksByCategory("category") }
        assertThat(listResult).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_fetchDrinkByAlcoholic_returnFilterDrinkList() = runBlocking {
        //given
        val filterResultDrink = FilterResultDrink("","","")
        val response = FilterResultResponse(drinks = listOf(filterResultDrink))
        coEvery { theCockTailDBBaseApi.fetchDrinksByAlcoholic("alcoholic") } returns  response
        //when
        val listResult: List<FilterResultDrink> = objectUnderTest!!.fetchDrinkByAlcoholic("alcoholic")
        //then
        coVerify { theCockTailDBBaseApi.fetchDrinksByAlcoholic("alcoholic") }
        assertThat(listResult).isSameInstanceAs(response.drinks)
    }


    @After
    fun teardown() {
        objectUnderTest = null
    }
}