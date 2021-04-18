package com.khangle.thecocktaildbapp.alcoholic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.khangle.thecocktaildbapp.R
import com.khangle.thecocktaildbapp.databinding.FragmentAlcoholicFilterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlcoholicFilterFragment : Fragment() {
    private lateinit var binding: FragmentAlcoholicFilterBinding
    private val alcoholicViewModel: AlcoholicViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_alcoholic_filter,container,false)
        return binding.root
    }


}