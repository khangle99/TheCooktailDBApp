package com.khangle.thecocktaildbapp.presentation.extensions

import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {

    val query = MutableStateFlow("")

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            this@getQueryTextChangeStateFlow.clearFocus()
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            query.value = newText
            return true
        }
    })

    return query

}