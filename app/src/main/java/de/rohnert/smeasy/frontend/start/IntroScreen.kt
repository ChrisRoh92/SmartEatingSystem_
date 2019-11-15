package de.rohnert.smeasy.frontend.start

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import backend.helper.Helper
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smeasy.MainActivity
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.frontend.foodtracker.dialogs.DialogDatePicker
import de.rohnert.smeasy.helper.dialogs.CustomDatePicker
import de.rohnert.smeasy.helper.dialogs.DialogSingleList
import java.util.*

class IntroScreen : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    private lateinit var sharePrefs: SharedAppPreferences
    private var helper = Helper()
    private var btnDateClicked = false
    private var spinnerClicked = false
    // Views...
    private lateinit var icon:ImageView
    private lateinit var tvTitle:TextView
    private lateinit var tvBday:TextView
    private lateinit var tvSex:TextView
    private lateinit var card: CardView

    private lateinit var btn:Button
    private lateinit var btnDate:Button
    private lateinit var checkBox:CheckBox
    // ET:
    private lateinit var etUserName:TextInputLayout
    // Spinner:
    private lateinit var spinner:Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_screen)
        sharePrefs = SharedAppPreferences(this)

        initViews()
        initAnimations()
    }

    private fun initViews()
    {
        btn = findViewById(R.id.introscreen_btn)
        btn.setOnClickListener {

            if(checkBox.isChecked && etUserName.editText!!.text.toString().isNotEmpty() && btnDateClicked && spinnerClicked)
            {

                // Speichern der Prefs...
                sharePrefs.setNewUserName(etUserName.editText!!.text.toString())
                sharePrefs.setNewSex(spinner.selectedItem.toString())
                sharePrefs.setNewBday(tvBday.text.toString())
                sharePrefs.setNewAppInitialStart(true)


                var i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }
            else
            {
                Toast.makeText(this,"Bitte alle fehlenden Angaben eintragen...",Toast.LENGTH_SHORT).show()
            }

        }

        btnDate = findViewById(R.id.introscreen_btn_date)
        btnDate.setOnClickListener {
            var dialog = CustomDatePicker()
            dialog.show(supportFragmentManager,"datepicker")
            dialog.setOnCustomDatePickerListener(object: CustomDatePicker.OnCustomDatePickerListener{
                override fun setOnCustomDatePickerListener(date: Date) {
                    Log.d("Smeasy","IntroScreen - Datepicker date: $date")
                    tvBday.text = helper.getStringFromDate(date)
                    btnDateClicked = true
                }

            })

        }

        checkBox = findViewById(R.id.introscreen_checkbox)
        checkBox.isChecked = false

        etUserName = findViewById(R.id.introscreen_et_username)


        // Icon:
        icon = findViewById(R.id.introscreen_icon)

        //CardView:
        card = findViewById(R.id.introscreen_cardview)

        // TextViews:
        tvTitle = findViewById(R.id.introscreen_tv_title)
        tvSex = findViewById(R.id.introscreen_tv_sexTitle)
        tvBday = findViewById(R.id.introscreen_tv_bday)
        tvBday.text = helper.getStringFromDate(helper.getCurrentDate())

        // Spinner:
        spinner = findViewById(R.id.introscreen_spinner)
        var adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayListOf("Frau","Mann"))
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this










    }


    override fun onNothingSelected(p0: AdapterView<*>?) {
        spinnerClicked = false
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        spinnerClicked = true
    }

    private fun initAnimations()
    {
        var iconAnimator = getObjectAnimation(icon,delay = 150)
        var titleAnimator = getObjectAnimation(tvTitle, delay = 150)

        var etAnimator = getObjectAnimation(etUserName)

        var cardAnimation = getObjectAnimation(card)
        var tvSexAnimator = getObjectAnimation(tvSex)
        var spinnerAnimator = getObjectAnimation(spinner)

        var btnDateAnimator = getObjectAnimation(btnDate)
        var tvBdayAnimator = getObjectAnimation(tvBday)

        var checkBoxAnimator = getObjectAnimation(checkBox)

        var btnSaveAnimator = getObjectAnimation(btn)

        var set = AnimatorSet()
        var set1 = AnimatorSet()
        var set2 = AnimatorSet()
        var set3 = AnimatorSet()
        var set4 = AnimatorSet()

        set1.playTogether(iconAnimator,titleAnimator)
        set2.playTogether(cardAnimation,tvSexAnimator,spinnerAnimator)
        set3.playTogether(btnDateAnimator,tvBdayAnimator)

        set.playTogether(set1,etAnimator,set2,set3,checkBoxAnimator,btnSaveAnimator)
        set.startDelay = 350

        //set.playSequentially(iconAnimation,titleAnimation)
//        set.play(iconAnimator).with(titleAnimator).before(etAnimator).before(tvSexAnimator).with(spinnerAnimator)
//            .before(btnDateAnimator).with(tvBdayAnimator).before(checkBoxAnimator).before(btnSaveAnimator)
        set.start()



    }


    private fun getObjectAnimation(view:View,delay:Long = 0):ObjectAnimator
    {
        var alpha = PropertyValuesHolder.ofFloat(View.ALPHA,0f,1f)
        var scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,0f,1.0f)
        var scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,0f,1.0f)

        var animator = ObjectAnimator.ofPropertyValuesHolder(view,alpha,scaleX,scaleY).apply {
            duration = 350
            startDelay = delay
            interpolator = FastOutSlowInInterpolator()
        }

        return animator
    }
}
