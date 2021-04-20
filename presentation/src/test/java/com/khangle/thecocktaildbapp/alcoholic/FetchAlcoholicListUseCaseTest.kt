package com.khangle.thecocktaildbapp.alcoholic

import com.google.common.truth.Truth.assertThat
import com.khangle.domain.model.Alcoholic
import com.khangle.domain.repository.TheCockTailDBRepository
import com.khangle.domain.usecase.FetchAlcoholicListUseCase
import com.khangle.domain.usecase.FetchAlcoholicListUseCaseImp
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class FetchAlcoholicListUseCaseTest {
    var objectUnderTest :FetchAlcoholicListUseCase? = null

    @MockK
    lateinit var theCockTailDBRepository: TheCockTailDBRepository
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        objectUnderTest = FetchAlcoholicListUseCaseImp(theCockTailDBRepository)
    }

    @Test
    fun test_invoke() = runBlocking {
        val alcoholic = Alcoholic("")
        val alcoholicList = listOf(alcoholic)
        coEvery { theCockTailDBRepository.fetchAlcoholicList() } returns alcoholicList
        val result = objectUnderTest!!.invoke()
        coVerify { theCockTailDBRepository.fetchAlcoholicList()  }
        assertThat(result).isSameInstanceAs(alcoholicList)
    }


    @After
    fun teardown() {
        objectUnderTest = null
    }
}