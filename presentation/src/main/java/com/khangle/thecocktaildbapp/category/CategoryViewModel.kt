package com.khangle.thecocktaildbapp.category

import androidx.lifecycle.ViewModel
import com.khangle.domain.usecase.FetchCategoryListUseCase
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val fetchCategoryListUseCase: FetchCategoryListUseCase,
    private val fetchDrinkByCategoryUseCase: FetchDrinkByCategoryUseCase
) : ViewModel() {
}