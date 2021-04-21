package com.khangle.thecocktaildbapp.domain.usecase

import com.khangle.thecocktaildbapp.domain.model.Alcoholic
import com.khangle.thecocktaildbapp.domain.repository.TheCockTailDBRepository
import javax.inject.Inject

interface FetchAlcoholicListUseCase {
    suspend operator fun invoke(): List<Alcoholic>
}
class FetchAlcoholicListUseCaseImp @Inject constructor(private val theCockTailDBRepository: TheCockTailDBRepository): FetchAlcoholicListUseCase {
    override suspend fun invoke(): List<Alcoholic> {
        return theCockTailDBRepository.fetchAlcoholicList()
    }
}