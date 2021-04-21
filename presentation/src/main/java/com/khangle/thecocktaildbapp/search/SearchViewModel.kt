@file:Suppress("SENSELESS_COMPARISON")

package com.khangle.thecocktaildbapp.search

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.domain.model.Drink
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.SearchDrinkByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel constructor(
    private val dispatcher: CoroutineDispatcher,
    private val searchDrinkByNameUseCase: SearchDrinkByNameUseCase
) :
    ViewModel() {
    @Inject
    constructor(searchDrinkByNameUseCase: SearchDrinkByNameUseCase) : this(
        Dispatchers.IO,
        searchDrinkByNameUseCase
    )

    private val _drinks = MutableLiveData<Resource<List<Drink>>>()
    val drinks: LiveData<Resource<List<Drink>>> = _drinks
    private val stateFlow = MutableStateFlow("")

    private fun query(queryStr: String): Flow<List<Drink>> {
        return flow {
            emit(searchDrinkByNameUseCase(queryStr))
        }
    }

    private var isCollected = false // flag check
    private suspend fun setupCollectIfNot() {
        if (!isCollected) {
            isCollected = true
            try {
                _drinks.postValue(Resource.Loading())
                val a = stateFlow.flatMapLatest {
                    query(it)
                }
                a.collect {
                    if (it != null) { // null when no result match
                        _drinks.postValue(Resource.Success(data = it))
                    } else {
                        _drinks.postValue(
                            Resource.Error(
                                Resources.NotFoundException(),
                                data = emptyList()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _drinks.postValue(Resource.Error(e))
            }
        }
    }

    fun queryStr(str: String) {
        viewModelScope.launch(dispatcher) {
            if (str != stateFlow.value) {
                stateFlow.value = str
                setupCollectIfNot()
            }
        }
    }


}