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
        //startAnimation()
        startCountdown()
    }

    fun startCountdown()
    {

        if(sharePrefs.userName == "")
        {
            Thread(Runnable {

                Thread.sleep(1000)
                var i = Intent(this, IntroScreen::class.java)
                startActivity(i)
                finish()

            }).start()
        }
        else
        {
            Thread(Runnable {

                Thread.sleep(1000)
                var i = Intent(this, MainActivity::class.java)
                sharePrefs.setNewAppInitialStart(false)
                startActivity(i)
                finish()

            }).start()
        }

    }
}
