package com.khangle.thecocktaildbapp.alcoholic

import androidx.lifecycle.ViewModel
import com.khangle.domain.usecase.FetchAlcoholicListUseCase
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlcoholicViewModel @Inject constructor(
    private val fetchAlcoholicListUseCase: FetchAlcoholicListUseCase,
    private val fetchDrinkByAlcoholicUseCase: FetchDrinkByAlcoholicUseCase
): ViewModel() {
}