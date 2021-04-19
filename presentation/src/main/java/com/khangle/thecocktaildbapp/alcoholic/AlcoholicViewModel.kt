package com.khangle.thecocktaildbapp.alcoholic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.domain.model.Alcoholic
import com.khangle.domain.model.Category
import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.FetchAlcoholicListUseCase
import com.khangle.domain.usecase.FetchDrinkByAlcoholicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AlcoholicViewModel @Inject constructor(
    private val fetchAlcoholicListUseCase: FetchAlcoholicListUseCase,
    private val fetchDrinkByAlcoholicUseCase: FetchDrinkByAlcoholicUseCase
): ViewModel() {
    private val _alcoholicList = MutableLiveData<Resource<List<Alcoholic>>>()
    val category: LiveData<Resource<List<Alcoholic>>> = _alcoholicList
    private val _drinks = MutableLiveData<Resource<List<FilterResultDrink>>>()
    val drinks: LiveData<Resource<List<FilterResultDrink>>> = _drinks
    private val _selectedCategory = MutableLiveData<String>()
    val selectedCategory: LiveData<String> = _selectedCategory
    fun selecteAlcoholic(position: Int) {
        _selectedCategory.value = _alcoholicList.value?.data?.get(position)?.strAlcoholic
    }

    // avoid reloading when configuration change
    var fetchCategoryFlag = true // user will change to true in future
    fun fetchCategoryList() {
        if (fetchCategoryFlag) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    _alcoholicList.postValue(Resource.Loading())
                    val alcoholicList = fetchAlcoholicListUseCase()
                    _alcoholicList.postValue(Resource.Success(data = alcoholicList))
                } catch (e: Exception) {
                    _alcoholicList.postValue(Resource.Error(e.message!!))
                }
            }
            fetchCategoryFlag = false
        }
    }

    fun fetchDrinkByAlcoholic(alcoholicStr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _drinks.postValue(Resource.Loading())
                val drinks = fetchDrinkByAlcoholicUseCase(alcoholicStr)
                _drinks.postValue(Resource.Success(data = drinks))
            } catch (e: Exception) {
                _drinks.postValue(Resource.Error(e.message!!))
            }
        }
    }
}