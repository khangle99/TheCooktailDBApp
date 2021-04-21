package com.khangle.thecocktaildbapp.domain.usecase

import com.khangle.thecocktaildbapp.domain.model.Category
import com.khangle.thecocktaildbapp.domain.repository.TheCockTailDBRepository
import javax.inject.Inject


interface FetchCategoryListUseCase {
    suspend operator fun invoke(): List<Category>
}
class FetchCategoryListUseCaseImp @Inject constructor (private val theCockTailDBRepository: TheCockTailDBRepository): FetchCategoryListUseCase {
    override suspend fun invoke(): List<Category> {
        return theCockTailDBRepository.fetchCategoryList()
    }
}