package com.khangle.thecocktaildbapp.di

import com.khangle.domain.usecase.FetchAlcoholicListUseCase
import com.khangle.domain.usecase.FetchAlcoholicListUseCaseImp
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCase
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class AlcoholicBindModule {
    @Binds
    @ViewModelScoped
    abstract fun bindFetchAlcoholicListUseCase(fetchAlcoholicListUseCaseImp: FetchAlcoholicListUseCaseImp): FetchAlcoholicListUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindFetchDrinkByAlcoholicUseCase(fetchDrinkByAlcoholicUseCaseImp: FetchDrinkByAlcoholicUseCaseImp): FetchDrinkByAlcoholicUseCase
}