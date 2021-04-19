package com.khangle.thecocktaildbapp.home

import androidx.lifecycle.*
import com.khangle.domain.model.Drink
import com.khangle.domain.model.Quote
import com.khangle.domain.model.Resource
import com.khangle.domain.repository.TheCockTailDBRepository
import com.khangle.domain.usecase.GetRandomDrinkUseCase
import com.khangle.domain.usecase.GetRandomQuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DeferredResultUnused")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val randomDrinkUseCase: GetRandomDrinkUseCase,
    private val randomQuoteUseCase: GetRandomQuoteUseCase
) : ViewModel() {
    private val _randomDrink = MutableLiveData<Resource<List<Drink>>>()
    val randomDrink: LiveData<Resource<List<Drink>>> = _randomDrink
    private val _randomQuote = MutableLiveData<Resource<List<Quote>>>()
    val randomQuote: LiveData<Resource<List<Quote>>> = _randomQuote

    var refreshFlag = true // avoid configuration change
    fun refresh(forceRefresh: Boolean = false) {
        if (refreshFlag || forceRefresh) {
            viewModelScope.launch(Dispatchers.IO) {
                async {
                    randomDrinkUseCase(forceRefresh).collect {
                        _randomDrink.postValue(it)
                    }
                }
                async {
                    randomQuoteUseCase(forceRefresh).collect {
                        _randomQuote.postValue(it)
                    }
                }
                refreshFlag = false
            }
        }
    }
}