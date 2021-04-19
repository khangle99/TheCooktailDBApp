package com.khangle.thecocktaildbapp.home

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.khangle.thecocktaildbapp.R
import com.khangle.thecocktaildbapp.alcoholic.AlcoholicFilterFragment
import com.khangle.thecocktaildbapp.category.CategoryFilterFragment
import com.khangle.thecocktaildbapp.cocktailDetail.CockTailDetailFragment
import com.khangle.thecocktaildbapp.databinding.FragmentHomeBinding
import com.khangle.thecocktaildbapp.extensions.commitAnimate
import com.khangle.thecocktaildbapp.search.SearchFragment
import com.rbddevs.splashy.Splashy
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setSlashy()
        super.onCreate(savedInstanceState)
        homeViewModel.refresh()
    }

    private fun setSlashy() {
        Splashy(requireActivity())
            .setLogo(R.drawable.ic_app)
            .setTitle("Cocktail")
            .setTitleSize(30.0f)
            .setTitleColor("#FFFFFF")
            .setSubTitle("The Cocktail DB App")
            .setProgressColor(R.color.white)
            .setAnimation(Splashy.Animation.SLIDE_IN_TOP_BOTTOM,500)
            .setFullScreen(true)
            .setDuration(2000)
            .show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        homeViewModel.randomDrink.observe(viewLifecycleOwner, Observer {
            binding.drink = it
            binding.executePendingBindings()
            (view.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        })

        homeViewModel.randomQuote.observe(viewLifecycleOwner, Observer {
            binding.quote = it
            binding.executePendingBindings()
        })

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

                fragmentDetail.arguments = bundleOf(
                    "isRequest" to false,
                    "drink" to homeViewModel.randomDrink.value,
                    "shareViewId" to "thumb"
                )
                ViewCompat.setTransitionName(binding.compatDrink.thumbItem, "thumb")
                addSharedElement(binding.compatDrink.thumbItem, "thumb")
                addToBackStack(null)
                replace(R.id.hostFragment, fragmentDetail)
            }
        }
    }


}