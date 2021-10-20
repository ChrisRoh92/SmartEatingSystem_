package de.rohnert.smarteatingsystem.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesSmeasyValues(var context: Context)
{
    private var sharedPref: SharedPreferences = context.getSharedPreferences("smeasy", Context.MODE_PRIVATE)
    private lateinit var editor: SharedPreferences.Editor

    // Aktuelle BodyDaten:
    var weight:Float
    var kfa:Float
    var bmi:Float
    var bauch:Float
    var brust:Float
    var hals:Float
    var huefte:Float

    // Aktueller Fortschritt:
    var progressWeight:Float
    var progressKfa:Float
    var progressBmi:Float
    var progressBauch:Float
    var progressBrust:Float
    var progressHals:Float
    var progressHuefte:Float

    // Aktuelle DailyDaten
    var kcal:Float
    var carbs:Float
    var protein:Float
    var fett:Float

    init {

        weight = sharedPref.getFloat("weight",0f)
        kfa = sharedPref.getFloat("kfa",0f)
        bmi = sharedPref.getFloat("bmi",0f)
        bauch = sharedPref.getFloat("bauch",0f)
        brust = sharedPref.getFloat("brust",0f)
        hals = sharedPref.getFloat("hals",0f)
        huefte = sharedPref.getFloat("huefte",0f)

        progressWeight = sharedPref.getFloat("progressWeight",0f)
        progressKfa = sharedPref.getFloat("progressKfa",0f)
        progressBmi = sharedPref.getFloat("progressBmi",0f)
        progressBauch = sharedPref.getFloat("progressBrust",0f)
        progressBrust = sharedPref.getFloat("brust",0f)
        progressHals = sharedPref.getFloat("progressHals",0f)
        progressHuefte = sharedPref.getFloat("progressHuefte",0f)

        kcal = sharedPref.getFloat("kcal",0f)
        carbs = sharedPref.getFloat("carbs",0f)
        protein = sharedPref.getFloat("protein",0f)
        fett = sharedPref.getFloat("fett",0f)

    }

    // BodyDaten Setzen:
    fun setNewWeight(value:Float)
    {
        weight = value
        saveFloat(value,"weight")
    }

    fun setNewKfa(value:Float)
    {
        kfa = value
        saveFloat(value,"kfa")
    }

    fun setNewBmi(value:Float)
    {
        bmi = value
        saveFloat(value,"bmi")
    }

    fun setNewBauch(value:Float)
    {
        bauch = value
        saveFloat(value,"bauch")
    }

    fun setNewBrust(value:Float)
    {
        brust = value
        saveFloat(value,"brust")
    }

    fun setNewHals(value:Float)
    {
        hals = value
        saveFloat(value,"weight")
    }

    fun setNewHuefte(value:Float)
    {
        huefte = value
        saveFloat(value,"huefte")
    }

    // Body Progress Setzen
    fun setNewWeightProgress(value:Float)
    {
        progressWeight = value
        saveFloat(value,"progressWeight")
    }

    fun setNewKfaProgress(value:Float)
    {
        progressKfa = value
        saveFloat(value,"progressKfa")
    }

    fun setNewBmiProgress(value:Float)
    {
        progressBmi = value
        saveFloat(value,"progressBmi")
    }

    fun setNewBauchProgress(value:Float)
    {
        progressBauch = value
        saveFloat(value,"progressBauch")
    }

    fun setNewBrustProgress(value:Float)
    {
        progressBrust = value
        saveFloat(value,"progressBrust")
    }

    fun setNewHalsProgress(value:Float)
    {
        progressHals = value
        saveFloat(value,"progressHals")
    }

    fun setNewHuefteProgress(value:Float)
    {
        progressHuefte = value
        saveFloat(value,"progressHuefte")
    }


    // Daily Stuff setzen

    fun setNewKcal(value:Float)
    {
        kcal = value
        saveFloat(value,"kcal")
    }

    fun setNewCarbs(value:Float)
    {
        carbs = value
        saveFloat(value,"carbs")
    }

    fun setNewProtein(value:Float)
    {
        protein = value
        saveFloat(value,"protein")
    }

    fun setNewFett(value:Float)
    {
        fett = value
        saveFloat(value,"fett")
    }



    // Data Setter:
    fun saveFloat(value:Float,key:String)
    {
        editor = sharedPref.edit()
        editor.putFloat(key,value)
        editor.commit()
    }

}