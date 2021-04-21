package com.khangle.thecocktaildbapp.presentation.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.asksira.dropdownview.OnDropDownSelectionListener
import com.khangle.thecocktaildbapp.domain.model.Drink
import com.khangle.thecocktaildbapp.domain.model.Resource
import com.khangle.thecocktaildbapp.presentation.R
import com.khangle.thecocktaildbapp.presentation.cocktailDetail.CockTailDetailFragment
import com.khangle.thecocktaildbapp.presentation.databinding.FragmentCategoryFilterBinding
import com.khangle.thecocktaildbapp.presentation.search.DrinkListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFilterFragment : Fragment() {
    private var _binding: FragmentCategoryFilterBinding? = null
    private val binding get() = _binding!!
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var adapter: DrinkListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel.fetchCategoryList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_filter, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
        initAdapter()
        observeLiveData()
        setEvent()
    }

    private fun setEvent() {
        binding.dropdownview.onSelectionListener =
            OnDropDownSelectionListener { _, position ->
                categoryViewModel.selecteCategory(position)
            }
    }

    private fun initAdapter() {
        adapter = DrinkListAdapter { drink, shareView ->
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                val fragmentDetail = CockTailDetailFragment()
                fragmentDetail.arguments = bundleOf(
                    "isRequest" to true,
                    "id" to drink.id,
                    "shareViewId" to ViewCompat.getTransitionName(shareView)
                ) // request
                addSharedElement(shareView, ViewCompat.getTransitionName(shareView)!!)
                addToBackStack(null)
                replace(R.id.hostFragment, fragmentDetail)
            }
            if (binding.categoryProgressBar.isInflated) {
                binding.categoryProgressBar.root.isVisible = true
            } else {
                binding.categoryProgressBar.viewStub?.isVisible = true
            }
        }
        binding.drinkRecycleView.adapter = adapter
        binding.drinkRecycleView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeLiveData() {
        categoryViewModel.selectedCategory.observe(viewLifecycleOwner, {
            categoryViewModel.fetchDrinkByCategory(it)
        })
        categoryViewModel.category.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.dropdownview.dropDownItemList =
                        resource.data?.map { it.strCategory } ?: emptyList()
                    binding.categoryProgressBar.root?.isVisible = false
                    binding.dropdownview.isVisible = true
                }
                is Resource.Loading -> {
                    binding.categoryProgressBar.viewStub?.isVisible =
                        true // dont need to check inflate
                    binding.dropdownview.isVisible = false
                }
                is Resource.Error -> {
                    binding.categoryProgressBar.root?.isVisible = false
                    binding.dropdownview.isVisible = false
                    // chua code UI error
                }
            }
        })

        categoryViewModel.drinks.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    binding.categoryProgressBar.root?.isVisible =
                        true // dont need to check inflated
                }
                is Resource.Error -> {
                    binding.categoryProgressBar.root?.isVisible = false
                    // chua hien UI loi
                }
                is Resource.Success -> {
                    // bind to adapter
                    adapter.submitList(it.data?.map { filterDrink ->
                        Drink().apply {
                            name = filterDrink.name
                            id = filterDrink.id
                            thumbUrl = filterDrink.thumbUrl
                        }
                    })
                    binding.categoryProgressBar.root?.isVisible = false
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}