@file:Suppress("unused")

package com.khangle.thecocktaildbapp.presentation.di

import com.khangle.thecocktaildbapp.domain.usecase.GetRandomDrinkUseCase
import com.khangle.thecocktaildbapp.domain.usecase.GetRandomDrinkUseCaseImp
import com.khangle.thecocktaildbapp.domain.usecase.GetRandomQuoteUseCase
import com.khangle.thecocktaildbapp.domain.usecase.GetRandomQuoteUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeViewModelModule {
    @Binds
    @ViewModelScoped
    abstract fun bindRandomQuoteUseCase(getRandomQuoteUseCaseImp: GetRandomQuoteUseCaseImp): GetRandomQuoteUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindUseCase(randomDrinkUseCaseImp: GetRandomDrinkUseCaseImp) : GetRandomDrinkUseCase
}