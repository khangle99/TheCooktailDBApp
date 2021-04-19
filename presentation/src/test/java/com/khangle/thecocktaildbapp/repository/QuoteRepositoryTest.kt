package com.khangle.thecocktaildbapp.repository

import androidx.room.withTransaction
import com.khangle.data.db.QuoteDao
import com.khangle.data.db.QuoteDatabase
import com.khangle.data.repository.GoQuoteRepositoryImp
import com.khangle.data.webservice.GoQuoteBaseApi
import com.khangle.domain.model.Quote
import com.khangle.domain.model.QuoteResponse
import com.khangle.domain.repository.GoQuoteRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class QuoteRepositoryTest {

    var objectUnderTest: GoQuoteRepository? = null

    @MockK
    lateinit var quoteDatabase: QuoteDatabase

    @MockK
    lateinit var goQuoteBaseApi: GoQuoteBaseApi

    @MockK
    lateinit var quoteDao: QuoteDao

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = GoQuoteRepositoryImp(goQuoteBaseApi, quoteDatabase)
    }

    @Test
    fun test_getRandomQuote_force() = runBlocking {
        val quote = Quote(author = "", text = "")
        val listQuote = listOf(quote)
        coEvery { quoteDao.getAll() } returns flow { emit(listQuote) }
     //   coEvery { quoteDao.deleteAll() }
    //    coEvery { quoteDao.insertAllWithTimestamp(quote) }
  //      coEvery { goQuoteBaseApi.fetchRandomQuote() } returns QuoteResponse(quotes = listQuote)


//        coEvery {
//            quoteDatabase.withTransaction {
//                coJustRun {
//                    any()
//                }
//            }
//        }
        every { quoteDatabase.quoteDao() } returns quoteDao


        objectUnderTest!!.getRandomQuote(true).collect { }
        coVerify { quoteDao.getAll() }
//        coVerify { quoteDao.deleteAll() }
 //       coVerify { quoteDao.insertAllWithTimestamp(quote) }
//        coVerify {
//            quoteDatabase.withTransaction {
//                coJustRun {
//                    any()
//                }
//            }
//        }
        coVerify { goQuoteBaseApi.fetchRandomQuote() }
   //     confirmVerified()
//        confirmVerified(quoteDao)
//        confirmVerified(quoteDatabase)
//        confirmVerified(goQuoteBaseApi)
    }
//    @Test
//    fun test_getRandomQuote_not_force() {
//
//    }
//
//    @Test
//    fun test_getRandomQuote_empty() {
//
//    }


    @After()
    fun teardown() {
        objectUnderTest = null
    }
}