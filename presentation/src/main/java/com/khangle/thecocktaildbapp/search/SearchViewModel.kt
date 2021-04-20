package com.khangle.thecocktaildbapp.search

import android.content.res.Resources
import androidx.lifecycle.*
import com.khangle.domain.model.Drink
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.SearchDrinkByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel @Inject constructor(private val searchDrinkByNameUseCase: SearchDrinkByNameUseCase) :
    ViewModel() {
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
        viewModelScope.launch {
            if (!str.equals(stateFlow.value)) {
                stateFlow.value = str
                setupCollectIfNot()
            }
        }
    }


}