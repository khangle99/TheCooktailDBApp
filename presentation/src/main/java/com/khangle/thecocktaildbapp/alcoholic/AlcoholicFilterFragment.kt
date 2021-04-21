package com.khangle.thecocktaildbapp.alcoholic

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
import com.khangle.domain.model.Drink
import com.khangle.domain.model.Resource
import com.khangle.thecocktaildbapp.R
import com.khangle.thecocktaildbapp.cocktailDetail.CockTailDetailFragment
import com.khangle.thecocktaildbapp.databinding.FragmentAlcoholicFilterBinding
import com.khangle.thecocktaildbapp.search.DrinkListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlcoholicFilterFragment : Fragment() {

    private var _binding: FragmentAlcoholicFilterBinding? = null
    private val binding get() = _binding!!
    private val alcoholicViewModel: AlcoholicViewModel by viewModels()
    private lateinit var adapter: DrinkListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alcoholicViewModel.fetchAlcoholicList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_alcoholic_filter, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
        binding.dropdownview.onSelectionListener =
            OnDropDownSelectionListener { _, position ->
                alcoholicViewModel.selectedAlcoholic(position)
            }
        initAdapter()
        observeLiveData()
    }

    private fun observeLiveData() {
        alcoholicViewModel.selectedAlcoholic.observe(viewLifecycleOwner, {
            alcoholicViewModel.fetchDrinkByAlcoholic(it)
        })
        alcoholicViewModel.alcoholicList.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.dropdownview.dropDownItemList =
                        resource.data?.map { it.strAlcoholic } ?: emptyList()
                    binding.alcoholicProgress.root?.isVisible = false
                    binding.dropdownview.isVisible = true
                }
                is Resource.Loading -> {
                    binding.alcoholicProgress.viewStub?.isVisible =
                        true // dont need to check inflate
                    binding.dropdownview.isVisible = false
                }
                is Resource.Error -> {
                    binding.alcoholicProgress.root?.isVisible = false
                    binding.dropdownview.isVisible = false
                    // chua code UI error
                }
            }
        })

        alcoholicViewModel.drinks.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    binding.alcoholicProgress.root?.isVisible =
                        true // dont need to check inflated
                }
                is Resource.Error -> {
                    binding.alcoholicProgress.root?.isVisible = false
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
                    binding.alcoholicProgress.root?.isVisible = false

                }
            }
        })
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
            if (binding.alcoholicProgress.isInflated) {
                binding.alcoholicProgress.root.isVisible = true
            } else {
                binding.alcoholicProgress.viewStub?.isVisible = true
            }
        }
        binding.drinkRecycleView.adapter = adapter
        binding.drinkRecycleView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}