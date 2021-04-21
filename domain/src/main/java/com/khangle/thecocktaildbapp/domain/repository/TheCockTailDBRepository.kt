package com.khangle.thecocktaildbapp.domain.repository

import com.khangle.thecocktaildbapp.domain.model.*
import kotlinx.coroutines.flow.Flow

interface TheCockTailDBRepository {
    fun getRandomDrink(forceRefresh: Boolean): Flow<Resource<List<Drink>>>
    suspend fun fetchDrinkById(id: String): Drink
    suspend fun query(str: String): List<Drink>
    suspend fun fetchAlcoholicList(): List<Alcoholic>
    suspend fun fetchCategoryList(): List<Category>
    suspend fun fetchDrinkByCategory(categoryStr: String): List<FilterResultDrink>
    suspend fun fetchDrinkByAlcoholic(alcoholicStr: String): List<FilterResultDrink>
    companion object {
        const val timeout = 60 * 60 * 1000
    }
}