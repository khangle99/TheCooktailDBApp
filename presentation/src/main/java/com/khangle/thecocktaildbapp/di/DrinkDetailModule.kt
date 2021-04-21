@file:Suppress("unused")

package com.khangle.thecocktaildbapp.di

import com.khangle.domain.usecase.FetchDrinkByIdUseCase
import com.khangle.domain.usecase.FetchDrinkByIdUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class DrinkDetailBindModule {
    @Binds
    @ViewModelScoped
    abstract fun bindFetchDrinkById(fetchDrinkByIdUseCaseImp: FetchDrinkByIdUseCaseImp): FetchDrinkByIdUseCase
}