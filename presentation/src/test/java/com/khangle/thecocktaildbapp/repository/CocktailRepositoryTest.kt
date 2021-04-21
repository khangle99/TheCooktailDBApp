package com.khangle.thecocktaildbapp.repository

import androidx.room.withTransaction
import com.google.common.truth.Truth.assertThat
import com.khangle.data.db.CockTailDao
import com.khangle.data.db.CockTailDatabase
import com.khangle.data.repository.TheCocktailDBRepositoryImp
import com.khangle.data.webservice.TheCockTailDBBaseApi
import com.khangle.domain.model.*
import com.khangle.domain.repository.TheCockTailDBRepository
import com.khangle.thecocktaildbapp.util.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class TheCockTailDBRepositoryTest {
    var objectUnderTest: TheCockTailDBRepository? = null

    @MockK
    lateinit var theCockTailDBBaseApi: TheCockTailDBBaseApi

    @MockK
    lateinit var cocktailDatabase: CockTailDatabase

    @MockK
    lateinit var cockTailDao: CockTailDao

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = TheCocktailDBRepositoryImp(theCockTailDBBaseApi, cocktailDatabase)
    }

    @Test
    fun test_fetchDrinkById_returnDrink() = testCoroutineRule.runBlockingTest {
        //given
        val drink = Drink()
        val response = DrinkDetailResponse(drinks = listOf(drink))
        coEvery { theCockTailDBBaseApi.fetchDrinkById("id") } returns response
        //when
        val drinkAns: Drink = objectUnderTest!!.fetchDrinkById("id")
        //then
        coVerify(exactly = 1) { theCockTailDBBaseApi.fetchDrinkById("id") }
        assertThat(drinkAns).isSameInstanceAs(drink)
    }

    @Test
    fun test_query_returnDrinkList() = testCoroutineRule.runBlockingTest {
        //given
        val drink = Drink()
        val response = DrinkDetailResponse(drinks = listOf(drink))
        coEvery { theCockTailDBBaseApi.search("id") } returns response
        //when
        val query: List<Drink> = objectUnderTest!!.query("id")
        //then
        coVerify(exactly = 1) { theCockTailDBBaseApi.search("id") }
        assertThat(query).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_fetchAlcoholicList_returnAlcoholicList() = testCoroutineRule.runBlockingTest {
        //given
        val alcoholic = Alcoholic("")
        val response = AlcoholicListResponse(drinks = listOf(alcoholic))
        coEvery { theCockTailDBBaseApi.fetchAlcoholicList() } returns response
        //when
        val alcoholicList = objectUnderTest!!.fetchAlcoholicList()
        //then
        coVerify(exactly = 1) { theCockTailDBBaseApi.fetchAlcoholicList() }
        assertThat(alcoholicList).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_fetchCategoryList_returnCategoryList() = testCoroutineRule.runBlockingTest {
        //given
        val alcoholic = Category("")
        val response = CategoryListResponse(drinks = listOf(alcoholic))
        coEvery { theCockTailDBBaseApi.fetchCategoryList() } returns response
        //when
        val categoryList = objectUnderTest!!.fetchCategoryList()
        //then
        coVerify(exactly = 1) { theCockTailDBBaseApi.fetchCategoryList() }
        assertThat(categoryList).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_fetchDrinkByCategory_returnFilterDrinkList() = testCoroutineRule.runBlockingTest {
        //given
        val filterResultDrink = FilterResultDrink("", "", "")
        val response = FilterResultResponse(drinks = listOf(filterResultDrink))
        coEvery { theCockTailDBBaseApi.fetchDrinksByCategory("category") } returns response
        //when
        val listResult: List<FilterResultDrink> = objectUnderTest!!.fetchDrinkByCategory("category")
        //then
        coVerify(exactly = 1) { theCockTailDBBaseApi.fetchDrinksByCategory("category") }
        assertThat(listResult).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_fetchDrinkByAlcoholic_returnFilterDrinkList() = testCoroutineRule.runBlockingTest {
        //given
        val filterResultDrink = FilterResultDrink("", "", "")
        val response = FilterResultResponse(drinks = listOf(filterResultDrink))
        coEvery { theCockTailDBBaseApi.fetchDrinksByAlcoholic("alcoholic") } returns response
        //when
        val listResult: List<FilterResultDrink> =
            objectUnderTest!!.fetchDrinkByAlcoholic("alcoholic")
        //then
        coVerify(exactly = 1) { theCockTailDBBaseApi.fetchDrinksByAlcoholic("alcoholic") }
        assertThat(listResult).isSameInstanceAs(response.drinks)
    }

    @Test
    fun test_getRandomDrink_force() = testCoroutineRule.runBlockingTest {
        //given
        val drinkApi = Drink()
        val drinkApiList = listOf(drinkApi)
        val drinkDB = Drink()
        val drinkDBList = listOf(drinkDB)
        coEvery { theCockTailDBBaseApi.fetchRandomDrink() } returns DrinkDetailResponse(
            drinkApiList
        )
        coEvery { cockTailDao.getAll() } returns flow { emit(drinkDBList) }
        coEvery { cockTailDao.deleteAll() } coAnswers {}
        coEvery { cockTailDao.insertAllWithTimestamp(*drinkApiList.toTypedArray()) } coAnswers {}
        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )
        val transactionLambda = slot<suspend () -> Unit>()
        coEvery { cocktailDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
        every { cocktailDatabase.cocktailDao() } returns cockTailDao
        // when
        objectUnderTest!!.getRandomDrink(true).collect { }
        //then
        coVerify(exactly = 1) { cockTailDao.getAll() }
        coVerify(exactly = 1) { cockTailDao.deleteAll() }
        coVerify(exactly = 1) { cockTailDao.insertAllWithTimestamp(*drinkApiList.toTypedArray()) }
        coVerify(exactly = 1) { theCockTailDBBaseApi.fetchRandomDrink() }
        verify(exactly = 1) { cocktailDatabase.cocktailDao() }
        confirmVerified(theCockTailDBBaseApi, cockTailDao, cocktailDatabase)
    }

    @Test
    fun test_getRandomDrink_notForce_Timeout() = testCoroutineRule.runBlockingTest {
        //given
        val drinkAPi = Drink()
        val drinkApiList = listOf(drinkAPi)
        val drinkDB = Drink(
            createdAt = System.currentTimeMillis() - TheCockTailDBRepository.timeout * 2
        )
        val drinkDBList = listOf(drinkDB)
        coEvery { theCockTailDBBaseApi.fetchRandomDrink() } returns DrinkDetailResponse(
            drinkApiList
        )
        coEvery { cockTailDao.getAll() } returns flow { emit(drinkDBList) }
        coEvery { cockTailDao.deleteAll() } coAnswers {}
        coEvery { cockTailDao.insertAllWithTimestamp(*drinkApiList.toTypedArray()) } coAnswers {}
        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )
        val transactionLambda = slot<suspend () -> Unit>()
        coEvery { cocktailDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
        every { cocktailDatabase.cocktailDao() } returns cockTailDao
        // when
        objectUnderTest!!.getRandomDrink(true).collect { }
        //then
        coVerify(exactly = 1) { cockTailDao.getAll() }
        coVerify(exactly = 1) { cockTailDao.deleteAll() }
        coVerify(exactly = 1) { cockTailDao.insertAllWithTimestamp(*drinkApiList.toTypedArray()) }
        coVerify(exactly = 1) { theCockTailDBBaseApi.fetchRandomDrink() }
        verify(exactly = 1) { cocktailDatabase.cocktailDao() }
        confirmVerified(theCockTailDBBaseApi, cockTailDao, cocktailDatabase)
    }

    @Test
    fun test_getRandomDrink_notForce_notTimeout() = testCoroutineRule.runBlockingTest {
        //given
        val drinkDB = Drink(
            createdAt = System.currentTimeMillis()
        )
        val listDrinkDB = listOf(drinkDB)
        coEvery { cockTailDao.getAll() } returns flow { emit(listDrinkDB) }
        every { cocktailDatabase.cocktailDao() } returns cockTailDao
        // when
        objectUnderTest!!.getRandomDrink(false).collect {
            assertThat(it.data).isSameInstanceAs(listDrinkDB)
        }
        //then
        coVerify(exactly = 1) { cockTailDao.getAll() }
        verify(exactly = 1) { cocktailDatabase.cocktailDao() }
        confirmVerified(cockTailDao, cocktailDatabase)
    }

    @Test
    fun test_getRandomDrink_emptyDB() = testCoroutineRule.runBlockingTest {
        //given
        val drinkApi = Drink()
        val drinkApiList = listOf(drinkApi)
        val drinkDBList = emptyList<Drink>()
        coEvery { theCockTailDBBaseApi.fetchRandomDrink() } returns DrinkDetailResponse(
            drinkApiList
        )
        coEvery { cockTailDao.getAll() } returns flow { emit(drinkDBList) }
        coEvery { cockTailDao.deleteAll() } coAnswers {}
        coEvery { cockTailDao.insertAllWithTimestamp(*drinkApiList.toTypedArray()) } coAnswers {}
        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )
        val transactionLambda = slot<suspend () -> Unit>()
        coEvery { cocktailDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
        every { cocktailDatabase.cocktailDao() } returns cockTailDao
        // when
        objectUnderTest!!.getRandomDrink(true).collect { }
        //then
        coVerify(exactly = 1) { cockTailDao.getAll() }
        coVerify(exactly = 1) { cockTailDao.deleteAll() }
        coVerify(exactly = 1) { cockTailDao.insertAllWithTimestamp(*drinkApiList.toTypedArray()) }
        coVerify(exactly = 1) { theCockTailDBBaseApi.fetchRandomDrink() }
        verify(exactly = 1) { cocktailDatabase.cocktailDao() }
        confirmVerified(theCockTailDBBaseApi, cockTailDao, cocktailDatabase)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}