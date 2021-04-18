package com.khangle.data.webservice

import com.khangle.domain.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCockTailDBBaseApi {
    suspend fun getDrink(id: String): DrinkDetailResponse
    suspend fun search(query: String): DrinkDetailResponse
    suspend fun getRandom(): DrinkDetailResponse
    suspend fun getCategoryList(): CategoryListResponse
    suspend fun getAlcoholicList(): AlcoholicListResponse
    suspend fun getDrinksByAlcoholic(alcoholic: String): FilterResultResponse
    suspend fun getDrinksByCategory(category: String): FilterResultResponse
}

interface TheCockTailDBApi: TheCockTailDBBaseApi {
    @GET("lookup.php")
    override suspend fun getDrink(@Query("i") id: String): DrinkDetailResponse
    @GET("search.php")
    override suspend fun search(@Query("s") query: String): DrinkDetailResponse
    @GET("random.php")
    override suspend fun getRandom(): DrinkDetailResponse
    @GET("list.php?c=list")
    override suspend fun getCategoryList(): CategoryListResponse
    @GET("list.php?a=list")
    override suspend fun getAlcoholicList(): AlcoholicListResponse
    @GET("filter.php")
    override suspend fun getDrinksByAlcoholic(@Query("a") alcoholic: String): FilterResultResponse
    @GET("filter.php")
    override suspend fun getDrinksByCategory(@Query("c") category: String): FilterResultResponse
}