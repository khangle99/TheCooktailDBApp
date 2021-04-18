package com.khangle.thecocktaildbapp.di

import com.khangle.domain.usecase.FetchAlcoholicListUseCase
import com.khangle.domain.usecase.FetchAlcoholicListUseCaseImp
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCase
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AlcoholicBindModule {
    @Binds
    abstract fun bindFetchAlcoholicListUseCase(fetchAlcoholicListUseCaseImp: FetchAlcoholicListUseCaseImp): FetchAlcoholicListUseCase

    @Binds
    abstract fun bindFetchDrinkByAlcoholicUseCase(fetchDrinkByAlcoholicUseCaseImp: FetchDrinkByAlcoholicUseCaseImp): FetchDrinkByAlcoholicUseCase
}