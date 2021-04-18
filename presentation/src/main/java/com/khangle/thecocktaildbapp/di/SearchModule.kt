package com.khangle.thecocktaildbapp.di

import com.khangle.domain.usecase.SearchDrinkByNameUseCase
import com.khangle.domain.usecase.SearchDrinkByNameUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SearchBindModule {

    @Binds
    abstract fun bindSearchUseCase(searchDrinkByNameUseCaseImp: SearchDrinkByNameUseCaseImp): SearchDrinkByNameUseCase
}