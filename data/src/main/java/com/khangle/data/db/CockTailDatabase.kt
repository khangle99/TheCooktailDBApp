package com.khangle.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.khangle.domain.model.Drink

@Database(entities = arrayOf(Drink::class), version = 1)
@TypeConverters(Converter::class)
abstract class CockTailDatabase : RoomDatabase() {
    abstract fun cocktailDao(): CockTailDao
}