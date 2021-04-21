package com.khangle.thecocktaildbapp.domain.model

import com.google.gson.annotations.SerializedName

class FilterResultDrink(@SerializedName("idDrink")val id: String,
                        @SerializedName("strDrinkThumb") val thumbUrl: String,
                        @SerializedName("strDrink") val name: String)

class FilterResultResponse(val drinks: List<FilterResultDrink>)