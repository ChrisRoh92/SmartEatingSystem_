package de.rohnert.smarteatingsystem.ui.start

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Property
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import de.rohnert.smarteatingsystem.MainActivity
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.sharedpreferences.SharedAppPreferences

class SplashScreen : AppCompatActivity() {

    // Allgemeine Sachen
    private lateinit var sharePrefs: SharedAppPreferences
    private val animDuration = 300L

    // View Elemente:
    private lateinit var icon:ImageView
    private lateinit var tvMain:TextView
    private lateinit var tvSub:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharePrefs = SharedAppPreferences(this)

        // Init View Elements:
        icon = findViewById(R.id.splash_icon)
        tvMain = findViewById(R.id.splash_tv_main)
        tvSub = findViewById(R.id.splash_tv_sub)

        // Set Alpha and TranslationY:
        icon.alpha = 0f
        icon.translationY = 50f
        icon.scaleX = 0.8f
        icon.scaleY = 0.8f


        tvMain.alpha = 0f
        tvMain.translationY = 50f

        tvSub.alpha = 0f
        tvSub.translationY = 50f


        startAnimation()

    }


    private fun startAnimation()
    {
        val iconSet = AnimatorSet().apply {
            play(createObjectAnimator(icon,View.ALPHA,1f))
                .with(createObjectAnimator(icon,View.TRANSLATION_Y,0f))
                .with(createObjectAnimator(icon,View.SCALE_X,1f))
                .with(createObjectAnimator(icon,View.SCALE_Y,1f))
        }

        val textSet = AnimatorSet().apply {
            play(createObjectAnimator(tvMain,View.ALPHA,1f,0))
                .with(createObjectAnimator(tvMain,View.TRANSLATION_Y,0f,0))
                .with(createObjectAnimator(tvSub,View.ALPHA,1f,0))
                .with(createObjectAnimator(tvSub,View.TRANSLATION_Y,0f,0))
        }

        val totalSet = AnimatorSet().apply {
            play(iconSet).before(textSet)
            start()
        }

        totalSet.addListener(object:Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
               Thread(Runnable {
                   Thread.sleep(100)
                   navigateToNextDestination()
               }).start()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })



    }

    // Methode navigiert zur nächsten Destination
    private fun navigateToNextDestination()
    {
        if(sharePrefs.userName == "")
        {
            var i = Intent(this, IntroScreen::class.java)
            startActivity(i)
            finish()
        }
        else
        {
            var i = Intent(this, MainActivity::class.java)
            sharePrefs.setNewAppInitialStart(false)
            startActivity(i)
            finish()
        }
    }

    // Methode erstellt verschiedene ObjectAnimators...
    private fun createObjectAnimator(view: View, type: Property<View, Float>,value:Float, delay:Long = 500):ObjectAnimator
    {
        val animator = ObjectAnimator.ofFloat(view,type,value).apply {
            startDelay = delay
            duration = animDuration
            interpolator = FastOutSlowInInterpolator()
        }

        return animator
    }




}
