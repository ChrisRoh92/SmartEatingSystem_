package de.rohnert.smeasy.frontend.start

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import de.rohnert.smeasy.MainActivity
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences

class SplashScreen : AppCompatActivity() {

    private lateinit var sharePrefs: SharedAppPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharePrefs = SharedAppPreferences(this)
        startAnimation()
    }

    private fun startAnimation()
    {
        var icon: ImageView = findViewById(R.id.intro_logo)
        var title: TextView = findViewById(R.id.intro_tv)





        var alpha = PropertyValuesHolder.ofFloat(View.ALPHA,0f,1f)
        var scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,1f,1.2f)
        var scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,1f,1.2f)
        var transXIcon = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,0f,-50f)
        var transXTitle = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,0f,40f)

        var iconAnimation = ObjectAnimator.ofPropertyValuesHolder(icon,transXIcon).apply {
            duration = 350

            interpolator = FastOutSlowInInterpolator()
        }

        var titleAnimation = ObjectAnimator.ofPropertyValuesHolder(title,transXTitle,alpha,scaleX,scaleY).apply {
            duration = 350
            interpolator = FastOutSlowInInterpolator()
        }

        var set = AnimatorSet()
        //set.playSequentially(iconAnimation,titleAnimation)
        set.playTogether(iconAnimation,titleAnimation)
        set.startDelay = 500
        set.duration = 350
        set.start()
        set.addListener(object: Animator.AnimatorListener
        {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                startCountdown()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }

        })


    }

    fun startCountdown()
    {

        if(sharePrefs.userName == "")
        {
            Thread(Runnable {

                Thread.sleep(500)
                var i = Intent(this, IntroScreen::class.java)
                startActivity(i)
                finish()

            }).start()
        }
        else
        {
            Thread(Runnable {

                Thread.sleep(500)
                var i = Intent(this, MainActivity::class.java)
                sharePrefs.setNewAppInitialStart(false)
                startActivity(i)
                finish()

            }).start()
        }

    }
}
