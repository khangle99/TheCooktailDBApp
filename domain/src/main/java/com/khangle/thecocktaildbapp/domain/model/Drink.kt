package com.khangle.thecocktaildbapp.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
class Drink(
    @PrimaryKey var id: String = "",
    var name: String = "",
    var category: String = "",
    var alcoholic: String = "",
    var instructions: String = "",
    var thumbUrl: String = "",
    @ColumnInfo(name = "created_at") var createdAt: Long = 0,
    val ingredients: MutableList<String> = mutableListOf(),
    val ingredientsMeasure: MutableList<String> = mutableListOf()
) : Parcelable

class DrinkTypeAdapter : TypeAdapter<Drink>() {
    override fun write(out: JsonWriter?, value: Drink?) {
    }

    override fun read(`in`: JsonReader?): Drink {
        val drink = Drink()
        `in`?.let { reader ->
            reader.beginObject()
            var fieldname = ""
            while (reader.hasNext()) {
                var token: JsonToken = reader.peek()
                if (token == JsonToken.NAME) {
                    fieldname = reader.nextName()
                }
                when {
                    fieldname == "idDrink" -> {
                        drink.id = reader.nextString()
                    }
                    fieldname == "strDrink" -> {
                        drink.name = reader.nextString()
                    }
                    fieldname == "strCategory" -> {
                        drink.category = reader.nextString()
                    }
                    fieldname == "strAlcoholic" -> {
                        drink.alcoholic = reader.nextString()
                    }
                    fieldname == "strInstructions" -> {
                        drink.instructions = reader.nextString()
                    }
                    fieldname == "strDrinkThumb" -> {
                        drink.thumbUrl = reader.nextString()
                    }
                    fieldname.startsWith("strIngredient") -> {
                        token = reader.peek()
                        if (token != JsonToken.NULL) {
                            drink.ingredients.add(reader.nextString())
                        } else {
                            reader.skipValue()
                        }

                    }
                    fieldname.startsWith("strMeasure") -> {
                        token = reader.peek()
                        if (token != JsonToken.NULL) {
                            drink.ingredientsMeasure.add(reader.nextString())
                        } else {
                            reader.skipValue()
                        }
                    }
                    else -> reader.skipValue()
                }

            }
            reader.endObject()
        }

        return drink
    }

}

class DrinkDetailResponse(val drinks: List<Drink>)