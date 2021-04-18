package com.khangle.thecocktaildbapp.binding

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.khangle.thecocktaildbapp.R

@BindingAdapter("image_url")
fun setImageUrl(view: ImageView, strUrl: String?) {
    strUrl?.let {
        view.load(strUrl) {
            //  crossfade(true)
            crossfade(500)
            placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_broken_image)
                .transformations(RoundedCornersTransformation(20f))
        }
    }
}

