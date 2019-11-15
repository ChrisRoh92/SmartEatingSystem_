package de.rohnert.smeasy.backend.sharedpreferences

import android.app.Activity
import android.app.Application
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.SharedPreferences



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
    // User Data:
    var userName:String
    var sex:String
    var bday:String

    // Werte für FoodListFilter:
    var maxAllowedKcal:Float

    init {



        maxKcal = sharedPref.getFloat("maxKcal",0f)
        maxCarb = sharedPref.getFloat("maxCarb",0f)
        maxProtein = sharedPref.getFloat("maxProtein",0f)
        maxFett = sharedPref.getFloat("maxFett",0f)



        userName = sharedPref.getString("userName","")!!
        sex = sharedPref.getString("sex","")!!
        bday = sharedPref.getString("bday","")!!

        maxAllowedKcal = sharedPref.getFloat("maxAllowedKcal",0f)

        appInitalStart = sharedPref.getBoolean("appInitalStart",false)

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


    }

    fun setNewMaxCarb(value:Float)
    {
        maxCarb = value
        saveFloat(value,"maxCarb")


    }

    fun setNewMaxProtein(value:Float)
    {
        maxProtein = value
        saveFloat(value,"maxProtein")


    }

    fun setNewMaxFett(value:Float)
    {
        maxFett = value
        saveFloat(value,"maxFett")


    }

    fun setNewMaxAllowedKcal(value:Float)
    {
        maxAllowedKcal = value
        saveFloat(value,"maxAllowedKcal")


    }

    fun setNewUserName(value:String)
    {
        userName = value
        saveString(value,"userName")
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