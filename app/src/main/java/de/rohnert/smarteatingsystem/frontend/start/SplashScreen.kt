package de.rohnert.smarteatingsystem.frontend.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.rohnert.smarteatingsystem.MainActivity
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences

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
