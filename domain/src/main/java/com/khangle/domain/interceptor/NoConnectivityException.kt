package com.khangle.domain.interceptor

import java.lang.Exception

class NoConnectivityException: Exception() {
    override val message: String
        get() = "No connectivity"
}