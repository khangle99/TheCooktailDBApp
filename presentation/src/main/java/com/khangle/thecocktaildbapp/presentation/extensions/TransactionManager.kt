package com.khangle.thecocktaildbapp.presentation.extensions

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.khangle.thecocktaildbapp.presentation.R

inline fun FragmentManager.commitAnimate(
    allowStateLoss: Boolean = false,
    body: FragmentTransaction.() -> Unit
) {
    val transaction = beginTransaction()
    transaction.setCustomAnimations(R.anim.slide_in,R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
    transaction.body()
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}