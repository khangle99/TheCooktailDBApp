package com.khangle.thecocktaildbapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.domain.model.Drink
import com.khangle.domain.model.Quote
import com.khangle.domain.repository.TheCockTailDBRepository
import com.khangle.domain.usecase.GetRandomDrinkUseCase
import com.khangle.domain.usecase.GetRandomQuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val randomDrinkUseCase: GetRandomDrinkUseCase,
    private val randomQuoteUseCase: GetRandomQuoteUseCase,
    private val theCockTailDBRepository: TheCockTailDBRepository
) : ViewModel() {
    private val _randomDrink = MutableLiveData<Drink>()
    val randomDrink: LiveData<Drink> = _randomDrink
    private val _randomQuote = MutableLiveData<Quote>()
    val randomQuote: LiveData<Quote> = _randomQuote
    private val _loadComplete = MutableLiveData<Boolean>()
    val loadComplete: LiveData<Boolean> = _loadComplete
    var refreshFlag = true
    fun refresh() {
        if (refreshFlag) {
            viewModelScope.launch(Dispatchers.IO) {
                val quote = async { randomQuoteUseCase() }
                val drink = async { randomDrinkUseCase() }
                _randomDrink.postValue(drink.await())
                _randomQuote.postValue(quote.await())
                _loadComplete.postValue(true)
                refreshFlag = !refreshFlag
            }
        }

    }


}