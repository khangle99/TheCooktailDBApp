package com.khangle.thecocktaildbapp.di

import com.khangle.domain.usecase.GetRandomDrinkUseCase
import com.khangle.domain.usecase.GetRandomDrinkUseCaseImp
import com.khangle.domain.usecase.GetRandomQuoteUseCase
import com.khangle.domain.usecase.GetRandomQuoteUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeViewModelModule {
    @Binds
    abstract fun bindRandomQuoteUseCase(getRandomQuoteUseCaseImp: GetRandomQuoteUseCaseImp): GetRandomQuoteUseCase

    @Binds
    abstract fun bindUseCase(randomDrinkUseCaseImp: GetRandomDrinkUseCaseImp) : GetRandomDrinkUseCase
}