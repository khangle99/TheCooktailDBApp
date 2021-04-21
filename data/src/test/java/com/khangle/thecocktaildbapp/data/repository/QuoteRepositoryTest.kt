package com.khangle.thecocktaildbapp.data.repository

import androidx.room.withTransaction
import com.google.common.truth.Truth.assertThat
import com.khangle.thecocktaildbapp.data.db.QuoteDao
import com.khangle.thecocktaildbapp.data.db.QuoteDatabase
import com.khangle.thecocktaildbapp.data.util.TestCoroutineRule
import com.khangle.thecocktaildbapp.data.webservice.GoQuoteBaseApi
import com.khangle.thecocktaildbapp.domain.model.Quote
import com.khangle.thecocktaildbapp.domain.model.QuoteResponse
import com.khangle.thecocktaildbapp.domain.repository.GoQuoteRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GoQuoteRepositoryTest {

    private var objectUnderTest: GoQuoteRepository? = null

    @MockK
    lateinit var quoteDatabase: QuoteDatabase

    @MockK
    lateinit var goQuoteBaseApi: GoQuoteBaseApi

    @MockK
    lateinit var quoteDao: QuoteDao

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = GoQuoteRepositoryImp(goQuoteBaseApi, quoteDatabase)
    }

    @Test
    fun test_getRandomQuote_force() = testCoroutineRule.runBlockingTest {
        //given
        val quoteAPI = Quote(author = "", text = "")
        val listQuoteAPI = listOf(quoteAPI)
        val quoteDB = Quote(author = "", text = "")
        val listQuoteDB = listOf(quoteDB)
        coEvery { goQuoteBaseApi.fetchRandomQuote() } returns QuoteResponse(quotes = listQuoteAPI)
        coEvery { quoteDao.getAll() } returns flow { emit(listQuoteDB) }
        coEvery { quoteDao.deleteAll() } coAnswers {}
        coEvery { quoteDao.insertAllWithTimestamp(*listQuoteAPI.toTypedArray()) } coAnswers {}
        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )
        val transactionLambda = slot<suspend () -> Unit>()
        coEvery { quoteDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
        every { quoteDatabase.quoteDao() } returns quoteDao
        // when
        objectUnderTest!!.getRandomQuote(true).collect {}
        //then
        coVerify(exactly = 1) { quoteDao.getAll() }
        coVerify(exactly = 1) { quoteDao.deleteAll() }
        coVerify(exactly = 1) { quoteDao.insertAllWithTimestamp(*listQuoteAPI.toTypedArray()) }
        coVerify(exactly = 1) { goQuoteBaseApi.fetchRandomQuote() }
        verify(exactly = 1) { quoteDatabase.quoteDao() }
        confirmVerified(goQuoteBaseApi, quoteDao, quoteDatabase)
    }


    @Test
    fun test_getRandomQuote_notForce_Timeout() = testCoroutineRule.runBlockingTest {
        //given
        val quoteAPI = Quote(author = "", text = "")
        val listQuoteAPI = listOf(quoteAPI)
        val quoteDB = Quote(
            author = "",
            text = "",
            createdAt = System.currentTimeMillis() - GoQuoteRepository.timeout * 2
        )
        val listQuoteDB = listOf(quoteDB)
        coEvery { goQuoteBaseApi.fetchRandomQuote() } returns QuoteResponse(quotes = listQuoteAPI)
        coEvery { quoteDao.getAll() } returns flow { emit(listQuoteDB) }
        coEvery { quoteDao.deleteAll() } coAnswers {}
        coEvery { quoteDao.insertAllWithTimestamp(*listQuoteAPI.toTypedArray()) } coAnswers {}
        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )
        val transactionLambda = slot<suspend () -> Unit>()
        coEvery { quoteDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
        every { quoteDatabase.quoteDao() } returns quoteDao
        // when
        objectUnderTest!!.getRandomQuote(false).collect {}
        //then
        coVerify(exactly = 1) { quoteDao.getAll() }
        coVerify(exactly = 1) { quoteDao.deleteAll() }
        coVerify(exactly = 1) { quoteDao.insertAllWithTimestamp(*listQuoteAPI.toTypedArray()) }
        coVerify(exactly = 1) { goQuoteBaseApi.fetchRandomQuote() }
        verify(exactly = 1) { quoteDatabase.quoteDao() }
        confirmVerified(goQuoteBaseApi, quoteDao, quoteDatabase)
    }

    @Test
    fun test_getRandomQuote_notForce_notTimeout() = testCoroutineRule.runBlockingTest {
        //given
        val quoteDB = Quote(
            author = "",
            text = "",
            createdAt = System.currentTimeMillis()
        )
        val listQuoteDB = listOf(quoteDB)
        coEvery { quoteDao.getAll() } returns flow { emit(listQuoteDB) }
        every { quoteDatabase.quoteDao() } returns quoteDao
        // when
        objectUnderTest!!.getRandomQuote(false).collect {
            assertThat(it.data).isSameInstanceAs(listQuoteDB)
        }
        //then
        coVerify(exactly = 1) { quoteDao.getAll() }
        verify(exactly = 1) { quoteDatabase.quoteDao() }
        confirmVerified(quoteDao, quoteDatabase)
    }

    @Test
    fun test_getRandomQuote_emptyDB() = testCoroutineRule.runBlockingTest {
        //given
        val quoteAPI = Quote(author = "", text = "")
        val listQuoteAPI = listOf(quoteAPI)
        val listQuoteDB = emptyList<Quote>()
        coEvery { goQuoteBaseApi.fetchRandomQuote() } returns QuoteResponse(quotes = listQuoteAPI)
        coEvery { quoteDao.getAll() } returns flow { emit(listQuoteDB) }
        coEvery { quoteDao.deleteAll() } coAnswers {}
        coEvery { quoteDao.insertAllWithTimestamp(*listQuoteAPI.toTypedArray()) } coAnswers {}
        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )
        val transactionLambda = slot<suspend () -> Unit>()
        coEvery { quoteDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
        every { quoteDatabase.quoteDao() } returns quoteDao
        // when
        objectUnderTest!!.getRandomQuote(true).collect {}
        //then
        coVerify(exactly = 1) { quoteDao.getAll() }
        coVerify(exactly = 1) { quoteDao.deleteAll() }
        coVerify(exactly = 1) { quoteDao.insertAllWithTimestamp(*listQuoteAPI.toTypedArray()) }
        coVerify(exactly = 1) { goQuoteBaseApi.fetchRandomQuote() }
        verify(exactly = 1) { quoteDatabase.quoteDao() }
        confirmVerified(goQuoteBaseApi, quoteDao, quoteDatabase)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}