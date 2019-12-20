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
import de.rohnert.smeasy.helper.dialogs.DialogSingleChoiceList
import de.rohnert.smeasy.helper.dialogs.DialogSingleList
import java.util.*
import kotlin.collections.ArrayList

class IntroScreen : AppCompatActivity() {


    private lateinit var sharePrefs: SharedAppPreferences
    private var helper = Helper()

    // Content:
    private lateinit var content:ArrayList<String>
    private var mDate:String = ""
    private var mName:String = ""
    private var mSex:String = ""
    private var mHeight:Float = 0f
    private var mBday:String = ""

    // Views...
    private lateinit var icon:ImageView
    private lateinit var tvBday:TextView
    private lateinit var tvSex:TextView

    private lateinit var btn:Button
    private lateinit var btnDate:ImageButton
    private lateinit var btnSex:ImageButton

    // et:
    private lateinit var etUserName:TextInputLayout
    private lateinit var etUserHeight:TextInputLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_screen)
        sharePrefs = SharedAppPreferences(this)

        initViews()

    }

    private fun initViews()
    {
        btn = findViewById(R.id.introscreen_btn)
        btn.setOnClickListener {

            if(etUserName.editText!!.text.toString().isNotEmpty() && etUserHeight.editText!!.text.toString().isNotEmpty() && mSex != "" && mBday != "")
            {

                // Speichern der Prefs...
                sharePrefs.setNewUserName(etUserName.editText!!.text.toString().trim())
                sharePrefs.setNewUserHeight(etUserHeight.editText!!.text.toString().toFloat())
                sharePrefs.setNewSex(mSex)
                sharePrefs.setNewBday(mBday)
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
                    mBday = helper.getStringFromDate(date)
                }

            })

        }

        btnSex = findViewById(R.id.introscreen_btn_sex)
        btnSex.setOnClickListener {
            var dialog = DialogSingleChoiceList("Geschlecht wählen","Bitte wähle aus der Liste dein Geschlecht",
                arrayListOf("Frau","Mann"),this,true,-1)
            dialog.onItemClickListener(object:DialogSingleChoiceList.OnDialogListListener{
                override fun onItemClickListener(value: String, pos: Int) {
                    mSex = value
                    tvSex.text = value
                }

            })
        }


        etUserName = findViewById(R.id.introscreen_et_username)
        etUserHeight = findViewById(R.id.introscreen_et_userheight)


        // Icon:
        icon = findViewById(R.id.introscreen_icon)



        // TextViews:
        tvBday = findViewById(R.id.introscreen_tv_bday)
        tvBday.text = "Eingabe erforderlich"

        tvSex = findViewById(R.id.introscreen_tv_sex)












    }



}
