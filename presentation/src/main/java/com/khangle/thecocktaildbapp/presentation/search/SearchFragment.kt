package com.khangle.thecocktaildbapp.presentation.search

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.khangle.thecocktaildbapp.domain.model.Resource
import com.khangle.thecocktaildbapp.presentation.R
import com.khangle.thecocktaildbapp.presentation.cocktailDetail.CockTailDetailFragment
import com.khangle.thecocktaildbapp.presentation.databinding.FragmentSearchBinding
import com.khangle.thecocktaildbapp.presentation.extensions.getQueryTextChangeStateFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var adapter: DrinkListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
        observeLiveData()
        setupRecycleView()
        setupInstantSearch()
    }

    private fun observeLiveData() {
        searchViewModel.drinks.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.searchProgress.root?.visibility = View.GONE
                    adapter.submitList(it.data)
                    binding.notFoundStub.root?.visibility = View.GONE
                }

                is Resource.Loading -> {
                    if (binding.searchProgress.isInflated) {
                        binding.searchProgress.root.visibility = View.VISIBLE
                    } else {
                        binding.searchProgress.viewStub?.visibility = View.VISIBLE
                    }
                }

                is Resource.Error -> {
                    if (binding.searchProgress.isInflated) {
                        binding.searchProgress.root.visibility = View.GONE
                    } else {
                        binding.searchProgress.viewStub?.visibility = View.GONE
                    }
                    binding
                    if (it.throwable is Resources.NotFoundException) {
                        adapter.submitList(emptyList())
                        if (binding.notFoundStub.isInflated) {
                            binding.notFoundStub.root.visibility = View.VISIBLE
                        } else {
                            binding.notFoundStub.viewStub?.visibility = View.VISIBLE
                        }

                    }
                    // chua code UI error
                }
            }
        })
    }

    private fun setupRecycleView() {
        adapter = DrinkListAdapter { drink, shareview ->
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                val fragmentDetail = CockTailDetailFragment()
                fragmentDetail.arguments = bundleOf(
                    "isRequest" to false,
                    "drink" to drink,
                    "shareViewId" to ViewCompat.getTransitionName(shareview)
                ) // dont have to request
                addSharedElement(shareview, ViewCompat.getTransitionName(shareview)!!)
                addToBackStack(null)
                replace(R.id.hostFragment, fragmentDetail)
            }
        }
        binding.searchResultRecycleView.adapter = adapter
        binding.searchResultRecycleView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupInstantSearch() {
        lifecycleScope.launch {
            binding.searchView.getQueryTextChangeStateFlow()
                .debounce(300)
                .filter { query ->
                    if (query.isEmpty()) {
                        // hien UI khuyen search
                        // binding.findViewImg.isVisible = true
                        //  binding.findViewText.isVisible = true
                        return@filter false
                    } else {
                        binding.findViewImg.isVisible = false
                        binding.findViewText.isVisible = false
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .collect {
                    searchViewModel.queryStr(it)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
