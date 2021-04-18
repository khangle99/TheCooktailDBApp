package com.khangle.domain.usecase

import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.repository.TheCockTailDBRepository
import javax.inject.Inject

interface FetchDrinkByAlcoholicUseCase {
    suspend  operator fun invoke(alcoholicStr: String): List<FilterResultDrink>
}
class FetchDrinkByAlcoholicUseCaseImp @Inject constructor(private val theCockTailDBRepository: TheCockTailDBRepository): FetchDrinkByAlcoholicUseCase {
    override suspend fun invoke(alcoholicStr: String): List<FilterResultDrink> {
        return theCockTailDBRepository.getDrinkByAlcoholic(alcoholicStr)
    }

}