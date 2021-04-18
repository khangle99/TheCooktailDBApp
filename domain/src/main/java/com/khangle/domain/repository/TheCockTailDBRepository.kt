package com.khangle.domain.repository

import com.khangle.domain.model.Alcoholic
import com.khangle.domain.model.Category
import com.khangle.domain.model.Drink
import com.khangle.domain.model.FilterResultDrink

interface TheCockTailDBRepository {
    suspend fun getRandomDrink(): Drink
    suspend fun getDrinkById(id: String): Drink
    suspend fun query(str: String): List<Drink>
    suspend fun getAlcoholicList(): List<Alcoholic>
    suspend fun getCategoryList(): List<Category>
    suspend fun getDrinkByCategory(categoryStr: String): List<FilterResultDrink>
    suspend fun getDrinkByAlcoholic(alcoholicStr: String): List<FilterResultDrink>
}