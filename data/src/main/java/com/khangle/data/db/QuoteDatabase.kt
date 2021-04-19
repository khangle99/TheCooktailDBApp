package com.khangle.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.khangle.domain.model.Quote

@Database(entities = arrayOf(Quote::class), version = 1)
abstract class QuoteDatabase: RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
}