package com.khangle.thecocktaildbapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.khangle.thecocktaildbapp.domain.model.Drink

@Database(entities = [Drink::class], version = 1)
@TypeConverters(Converter::class)
abstract class CockTailDatabase : RoomDatabase() {
    abstract fun cocktailDao(): CockTailDao
}