package com.khangle.thecocktaildbapp.cocktailDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.khangle.thecocktaildbapp.R
import com.khangle.thecocktaildbapp.databinding.ItemIngredientBinding

class IngredientAdapter(private var ingredients: List<String>, private var ingredientMeasure: List<String>): RecyclerView.Adapter<IngredientViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        // when dont have measure for a ingredient
        var measure = ""
        if (position < ingredientMeasure.size) {
            measure = ingredientMeasure[position]
        }
        holder.bind(ingredient,measure)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }
    fun setData(ingredients: List<String>, ingredientMeasure: List<String>) {
        this.ingredients = ingredients
        this.ingredientMeasure = ingredientMeasure
        notifyDataSetChanged()
    }
}

class IngredientViewHolder(private val binding: ItemIngredientBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(ingredient: String,measure: String) {
        binding.ingredientStr = ingredient
        binding.measureStr = measure
    }
companion object {
    fun create(parent: ViewGroup): IngredientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemIngredientBinding>(inflater, R.layout.item_ingredient, parent,false)
        return IngredientViewHolder(binding)
    }
}
}