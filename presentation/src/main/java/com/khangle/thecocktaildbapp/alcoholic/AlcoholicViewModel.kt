package com.khangle.thecocktaildbapp.alcoholic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.domain.model.Alcoholic
import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.FetchAlcoholicListUseCase
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlcoholicViewModel constructor(
    private val dispatcher: CoroutineDispatcher,
    private val fetchAlcoholicListUseCase: FetchAlcoholicListUseCase,
    private val fetchDrinkByAlcoholicUseCase: FetchDrinkByAlcoholicUseCase
) : ViewModel() {
    @Inject
    constructor(
        fetchAlcoholicListUseCase: FetchAlcoholicListUseCase,
        fetchDrinkByAlcoholicUseCase: FetchDrinkByAlcoholicUseCase
    ) : this(Dispatchers.IO, fetchAlcoholicListUseCase, fetchDrinkByAlcoholicUseCase)

    private val _alcoholicList = MutableLiveData<Resource<List<Alcoholic>>>()
    val alcoholicList: LiveData<Resource<List<Alcoholic>>> = _alcoholicList
    private val _drinks = MutableLiveData<Resource<List<FilterResultDrink>>>()
    val drinks: LiveData<Resource<List<FilterResultDrink>>> = _drinks
    private val _selectedAlcoholic = MutableLiveData<String>()
    val selectedAlcoholic: LiveData<String> = _selectedAlcoholic
    fun selectedAlcoholic(position: Int) {
        _selectedAlcoholic.value = _alcoholicList.value?.data?.get(position)?.strAlcoholic
    }

    // avoid reloading when configuration change
    private var fetchALcoholicFlag = true // user will change to true in future
    fun fetchAlcoholicList() {
        if (fetchALcoholicFlag) {
            viewModelScope.launch(dispatcher) {
                try {
                    _alcoholicList.postValue(Resource.Loading())
                    val alcoholicList = fetchAlcoholicListUseCase()
                    _alcoholicList.postValue(Resource.Success(data = alcoholicList))
                } catch (e: Exception) {
                    _alcoholicList.postValue(Resource.Error(e))
                }
            }
            fetchALcoholicFlag = false
        }
    }

    fun fetchDrinkByAlcoholic(alcoholicStr: String) {
        viewModelScope.launch(dispatcher) {
            try {
                _drinks.postValue(Resource.Loading())
                val drinks = fetchDrinkByAlcoholicUseCase(alcoholicStr)
                _drinks.postValue(Resource.Success(data = drinks))
            } catch (e: Exception) {
                _drinks.postValue(Resource.Error(e))
            }
        }
    }
}