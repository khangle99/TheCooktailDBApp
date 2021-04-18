package com.khangle.thecocktaildbapp.cocktailDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.domain.model.Drink
import com.khangle.domain.usecase.FetchDrinkByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CockTailDetailViewModel @Inject constructor(private val fetchDrinkByIdUseCase: FetchDrinkByIdUseCase): ViewModel() {
     private val _drink = MutableLiveData<Drink>()
     var drink: LiveData<Drink> = _drink
     fun getDrink(drinkId: String) {
          viewModelScope.launch(Dispatchers.IO) {
               val drink = fetchDrinkByIdUseCase(drinkId)
               _drink.postValue(drink)
          }

     }

     fun setDrink(drink: Drink) {
          _drink.value = drink
     }
}