package com.khangle.thecocktaildbapp.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.khangle.thecocktaildbapp.R

@BindingAdapter("image_url")
fun setImageUrl(view: ImageView, strUrl: String?) {
    strUrl?.let {
        view.load(strUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_app)
                .error(R.drawable.ic_broken_image)
                .transformations(RoundedCornersTransformation(20f))
                .scale(Scale.FILL)
        }
    }
}

