package com.khangle.thecocktaildbapp.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val searchViewModel: SearchViewModel  by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search, container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = DrinkListAdapter { drink ->
            Log.i(TAG, "onViewCreated: ${drink.name}")
            parentFragmentManager.commitAnimate {
                setReorderingAllowed(true)
                val fragmentDetail = CockTailDetailFragment()
                fragmentDetail.arguments = bundleOf("isRequest" to false, "drink" to drink) // dont have to request
                replace(R.id.hostFragment, fragmentDetail)
                addToBackStack(null)
            }

        }
    }
lateinit var adapter: DrinkListAdapter
    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchResultRecycleView.adapter = adapter
        binding.searchResultRecycleView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch{
            binding.searchView.getQueryTextChangeStateFlow()
                .debounce(300)
                .filter { query ->
                    if (query.isEmpty()) {
                        // hien UI khuyen search
                        binding.findViewImg.isVisible = true
                        binding.findViewText.isVisible = true
                        return@filter false
                    } else {
                        binding.findViewImg.isVisible = false
                        binding.findViewText.isVisible = false
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    // truy van mang
                    searchViewModel.query(query)
                }
                .collect { result ->
                    if (result != null) {
                        Log.i(TAG, "onViewCreated:------ ${result}")
                        // bind vao recycleview adapter
                        adapter.submitList(result)
                        binding.notFoundStub.viewStub?.visibility = View.GONE

                    } else {
                        // hien khong co ket qua
                        adapter.submitList(null)
                        binding.notFoundStub.viewStub?.visibility = View.VISIBLE
                        Log.i(TAG, "onViewCreated:------ null")
                    }

                }
        }
    }

}

private const val TAG = "SearchFragment"
