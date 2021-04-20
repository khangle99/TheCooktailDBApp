package com.khangle.thecocktaildbapp.repository

import androidx.room.withTransaction
import com.google.common.truth.Truth.assertThat
import com.khangle.data.db.QuoteDao
import com.khangle.data.db.QuoteDatabase
import com.khangle.data.repository.GoQuoteRepositoryImp
import com.khangle.data.webservice.GoQuoteBaseApi
import com.khangle.domain.model.Quote
import com.khangle.domain.model.QuoteResponse
import com.khangle.domain.model.Resource
import com.khangle.domain.repository.GoQuoteRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

//class QuoteRepositoryTest {
//
//    var objectUnderTest: GoQuoteRepository? = null
//
//    @MockK
//    lateinit var quoteDatabase: QuoteDatabase
//
//    @MockK
//    lateinit var goQuoteBaseApi: GoQuoteBaseApi
//
//    @MockK
//    lateinit var quoteDao: QuoteDao
//
//    @Before
//    fun setup() {
//        MockKAnnotations.init(this)
//        objectUnderTest = GoQuoteRepositoryImp(goQuoteBaseApi, quoteDatabase)
//    }
//
//    @Test
//    fun test_getRandomQuote_force() = runBlocking {
//        val quoteAPI = Quote(author = "", text = "")
//        val listQuoteAPI = listOf(quoteAPI)
//        val quoteDB = Quote(author = "", text = "")
//        val listQuoteDB = listOf(quoteDB)
//        coEvery { quoteDao.getAll() } returns flow { emit(listQuoteDB) }
////        coEvery { quoteDao.deleteAll() }
////        coEvery { quoteDao.insertAllWithTimestamp(quote) }
////                coEvery {
////            quoteDatabase.withTransaction {
////                coJustRun {
////                    any()
////                }
////            }
////        }
//        coEvery { goQuoteBaseApi.fetchRandomQuote() } returns QuoteResponse(quotes = listQuoteAPI)
//        every { quoteDatabase.quoteDao() } returns quoteDao
//
//
//        val flow: Flow<Resource<List<Quote>>> =   objectUnderTest!!.getRandomQuote(true)
//        flow.collect {
//          assertThat(it.data).isSameInstanceAs(listQuoteAPI)
//        }
//
//        //       coVerify { quoteDao.deleteAll() }
//        //       coVerify { quoteDao.insertAllWithTimestamp(quote) }
//        verify { quoteDatabase.quoteDao() }
//        coVerify { quoteDao.getAll() }
//        coVerify { goQuoteBaseApi.fetchRandomQuote() }
//    }
//
//
//    @Test
//    fun test_getRandomQuote_not_force_timeOut() = runBlocking {
//        val quoteAPI = Quote(author = "", text = "")
//        val listQuoteAPI = listOf(quoteAPI)
//        val quoteDB = Quote(author = "", text = "", createdAt = System.currentTimeMillis() - 60*60*1000*2)
//        val listQuoteDB = listOf(quoteDB)
//        coEvery { quoteDao.getAll() } returns flow { emit(listQuoteDB) }
//        coEvery { goQuoteBaseApi.fetchRandomQuote() } returns QuoteResponse(quotes = listQuoteAPI)
//        every { quoteDatabase.quoteDao() } returns quoteDao
//
//
//        val flow: Flow<Resource<List<Quote>>> =   objectUnderTest!!.getRandomQuote(false)
//        flow.collect {
//            assertThat(it.data).isSameInstanceAs(listQuoteDB)
//        }
//        verify { quoteDatabase.quoteDao() }
//        coVerify { quoteDao.getAll() }
//        coVerify { goQuoteBaseApi.fetchRandomQuote() }
//    }
//    @Test
//    fun test_getRandomQuote_not_force_not_TimeOut() = runBlocking {
//        val quoteAPI = Quote(author = "", text = "")
//        val listQuoteAPI = listOf(quoteAPI)
//        val quoteDB = Quote(author = "", text = "", createdAt = System.currentTimeMillis()) // not time out
//        val listQuoteDB = listOf(quoteDB)
//        coEvery { quoteDao.getAll() } returns flow { emit(listQuoteDB) }
//        coEvery { goQuoteBaseApi.fetchRandomQuote() } returns QuoteResponse(quotes = listQuoteAPI)
//        every { quoteDatabase.quoteDao() } returns quoteDao
//
//        val flow: Flow<Resource<List<Quote>>> =   objectUnderTest!!.getRandomQuote(false)
//        flow.collect {
//            assertThat(it.data).isSameInstanceAs(listQuoteDB)
//        }
//        verify { quoteDatabase.quoteDao() }
//        coVerify { quoteDao.getAll() }
//    }
//
//    @Test
//    fun test_getRandomQuote_empty_db()= runBlocking {
//        val quoteAPI = Quote(author = "", text = "")
//        val listQuoteAPI = listOf(quoteAPI)
//        coEvery { quoteDao.getAll() } returns flow { emit(emptyList<Quote>()) }
//        coEvery { goQuoteBaseApi.fetchRandomQuote() } returns QuoteResponse(quotes = listQuoteAPI)
//        every { quoteDatabase.quoteDao() } returns quoteDao
//
//        val flow: Flow<Resource<List<Quote>>> =   objectUnderTest!!.getRandomQuote(false)
//        flow.collect {
//            assertThat(it.data).isSameInstanceAs(listQuoteAPI)
//        }
//        verify { quoteDatabase.quoteDao() }
//        coVerify { quoteDao.getAll() }
//        coVerify { goQuoteBaseApi.fetchRandomQuote() }
//    }
//
//
//    @After()
//    fun teardown() {
//        objectUnderTest = null
//    }
//}