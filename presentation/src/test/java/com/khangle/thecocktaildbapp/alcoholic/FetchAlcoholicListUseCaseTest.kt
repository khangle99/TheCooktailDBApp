package com.khangle.thecocktaildbapp.alcoholic

import com.google.common.truth.Truth.assertThat
import com.khangle.domain.model.Alcoholic
import com.khangle.domain.repository.TheCockTailDBRepository
import com.khangle.domain.usecase.FetchAlcoholicListUseCase
import com.khangle.domain.usecase.FetchAlcoholicListUseCaseImp
import com.khangle.thecocktaildbapp.util.TestCoroutineRule
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
class FetchAlcoholicListUseCaseTest {
    var objectUnderTest: FetchAlcoholicListUseCase? = null

    @MockK
    lateinit var theCockTailDBRepository: TheCockTailDBRepository

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = FetchAlcoholicListUseCaseImp(theCockTailDBRepository)
    }

    @Test
    fun test_fetchAlcoholicList_returnAlcoholicList() = testCoroutineRule.runBlockingTest {
        //given
        val alcoholic = Alcoholic("")
        val alcoholicList = listOf(alcoholic)
        coEvery { theCockTailDBRepository.fetchAlcoholicList() } returns alcoholicList
        //when
        val result = objectUnderTest!!.invoke()
        //then
        coVerify(exactly = 1) { theCockTailDBRepository.fetchAlcoholicList() }
        assertThat(result).isSameInstanceAs(alcoholicList)
    }

    @After
    fun teardown() {
        objectUnderTest = null
    }
}