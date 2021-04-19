package com.khangle.domain.usecase

import com.khangle.domain.model.Alcoholic
import com.khangle.domain.repository.TheCockTailDBRepository
import javax.inject.Inject

interface FetchAlcoholicListUseCase {
    suspend operator fun invoke(): List<Alcoholic>
}
class FetchAlcoholicListUseCaseImp @Inject constructor(private val theCockTailDBRepository: TheCockTailDBRepository): FetchAlcoholicListUseCase {
    override suspend fun invoke(): List<Alcoholic> {
        return theCockTailDBRepository.fetchAlcoholicList()
    }
}