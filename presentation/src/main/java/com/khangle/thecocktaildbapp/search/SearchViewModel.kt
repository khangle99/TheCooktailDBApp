package com.khangle.thecocktaildbapp.search

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

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchDrinkByNameUseCase: SearchDrinkByNameUseCase) :
    ViewModel() {

    fun query(queryStr: String): Flow<List<Drink>> {
        return flow {
            emit(searchDrinkByNameUseCase(queryStr))
        }
    }

    private val stateFlow = MutableStateFlow("")
    @ExperimentalCoroutinesApi
    fun queryStr(str: String) {
        viewModelScope.launch {
            if (!str.equals(stateFlow.value)) {
                stateFlow.value = str
               try {
                   _drinks.postValue(Resource.Loading())
                   stateFlow.flatMapLatest {
                       query(it)
                   }.collect {
                       if (it != null) { // null when no result match
                           _drinks.postValue(Resource.Success(data = it))
                       } else {
                           _drinks.postValue(Resource.Error("Not found",data = emptyList()))
                       }
                   }
               } catch (e: Exception) {
                   _drinks.postValue(Resource.Error(e.message!!))
               }
            }
        }
    }

    private val _drinks = MutableLiveData<Resource<List<Drink>>>()
    val drinks: LiveData<Resource<List<Drink>>> = _drinks
    override fun onCleared() {
        super.onCleared()

    }
}