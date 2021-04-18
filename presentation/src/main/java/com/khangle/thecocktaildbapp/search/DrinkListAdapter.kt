package com.khangle.thecocktaildbapp.search


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.domain.model.Drink
import com.khangle.thecocktaildbapp.R
import com.khangle.thecocktaildbapp.databinding.ItemDrinkBinding

class DrinkListAdapter(private val onClick: (Drink) -> Unit) :
    ListAdapter<Drink, DrinkViewHolder>(DrinkItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        return DrinkViewHolder.create(parent,onClick)
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DrinkViewHolder(
    private val binding: ItemDrinkBinding
) : RecyclerView.ViewHolder(binding.root) {
    lateinit var drink: Drink
    fun bind(drink: Drink) {
        this.drink = drink
        binding.drink = drink
        binding.executePendingBindings() // quan trong
    }

    companion object {
        fun create(parent: ViewGroup, onClick: (Drink) -> Unit): DrinkViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemDrinkBinding.inflate(inflater, parent, false)
            return DrinkViewHolder(binding).apply { itemView.setOnClickListener { onClick(drink) } }
        }
    }
}

object DrinkItemCallback : DiffUtil.ItemCallback<Drink>() {
    override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
        return oldItem.id == newItem.id
    }

}