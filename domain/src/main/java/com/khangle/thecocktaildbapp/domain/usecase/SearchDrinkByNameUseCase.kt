package com.khangle.thecocktaildbapp.domain.usecase

import com.khangle.thecocktaildbapp.domain.model.Drink
import com.khangle.thecocktaildbapp.domain.repository.TheCockTailDBRepository
import javax.inject.Inject

interface SearchDrinkByNameUseCase {
    suspend operator fun invoke(str:String) : List<Drink>
}
class SearchDrinkByNameUseCaseImp @Inject constructor(private val theCockTailDBRepository: TheCockTailDBRepository): SearchDrinkByNameUseCase {
    override suspend fun invoke(str: String): List<Drink> {
       return  theCockTailDBRepository.query(str)
    }

}