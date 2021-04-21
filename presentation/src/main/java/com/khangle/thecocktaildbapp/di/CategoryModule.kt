@file:Suppress("unused")

package com.khangle.thecocktaildbapp.di

import com.khangle.domain.usecase.FetchCategoryListUseCase
import com.khangle.domain.usecase.FetchCategoryListUseCaseImp
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCase
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class CategoryBindModule {
    @Binds
    @ViewModelScoped
    abstract fun bindFetchCategoryListUseCase(fetchCategoryListUseCaseImp: FetchCategoryListUseCaseImp): FetchCategoryListUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindFetchDrinkByCategoryUseCase(fetchDrinkByCategoryUseCaseImp: FetchDrinkByCategoryUseCaseImp): FetchDrinkByCategoryUseCase
}