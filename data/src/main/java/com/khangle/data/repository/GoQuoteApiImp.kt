package com.khangle.data.repository

import androidx.room.withTransaction
import com.khangle.data.db.QuoteDatabase
import com.khangle.data.util.networkBoundResource
import com.khangle.data.webservice.GoQuoteBaseApi
import com.khangle.domain.model.Quote
import com.khangle.domain.model.Resource
import com.khangle.domain.repository.GoQuoteRepository
import com.khangle.domain.repository.GoQuoteRepository.Companion.timeout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GoQuoteRepositoryImp @Inject constructor(
    private val goQuoteBaseApi: GoQuoteBaseApi,
    private val quoteDatabase: QuoteDatabase
) : GoQuoteRepository {
    override fun getRandomQuote(forceRefresh: Boolean): Flow<Resource<List<Quote>>> {
        val quoteDao = quoteDatabase.quoteDao()
        return networkBoundResource(
            query = { quoteDao.getAll() },
            fetch = {
                goQuoteBaseApi.fetchRandomQuote().quotes[0]
            },
            saveFetchResult = { quote ->
                quoteDatabase.withTransaction {
                    quoteDao.deleteAll()
                    quoteDao.insertAllWithTimestamp(quote)
                }
            },
            shouldFetch = {
                if (forceRefresh || it.isEmpty()) {
                    true
                } else {
                    System.currentTimeMillis() - it[0].createdAt > timeout
                }
            } // kiem tra timeout
        )
    }


}