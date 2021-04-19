package com.khangle.data.webservice

import com.khangle.domain.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCockTailDBBaseApi {
    suspend fun fetchDrinkById(id: String): DrinkDetailResponse
    suspend fun search(query: String): DrinkDetailResponse
    suspend fun fetchRandomDrink(): DrinkDetailResponse
    suspend fun fetchCategoryList(): CategoryListResponse
    suspend fun fetchAlcoholicList(): AlcoholicListResponse
    suspend fun fetchDrinksByAlcoholic(alcoholic: String): FilterResultResponse
    suspend fun fetchDrinksByCategory(category: String): FilterResultResponse
}

interface TheCockTailDBApi: TheCockTailDBBaseApi {
    @GET("lookup.php")
    override suspend fun fetchDrinkById(@Query("i") id: String): DrinkDetailResponse
    @GET("search.php")
    override suspend fun search(@Query("s") query: String): DrinkDetailResponse
    @GET("random.php")
    override suspend fun fetchRandomDrink(): DrinkDetailResponse
    @GET("list.php?c=list")
    override suspend fun fetchCategoryList(): CategoryListResponse
    @GET("list.php?a=list")
    override suspend fun fetchAlcoholicList(): AlcoholicListResponse
    @GET("filter.php")
    override suspend fun fetchDrinksByAlcoholic(@Query("a") alcoholic: String): FilterResultResponse
    @GET("filter.php")
    override suspend fun fetchDrinksByCategory(@Query("c") category: String): FilterResultResponse
}