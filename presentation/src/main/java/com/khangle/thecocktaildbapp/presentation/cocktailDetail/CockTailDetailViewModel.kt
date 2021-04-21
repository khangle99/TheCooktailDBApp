package com.khangle.thecocktaildbapp.presentation.cocktailDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.thecocktaildbapp.domain.model.Drink
import com.khangle.thecocktaildbapp.domain.model.Resource
import com.khangle.thecocktaildbapp.domain.usecase.FetchDrinkByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CockTailDetailViewModel constructor(
    private val dispatcher: CoroutineDispatcher,
    private val fetchDrinkByIdUseCase: FetchDrinkByIdUseCase
) :
    ViewModel() {
    @Inject
    constructor(fetchDrinkByIdUseCase: FetchDrinkByIdUseCase) : this(
        Dispatchers.IO,
        fetchDrinkByIdUseCase
    )

    private val _drink = MutableLiveData<Resource<Drink>>()
    var drink: LiveData<Resource<Drink>> = _drink
    private var isLoaded = false
    fun fetchDrink(drinkId: String) {
        if (!isLoaded) {
            isLoaded = true
            viewModelScope.launch(dispatcher) {
                try {
                    _drink.postValue(Resource.Loading())
                    val drink = fetchDrinkByIdUseCase(drinkId)
                    _drink.postValue(Resource.Success(data = drink))
                } catch (e: Exception) {
                    _drink.postValue(Resource.Error(e))
                }
            }
        }
    }

    fun setDrink(drink: Drink) {
        _drink.value = Resource.Success(data = drink)
    }
}