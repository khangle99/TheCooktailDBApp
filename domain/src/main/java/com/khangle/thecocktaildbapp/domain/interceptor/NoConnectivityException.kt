package com.khangle.thecocktaildbapp.domain.interceptor

import java.lang.Exception

class NoConnectivityException: Exception() {
    override val message: String
        get() = "No connectivity"
}