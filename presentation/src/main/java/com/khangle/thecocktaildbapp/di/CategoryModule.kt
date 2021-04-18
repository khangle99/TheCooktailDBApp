package com.khangle.thecocktaildbapp.di

import com.khangle.domain.usecase.FetchCategoryListUseCase
import com.khangle.domain.usecase.FetchCategoryListUseCaseImp
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCase
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CategoryBindModule {
    @Binds
    abstract fun bindFetchCategoryListUseCase(fetchCategoryListUseCaseImp: FetchCategoryListUseCaseImp): FetchCategoryListUseCase

    @Binds
    abstract fun bindFetchDrinkByCategoryUseCase(fetchDrinkByCategoryUseCaseImp: FetchDrinkByCategoryUseCaseImp): FetchDrinkByCategoryUseCase
}