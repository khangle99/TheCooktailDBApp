package com.khangle.thecocktaildbapp.search

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.AbsListView.VISIBLE
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.TransitionInflater
import com.khangle.domain.model.Drink
import com.khangle.domain.model.Resource
import com.khangle.thecocktaildbapp.R
import com.khangle.thecocktaildbapp.cocktailDetail.CockTailDetailFragment
import com.khangle.thecocktaildbapp.databinding.FragmentSearchBinding
import com.khangle.thecocktaildbapp.extensions.commitAnimate
import com.khangle.thecocktaildbapp.extensions.getQueryTextChangeStateFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        return binding.root
    }

    var testCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = DrinkListAdapter { drink, shareview ->
            Log.i(TAG, "onViewCreated: ${drink.name}")
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

    }

    lateinit var adapter: DrinkListAdapter

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
        searchViewModel.drinks.observe(viewLifecycleOwner, Observer {
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
                    binding.searchProgress.root.visibility = View.GONE
                    binding
                    if (it.message == "Not found") {
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
        binding.searchResultRecycleView.adapter = adapter
        binding.searchResultRecycleView.layoutManager = LinearLayoutManager(requireContext())
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

}

private const val TAG = "SearchFragment"
