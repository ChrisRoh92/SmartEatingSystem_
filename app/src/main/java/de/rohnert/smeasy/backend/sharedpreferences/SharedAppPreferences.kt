package de.rohnert.smeasy.backend.sharedpreferences

import android.app.Activity
import android.app.Application
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.SharedPreferences
import kotlin.math.roundToInt


class SharedAppPreferences(var context: Context)
{


    private var sharedPref: SharedPreferences = context.getSharedPreferences("pref",Context.MODE_PRIVATE)
    private lateinit var editor: SharedPreferences.Editor
    // Werte für App-Erlebnis..
    var appInitalStart:Boolean
    // Einstellungen für Foodwerte...
    var maxKcal:Float
    var maxCarb:Float
    var maxProtein:Float
    var maxFett:Float
    var maxCarbValue:Float = 0f
    var maxProteinValue:Float = 0f
    var maxFettValue:Float = 0f
    // User Data:
    var userName:String
    var userHeight:Float
    var sex:String
    var bday:String

    // Andere BodySettings:
    var aim:String
    var aimWeightLoss:Float
    var weightAim:Float
    var bmiAim:Float
    var kfaAim:Float
    var bauchAim:Float
    var brustAim:Float
    var halsAim:Float
    var huefteAim:Float

    // Werte für FoodListFilter:
    var maxAllowedKcal:Float
    var maxAllowedCarbs:Float = 0f
    var maxAllowedProtein:Float = 0f
    var maxAllowedFett:Float = 0f

    init {



        maxKcal = sharedPref.getFloat("maxKcal",2500f)
        maxCarb = sharedPref.getFloat("maxCarb",100f)
        maxProtein = sharedPref.getFloat("maxProtein",100f)
        maxFett = sharedPref.getFloat("maxFett",50f)

        initMaxValues()

        aim = sharedPref.getString("aim","Abnehmen")!!
        aimWeightLoss = sharedPref.getFloat("aimWeightLoss",0f)
        weightAim = sharedPref.getFloat("weightAim",0f)!!
        bmiAim = sharedPref.getFloat("bmiAim",0f)
        kfaAim = sharedPref.getFloat("kfaAim",0f)!!
        bauchAim = sharedPref.getFloat("bauchAim",0f)!!
        brustAim = sharedPref.getFloat("brustAim",0f)!!
        halsAim = sharedPref.getFloat("halsAim",0f)!!
        huefteAim = sharedPref.getFloat("huefteAim",0f)!!



        userName = sharedPref.getString("userName","")!!
        userHeight = sharedPref.getFloat("userHeight",180f)!!
        sex = sharedPref.getString("sex","")!!
        bday = sharedPref.getString("bday","")!!

        maxAllowedKcal = sharedPref.getFloat("maxAllowedKcal",0f)

        appInitalStart = sharedPref.getBoolean("appInitalStart",false)

    }

    private fun initMaxValues()
    {
        maxCarbValue = (((maxCarb/100f)*maxKcal)/4.1f).roundToInt().toFloat()
        maxProteinValue = (((maxProtein/100f)*maxKcal)/4.1f).roundToInt().toFloat()
        maxFettValue = (((maxFett/100f)*maxKcal)/9.2f).roundToInt().toFloat()
    }

    fun setNewAppInitialStart(value:Boolean)
    {
        appInitalStart = value
        saveBoolean(value,"appInitalStart")
    }


    fun setNewMaxKcal(value:Float)
    {
        maxKcal = value
        saveFloat(value,"maxKcal")
        initMaxValues()


    }

    fun setNewMaxCarb(value:Float)
    {
        maxCarb = value
        saveFloat(value,"maxCarb")
        initMaxValues()


    }

    fun setNewMaxProtein(value:Float)
    {
        maxProtein = value
        saveFloat(value,"maxProtein")
        initMaxValues()


    }

    fun setNewMaxFett(value:Float)
    {
        maxFett = value
        saveFloat(value,"maxFett")
        initMaxValues()


    }

    fun setNewMaxAllowedKcal(value:Float)
    {
        maxAllowedKcal = value
        saveFloat(value,"maxAllowedKcal")


    }

    fun setNewAim(value:String)
    {
        aim = value
        saveString(aim,"aim")
    }

    fun setNewAimWeightLoss(value:Float)
    {
        weightAim = value
        saveFloat(value,"aimWeightLoss")
    }

    fun setNewBmiAim()
    {
        bmiAim = weightAim/(userHeight*userHeight)
        saveFloat(bmiAim,"bmiAim")
    }

    fun setNewWeightAim(value:Float)
    {
        weightAim = value
        saveFloat(value,"weightAim")
    }

    fun setNewKfaAim(value:Float)
    {
        kfaAim = value
        saveFloat(value,"kfaAim")
    }

    fun setNewBauchAim(value:Float)
    {
        bauchAim = value
        saveFloat(value,"bauchAim")
    }

    fun setNewBrustAim(value:Float)
    {
        brustAim = value
        saveFloat(value,"brustAim")
    }

    fun setNewHalsAim(value:Float)
    {
        halsAim = value
        saveFloat(value,"halsAim")
    }

    fun setNewHueftAim(value:Float)
    {
        huefteAim = value
        saveFloat(value,"huefteAim")
    }




    fun setNewUserName(value:String)
    {
        userName = value
        saveString(value,"userName")
    }

    fun setNewUserHeight(value:Float)
    {
        userHeight = value
        saveFloat(userHeight,"userHeight")
    }

    fun setNewSex(value:String)
    {
        sex = value
        saveString(value,"sex")
    }

    fun setNewBday(value:String)
    {
        bday = value
        saveString(value,"bday")
    }





    fun saveFloat(value:Float,key:String)
    {
        editor = sharedPref.edit()
        editor.putFloat(key,value)
        editor.commit()
    }

    fun saveString(value:String,key:String)
    {
        editor = sharedPref.edit()
        editor.putString(key,value)
        editor.commit()
    }

    fun saveBoolean(value:Boolean,key:String)
    {
        editor = sharedPref.edit()
        editor.putBoolean(key,value)
        editor.commit()
    }



}