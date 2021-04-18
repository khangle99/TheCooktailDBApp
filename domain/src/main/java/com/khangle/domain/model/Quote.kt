package com.khangle.domain.model

class Quote(val text: String, val author: String)
class QuoteResponse(val quotes: List<Quote>)