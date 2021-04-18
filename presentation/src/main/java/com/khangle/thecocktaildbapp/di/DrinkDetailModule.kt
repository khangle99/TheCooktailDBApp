package com.khangle.thecocktaildbapp.di

import com.khangle.domain.usecase.FetchDrinkByIdUseCase
import com.khangle.domain.usecase.FetchDrinkByIdUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DrinkDetailBindModule {
    @Binds
    abstract fun bindFetchDrinkById(fetchDrinkByIdUseCaseImp: FetchDrinkByIdUseCaseImp): FetchDrinkByIdUseCase
}