@file:Suppress("unused")

package com.khangle.thecocktaildbapp.presentation.di

import com.khangle.thecocktaildbapp.domain.usecase.SearchDrinkByNameUseCase
import com.khangle.thecocktaildbapp.domain.usecase.SearchDrinkByNameUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class SearchBindModule {
    @Binds
    @ViewModelScoped
    abstract fun bindSearchUseCase(searchDrinkByNameUseCaseImp: SearchDrinkByNameUseCaseImp): SearchDrinkByNameUseCase
}