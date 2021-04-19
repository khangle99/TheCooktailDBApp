package com.khangle.thecocktaildbapp.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.domain.model.Category
import com.khangle.domain.model.Drink
import com.khangle.domain.model.FilterResultDrink
import com.khangle.domain.model.Resource
import com.khangle.domain.usecase.FetchCategoryListUseCase
import com.khangle.domain.usecase.FetchDrinkByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Error
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val fetchCategoryListUseCase: FetchCategoryListUseCase,
    private val fetchDrinkByCategoryUseCase: FetchDrinkByCategoryUseCase
) : ViewModel() {
    private val _categoryList = MutableLiveData<Resource<List<Category>>>()
    val category: LiveData<Resource<List<Category>>> = _categoryList
    private val _drinks = MutableLiveData<Resource<List<FilterResultDrink>>>()
    val drinks: LiveData<Resource<List<FilterResultDrink>>> = _drinks
    private val _selectedCategory = MutableLiveData<String>()
    val selectedCategory: LiveData<String> = _selectedCategory
    fun selecteCategory(position: Int) {
        _selectedCategory.value = _categoryList.value?.data?.get(position)?.strCategory
    }

    // avoid reloading when configuration change
    var fetchCategoryFlag = true // user will change to true in future
    fun fetchCategoryList() {
        if (fetchCategoryFlag) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    _categoryList.postValue(Resource.Loading())
                    val categoryList = fetchCategoryListUseCase()
                    _categoryList.postValue(Resource.Success(data = categoryList))
                } catch (e: Exception) {
                    _categoryList.postValue(Resource.Error(e.message!!))
                }
            }
            fetchCategoryFlag = false
        }
    }

    fun fetchDrinkByCategory(categoryStr: String) {
        viewModelScope.launch(Dispatchers.IO) {
           try {
               _drinks.postValue(Resource.Loading())
               val drinks = fetchDrinkByCategoryUseCase(categoryStr)
               _drinks.postValue(Resource.Success(data = drinks))
           } catch (e: Exception) {
               _drinks.postValue(Resource.Error(e.message!!))
           }
        }
    }
}