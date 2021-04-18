package com.khangle.thecocktaildbapp.cocktailDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.khangle.domain.model.Drink
import com.khangle.thecocktaildbapp.R
import com.khangle.thecocktaildbapp.databinding.FragmentCockTailDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CockTailDetailFragment : Fragment() {
    private val cockTailDetailViewModel: CockTailDetailViewModel by viewModels()
    private lateinit var binding: FragmentCockTailDetailBinding
    private lateinit var adapter: IngredientAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)

        val isRequest = requireArguments().getBoolean("isRequest", true)
        if (!isRequest) {
            cockTailDetailViewModel.setDrink(requireArguments().getParcelable("drink")!!)
        } else {
            cockTailDetailViewModel.getDrink(requireArguments().getString("id")!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cock_tail_detail, container, false)
        ViewCompat.setTransitionName(binding.thumbImageView, requireArguments().getString("shareViewId"))
        binding.ingredientRecycleView.isNestedScrollingEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        adapter = IngredientAdapter(listOf(), listOf())
        binding.ingredientRecycleView.adapter = adapter
        binding.ingredientRecycleView.layoutManager = LinearLayoutManager(requireContext())
        cockTailDetailViewModel.drink.observe(viewLifecycleOwner, Observer {
            binding.drink = it
            adapter.setData(it.ingredients, it.ingredientsMeasure)
            binding.executePendingBindings()
        })

        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }


    }


}