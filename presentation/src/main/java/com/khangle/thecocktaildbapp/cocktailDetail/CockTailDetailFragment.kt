package com.khangle.thecocktaildbapp.cocktailDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.khangle.domain.model.Resource
import com.khangle.thecocktaildbapp.R
import com.khangle.thecocktaildbapp.databinding.FragmentCockTailDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CockTailDetailFragment : Fragment() {
    private val cockTailDetailViewModel: CockTailDetailViewModel by viewModels()
    private var _binding: FragmentCockTailDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: IngredientAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)
        loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cock_tail_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        ViewCompat.setTransitionName(
            binding.thumbImageView,
            requireArguments().getString("shareViewId")
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        setUpRecycleView()
        observeLiveData()
    }

    private fun loadData() {
        val isRequest = requireArguments().getBoolean("isRequest", true)
        if (!isRequest) {
            cockTailDetailViewModel.setDrink(requireArguments().getParcelable("drink")!!)
        } else {
            cockTailDetailViewModel.fetchDrink(requireArguments().getString("id")!!)
        }
    }

    private fun setUpRecycleView() {
        adapter = IngredientAdapter(listOf(), listOf())
        binding.ingredientRecycleView.isNestedScrollingEnabled = false
        binding.ingredientRecycleView.adapter = adapter
        binding.ingredientRecycleView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeLiveData() {
        cockTailDetailViewModel.drink.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                    binding.drink = it.data
                    adapter.setData(
                        it.data?.ingredients ?: emptyList(),
                        it.data?.ingredientsMeasure ?: emptyList()
                    )
                    binding.executePendingBindings()
                    (requireView().parent as? ViewGroup)?.doOnPreDraw {
                        startPostponedEnterTransition()
                    }
                }
                is Resource.Error -> {

                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}