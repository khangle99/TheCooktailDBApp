package com.khangle.thecocktaildbapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khangle.thecocktaildbapp.domain.model.Quote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    suspend fun insertAllWithTimestamp(vararg quote: Quote) {
        val varargList = quote.map {
            it.apply {
                createdAt = System.currentTimeMillis()
            }
        }.toTypedArray()
        insertAll(*varargList)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg quote: Quote)

    @Query("SELECT * FROM quote")
    fun getAll(): Flow<List<Quote>>

    @Query("DELETE FROM quote")
    suspend fun deleteAll()
}