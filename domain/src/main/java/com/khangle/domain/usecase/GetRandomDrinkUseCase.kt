package com.khangle.domain.usecase

import com.khangle.domain.model.Drink
import com.khangle.domain.repository.TheCockTailDBRepository
import javax.inject.Inject

interface GetRandomDrinkUseCase {
    suspend operator fun invoke(): Drink
}
class GetRandomDrinkUseCaseImp @Inject constructor(private val theCockTailDBRepository: TheCockTailDBRepository): GetRandomDrinkUseCase{
    override suspend operator fun invoke(): Drink {
        return theCockTailDBRepository.getRandomDrink()
    }
}