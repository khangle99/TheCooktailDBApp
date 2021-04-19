package com.khangle.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Quote(
    @PrimaryKey val text: String,
    val author: String,
    @ColumnInfo(name = "created_at") var createdAt: Long = 0
)

class QuoteResponse(val quotes: List<Quote>)