package com.khangle.data.repository

import androidx.room.withTransaction
import com.khangle.data.db.CockTailDatabase
import com.khangle.data.util.networkBoundResource
import com.khangle.data.webservice.TheCockTailDBBaseApi
import com.khangle.domain.model.*
import com.khangle.domain.repository.TheCockTailDBRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class TheCocktailDBRepositoryImp @Inject constructor(
    private val theCockTailDBBaseApi: TheCockTailDBBaseApi,
    private val cockTailDatabase: CockTailDatabase
) :
    TheCockTailDBRepository {
    override fun getRandomDrink(forceRefresh: Boolean): Flow<Resource<List<Drink>>> {
        val cocktailDao = cockTailDatabase.cocktailDao()
        val flow = networkBoundResource(
            query = { cocktailDao.getAll() },
            fetch = { theCockTailDBBaseApi.fetchRandomDrink().drinks[0] },
            saveFetchResult = { drink ->
                cockTailDatabase.withTransaction {
                    cocktailDao.deleteAll()
                    cocktailDao.insertAllWithTimestamp(drink) // set time for cache time
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

    override suspend fun fetchDrinkById(id: String): Drink {
        return theCockTailDBBaseApi.fetchDrinkById(id).drinks[0]
    }

    override suspend fun query(str: String): List<Drink> {
        return theCockTailDBBaseApi.search(str).drinks
    }

    override suspend fun fetchAlcoholicList(): List<Alcoholic> {
        return theCockTailDBBaseApi.fetchAlcoholicList().drinks
    }

    override suspend fun fetchCategoryList(): List<Category> {
        return theCockTailDBBaseApi.fetchCategoryList().drinks
    }

    override suspend fun fetchDrinkByCategory(categoryStr: String): List<FilterResultDrink> {
        return theCockTailDBBaseApi.fetchDrinksByCategory(categoryStr).drinks
    }

    override suspend fun fetchDrinkByAlcoholic(alcoholicStr: String): List<FilterResultDrink> {
        return theCockTailDBBaseApi.fetchDrinksByAlcoholic(alcoholicStr).drinks
    }

}