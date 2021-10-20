package de.rohnert.smarteatingsystem.ui.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import de.rohnert.smarteatingsystem.data.helper.Helper
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smarteatingsystem.MainActivity
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.utils.dialogs.CustomDatePicker
import de.rohnert.smarteatingsystem.utils.dialogs.DialogSingleChoiceList
import de.rohnert.smarteatingsystem.utils.dialogs.DialogSingleMessage
import java.util.*

class IntroScreen : AppCompatActivity() {


    private lateinit var sharePrefs: SharedAppPreferences
    private var helper = Helper()

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

    // Skip Button:
    private lateinit var btnSkip:Button


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

        // Skippen:
        btnSkip = findViewById(R.id.introscreen_btn_skip)
        btnSkip.setOnClickListener{
            var dialog = DialogSingleMessage("Überspringen","Es werden Standartwerte eingetragen","Du hast jederzeit die Möglichkeit in den Einstellungen " +
                    "deine Daten nachträglich zu ändern. Die Daten dienen lediglich der Funktionalität von SmartEatingSystem",this)
            dialog.setOnDialogClickListener(object:DialogSingleMessage.OnDialogClickListener{
                override fun setOnDialogClickListener()
                {
                    mName = "Max Mustermann"
                    mSex = "Mann"
                    mHeight = 180f
                    mBday = "01.01.2000"

                    // Speichern der Prefs...
                    sharePrefs.setNewUserName(mName)
                    sharePrefs.setNewUserHeight(mHeight)
                    sharePrefs.setNewSex(mSex)
                    sharePrefs.setNewBday(mBday)
                    sharePrefs.setNewAppInitialStart(true)


                    var i = Intent(applicationContext, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }

            })



        }












    }



}
