package com.khangle.thecocktaildbapp.domain.usecase

import com.khangle.thecocktaildbapp.domain.model.Drink
import com.khangle.thecocktaildbapp.domain.repository.TheCockTailDBRepository
import javax.inject.Inject

interface FetchDrinkByIdUseCase {
    suspend operator fun invoke(id: String): Drink
}
class FetchDrinkByIdUseCaseImp @Inject constructor(private val theCockTailDBRepository: TheCockTailDBRepository): FetchDrinkByIdUseCase {
    override suspend fun invoke(id: String): Drink {
        return theCockTailDBRepository.fetchDrinkById(id)
    }

}