package com.khangle.thecocktaildbapp.category

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.domain.model.Drink
import com.khangle.domain.model.FilterResultDrink
import com.khangle.thecocktaildbapp.databinding.ItemDrinkBinding
import com.khangle.thecocktaildbapp.search.DrinkViewHolder

class FilterResultAdapter(val onClick: (Drink, View) -> Unit) :
    ListAdapter<FilterResultDrink, DrinkViewHolder>(FilterResultItemCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        return DrinkViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        val filterResultDrink = getItem(position)
        val drink = Drink().apply {
            name = filterResultDrink.name
            id = filterResultDrink.id
            thumbUrl = filterResultDrink.thumbUrl
        }
        holder.bind(drink)
    }

}

object FilterResultItemCallBack : DiffUtil.ItemCallback<FilterResultDrink>() {
    override fun areItemsTheSame(oldItem: FilterResultDrink, newItem: FilterResultDrink): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FilterResultDrink,
        newItem: FilterResultDrink
    ): Boolean {
        return oldItem.id == newItem.id
    }

}

