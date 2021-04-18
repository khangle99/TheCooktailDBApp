package com.khangle.thecocktaildbapp.search

import androidx.lifecycle.ViewModel
import com.khangle.domain.model.Drink
import com.khangle.domain.usecase.SearchDrinkByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchDrinkByNameUseCase: SearchDrinkByNameUseCase): ViewModel(){

    fun query(queryStr: String): Flow<List<Drink>> {
        return flow {
            emit(searchDrinkByNameUseCase(queryStr))
        }
    }
}