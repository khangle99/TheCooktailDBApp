package com.khangle.data.repository

import com.khangle.data.webservice.TheCockTailDBBaseApi
import com.khangle.domain.model.Alcoholic
import com.khangle.domain.model.Category
import com.khangle.domain.model.Drink
import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.repository.TheCockTailDBRepository
import javax.inject.Inject


class TheCocktailDBRepositoryImp @Inject constructor(private val theCockTailDBBaseApi: TheCockTailDBBaseApi) :
    TheCockTailDBRepository {
    override suspend fun getRandomDrink(): Drink {
       return  theCockTailDBBaseApi.getRandom().drinks[0]
    }

    override suspend fun getDrinkById(id: String): Drink {
        return theCockTailDBBaseApi.getDrink(id).drinks[0]
    }

    override suspend fun query(str: String): List<Drink> {
        return theCockTailDBBaseApi.search(str).drinks
    }

    override suspend fun getAlcoholicList(): List<Alcoholic> {
        return theCockTailDBBaseApi.getAlcoholicList().drinks
    }

    override suspend fun getCategoryList(): List<Category> {
       return theCockTailDBBaseApi.getCategoryList().drinks
    }

    override suspend fun getDrinkByCategory(categoryStr: String): List<FilterResultDrink> {
       return theCockTailDBBaseApi.getDrinksByCategory(categoryStr).drinks
    }

    override suspend fun getDrinkByAlcoholic(alcoholicStr: String): List<FilterResultDrink> {
       return theCockTailDBBaseApi.getDrinksByAlcoholic(alcoholicStr).drinks
    }

}