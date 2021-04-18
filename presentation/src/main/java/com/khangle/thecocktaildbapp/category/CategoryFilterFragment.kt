package com.khangle.thecocktaildbapp.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.asksira.dropdownview.OnDropDownSelectionListener
import com.khangle.domain.model.Resource
import com.khangle.thecocktaildbapp.R
import com.khangle.thecocktaildbapp.databinding.FragmentCategoryFilterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFilterFragment : Fragment() {
    private lateinit var binding: FragmentCategoryFilterBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var adapter: FilterResultAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel.fetchCategoryList()
        adapter = FilterResultAdapter { drink, shareView ->

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_filter, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.drinkRecycleView.adapter = adapter
        binding.drinkRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.dropdownview.onSelectionListener = OnDropDownSelectionListener { dropDownView, position ->
            categoryViewModel.selecteCategory(position)
        }
        categoryViewModel.category.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.dropdownview.dropDownItemList =
                        it.data?.map { it.strCategory } ?: emptyList()
                    binding.categoryProgressBar.root?.isVisible = false
                    binding.dropdownview.isVisible = true
                }
                is Resource.Loading -> {
                    // hien xoay, disable dropdown
                    binding.categoryProgressBar.viewStub?.isVisible = true
                    binding.dropdownview.isVisible = false
                }
                is Resource.Error -> {
                    binding.categoryProgressBar.root?.isVisible = false
                    binding.dropdownview.isVisible = false
                    // chua code UI error
                }
            }
        })

        categoryViewModel.drinks.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Resource.Loading -> {
                    binding.categoryProgressBar.root?.isVisible = true
                }
                is Resource.Error -> {
                    binding.categoryProgressBar.root?.isVisible = false
                    // chua hien UI loi
                }
                is Resource.Success -> {
                    // bind to adapter
                    adapter.submitList(it.data)
                    binding.categoryProgressBar.root?.isVisible = false

                }
            }
        })



        categoryViewModel.selectedCategory.observe(viewLifecycleOwner, Observer {
            categoryViewModel.fetchDrinkByCategory(it) // chua co logic load khi can, tu chay lai khi xoay or back
        })


    }

}