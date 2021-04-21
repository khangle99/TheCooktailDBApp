package com.khangle.thecocktaildbapp.domain.category

import com.google.common.truth.Truth
import com.khangle.thecocktaildbapp.domain.model.Category
import com.khangle.thecocktaildbapp.domain.repository.TheCockTailDBRepository
import com.khangle.thecocktaildbapp.domain.usecase.FetchCategoryListUseCase
import com.khangle.thecocktaildbapp.domain.usecase.FetchCategoryListUseCaseImp
import com.khangle.thecocktaildbapp.domain.util.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FetchCategoryListUseCaseTest {
    var objectUnderTest: FetchCategoryListUseCase? = null

    @MockK
    lateinit var theCockTailDBRepository: TheCockTailDBRepository

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = FetchCategoryListUseCaseImp(theCockTailDBRepository)
    }

    @Test
    fun test_fetchCategoryList_returnCategoryList() = testCoroutineRule.runBlockingTest {
        //given
        val category = Category("")
        val categoryList = listOf(category)
        coEvery { theCockTailDBRepository.fetchCategoryList() } returns categoryList
        //when
        val result = objectUnderTest!!.invoke()
        //then
        coVerify(exactly = 1) { theCockTailDBRepository.fetchCategoryList() }
        Truth.assertThat(result).isSameInstanceAs(categoryList)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}