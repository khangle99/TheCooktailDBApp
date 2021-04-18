package com.khangle.thecocktaildbapp.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.khangle.thecocktaildbapp.R
import com.khangle.thecocktaildbapp.databinding.FragmentCategoryFilterBinding


class CategoryFilterFragment : Fragment() {
    private lateinit var binding: FragmentCategoryFilterBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_category_filter,container,false)
        return binding.root
    }


}