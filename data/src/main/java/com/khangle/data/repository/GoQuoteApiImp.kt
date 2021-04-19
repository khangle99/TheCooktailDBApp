package com.khangle.data.repository

import androidx.room.withTransaction
import com.khangle.data.db.QuoteDatabase
import com.khangle.data.util.networkBoundResource
import com.khangle.data.webservice.GoQuoteBaseApi
import com.khangle.domain.model.Quote
import com.khangle.domain.model.Resource
import com.khangle.domain.repository.GoQuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoQuoteRepositoryImp @Inject constructor(
    private val goQuoteBaseApi: GoQuoteBaseApi,
    private val quoteDatabase: QuoteDatabase
) : GoQuoteRepository {
    override fun getRandomQuote(forceRefresh: Boolean): Flow<Resource<List<Quote>>> {
        val quoteDao = quoteDatabase.quoteDao()
        val flow = networkBoundResource(
            query = { quoteDao.getAll() },
            fetch = { goQuoteBaseApi.fetchRandomQuote().quotes[0] },
            saveFetchResult = { drink ->
                quoteDatabase.withTransaction {
                    quoteDao.deleteAll()
                    quoteDao.insertAllWithTimestamp(drink)
                }
            },
            shouldFetch = {
                if (forceRefresh || it.isEmpty()) {
                    true
                } else {
                    System.currentTimeMillis() - it.get(0).createdAt > 60 * 60 * 1000
                }
            } // kiem tra timeout
        )
        return flow
    }
}