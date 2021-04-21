package com.khangle.thecocktaildbapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.khangle.domain.model.Resource
import com.khangle.thecocktaildbapp.R
import com.khangle.thecocktaildbapp.alcoholic.AlcoholicFilterFragment
import com.khangle.thecocktaildbapp.category.CategoryFilterFragment
import com.khangle.thecocktaildbapp.cocktailDetail.CockTailDetailFragment
import com.khangle.thecocktaildbapp.databinding.FragmentHomeBinding
import com.khangle.thecocktaildbapp.extensions.commitAnimate
import com.khangle.thecocktaildbapp.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.refresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        setEvent()
        observeLiveData()
    }

    private fun setEvent() {
        binding.swiperefresh.setOnRefreshListener {
            homeViewModel.refresh(forceRefresh = true)
        }
        binding.categoryNavigate.setOnClickListener {
            parentFragmentManager.commitAnimate {
                setReorderingAllowed(true)
                replace<CategoryFilterFragment>(R.id.hostFragment)
                addToBackStack(null)
            }
        }
        binding.searchNavigate.setOnClickListener {
            parentFragmentManager.commitAnimate {
                setReorderingAllowed(true)
                replace<SearchFragment>(R.id.hostFragment)
                addToBackStack(null)
            }
        }
        binding.alcoholicNavigate.setOnClickListener {
            parentFragmentManager.commitAnimate {
                setReorderingAllowed(true)
                replace<AlcoholicFilterFragment>(R.id.hostFragment)
                addToBackStack(null)
            }
        }

        binding.compatDrink.thumbItem.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                val fragmentDetail = CockTailDetailFragment()
                val drink = homeViewModel.randomDrink.value?.data?.get(0)
                fragmentDetail.arguments = bundleOf(
                    "isRequest" to false,
                    "drink" to drink,
                    "shareViewId" to "thumb"
                )
                addSharedElement(binding.compatDrink.thumbItem, "thumb")
                addToBackStack(null)
                replace(R.id.hostFragment, fragmentDetail)
            }
        }
    }

    private fun observeLiveData() {
        homeViewModel.randomDrink.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.drink = it.data?.get(0)
                    binding.executePendingBindings()
                }
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                }
            }
            binding.swiperefresh.isRefreshing = false // turn off after done
            (requireView().parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        })

        homeViewModel.randomQuote.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.quote = it.data?.get(0)
                    binding.executePendingBindings()
                }
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                }
            }
            binding.executePendingBindings()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}