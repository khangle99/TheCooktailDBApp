package com.khangle.domain.usecase

import com.khangle.domain.model.Drink
import com.khangle.domain.model.Resource
import com.khangle.domain.repository.TheCockTailDBRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetRandomDrinkUseCase {
     operator fun invoke(forceRefresh: Boolean): Flow<Resource<List<Drink>>>
}
class GetRandomDrinkUseCaseImp @Inject constructor(private val theCockTailDBRepository: TheCockTailDBRepository): GetRandomDrinkUseCase{
    override operator fun invoke(forceRefresh: Boolean): Flow<Resource<List<Drink>>> {
        return theCockTailDBRepository.getRandomDrink(forceRefresh)
    }
}