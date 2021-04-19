package com.khangle.thecocktaildbapp.cocktailDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.domain.model.Drink
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.FetchDrinkByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CockTailDetailViewModel @Inject constructor(private val fetchDrinkByIdUseCase: FetchDrinkByIdUseCase): ViewModel() {
     private val _drink = MutableLiveData<Resource<Drink>>()
     var drink: LiveData<Resource<Drink>> = _drink
     fun getDrink(drinkId: String) {
          viewModelScope.launch(Dispatchers.IO) {
               try {
                    _drink.postValue(Resource.Loading())
                    val drink = fetchDrinkByIdUseCase(drinkId)
                    _drink.postValue(Resource.Success(data = drink))
               } catch (e: Exception) {
                    _drink.postValue(Resource.Error(e.message!!))
               }
          }

     }

     fun setDrink(drink: Drink) {
          _drink.value = Resource.Success(data = drink)
     }
}