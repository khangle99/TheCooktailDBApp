package com.khangle.domain.usecase

import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.repository.TheCockTailDBRepository
import javax.inject.Inject

interface FetchDrinkByCategoryUseCase {
    suspend operator fun invoke(categoryStr: String): List<FilterResultDrink>
}

class FetchDrinkByCategoryUseCaseImp @Inject constructor(private val theCockTailDBRepository: TheCockTailDBRepository): FetchDrinkByCategoryUseCase {
    override suspend fun invoke(categoryStr: String): List<FilterResultDrink> {
        return theCockTailDBRepository.fetchDrinkByCategory(categoryStr)
    }
}