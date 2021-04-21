package com.khangle.thecocktaildbapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khangle.thecocktaildbapp.domain.model.Drink
import kotlinx.coroutines.flow.Flow

@Dao
interface CockTailDao { // cache only one drink at home screen
    suspend fun insertAllWithTimestamp(vararg drink: Drink) {
        val varargList = drink.map {
            it.apply {
                createdAt = System.currentTimeMillis()
            }
        }.toTypedArray()

        insertAll(*varargList)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg drink: Drink)

    @Query("SELECT * FROM drink")
    fun getAll(): Flow<List<Drink>>

    @Query("DELETE FROM drink")
    suspend fun deleteAll()
}