package com.khangle.thecocktaildbapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.khangle.thecocktaildbapp.home.HomeFragment
import com.rbddevs.splashy.Splashy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
   // private val mainViewModel: MainViewModel by viewModels()
    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       if(savedInstanceState == null) {
           setSlashy()
       }
        setContentView(R.layout.activity_main)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.hostFragment, HomeFragment())
        }
    }

    private fun setSlashy() {
        Splashy(this)
            .setLogo(R.drawable.ic_app)
            .setTitle("Cocktail")
            .setTitleSize(30.0f)
            .setTitleColor("#FFFFFF")
            .setBackgroundColor(R.color.backgroundSplash)
            .setSubTitle("The Cocktail DB App")
            .setSubTitleColor(R.color.textOnSplash)
            .setSubTitleItalic(true)
            .setProgressColor(R.color.white)
            .setAnimation(Splashy.Animation.SLIDE_IN_TOP_BOTTOM, 1000)
            .setFullScreen(true)
            .setDuration(2000)
            .show()
    }
}