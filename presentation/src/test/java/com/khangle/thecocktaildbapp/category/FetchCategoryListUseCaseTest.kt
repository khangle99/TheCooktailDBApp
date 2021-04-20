package com.khangle.thecocktaildbapp.category

import com.google.common.truth.Truth
import com.khangle.domain.model.Alcoholic
import com.khangle.domain.model.Category
import com.khangle.domain.repository.TheCockTailDBRepository
import com.khangle.domain.usecase.FetchAlcoholicListUseCase
import com.khangle.domain.usecase.FetchAlcoholicListUseCaseImp
import com.khangle.domain.usecase.FetchCategoryListUseCase
import com.khangle.domain.usecase.FetchCategoryListUseCaseImp
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class FetchCategoryListUseCaseTest {
    var objectUnderTest : FetchCategoryListUseCase? = null

    @MockK
    lateinit var theCockTailDBRepository: TheCockTailDBRepository
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = FetchCategoryListUseCaseImp(theCockTailDBRepository)
    }

    @Test
    fun test_invoke() = runBlocking {
        val category = Category("")
        val categoryList = listOf(category)
        coEvery { theCockTailDBRepository.fetchCategoryList() } returns categoryList
        val result = objectUnderTest!!.invoke()
        coVerify { theCockTailDBRepository.fetchCategoryList()  }
        Truth.assertThat(result).isSameInstanceAs(categoryList)
    }


    @After
    fun teardown() {
        objectUnderTest = null
    }
}