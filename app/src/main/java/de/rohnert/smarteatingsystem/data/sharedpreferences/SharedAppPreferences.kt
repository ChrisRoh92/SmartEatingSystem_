package de.rohnert.smarteatingsystem.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import de.rohnert.smarteatingsystem.data.helper.Helper
import kotlin.math.roundToInt


class SharedAppPreferences(var context: Context)
{


    private var sharedPref: SharedPreferences = context.getSharedPreferences("pref3",Context.MODE_PRIVATE)
    private lateinit var editor: SharedPreferences.Editor
    private var helper = Helper()
    // Werte für App-Erlebnis..
    var appInitalStart:Boolean
    // Einstellungen für maximale Werte, die aufgenommen werden dürfen
    var maxKcal:Float
    var maxCarb:Float
    var maxProtein:Float
    var maxFett:Float
    // Nachfolgend sind die genauen Grammengen zu berechnen, die sich aus den Verhältnissen ergeben
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
    var maxAllowedKcal:Float = 0f
    var maxAllowedCarbs:Float = 0f
    var maxAllowedProtein:Float = 0f
    var maxAllowedFett:Float = 0f
    var minAllowedKcal:Float = 0f
    var minAllowedCarbs:Float = 0f
    var minAllowedProtein:Float = 0f
    var minAllowedFett:Float = 0f

    // Premium-Werte:
    var premiumStatus:Boolean
    var premiumDate:String
    var premiumTime:Int = 30
    var premiumEndDate:String

    // Rating Stuff:
    var rateStatus:Boolean = false      // Prüfen ob Nutzer auf Bewerten geklickt hat, dann soll der Dialog nicht mehr angezeigt werden
    var lastRequestDate:String = ""    // Anzeigen an welchem Datum, das letzte Mal der Nutzer gebeten wurde, die App zu bewerten
    var rateNeverStatus:Boolean = false // Wert zeigt an, ob Nutzer damit noch genervt werden möchte...
    var rateCountAppStart:Int = 0   // Anzahl, wie of die App gestartet wurde, ohne dass der Dialog angezeigt wurde



    init {

        // AllowedFood:
        maxAllowedKcal = sharedPref.getFloat("maxAllowedKcal",-1f)
        maxAllowedCarbs = sharedPref.getFloat("maxAllowedCarbs",-1f)
        maxAllowedProtein = sharedPref.getFloat("maxAllowedProtein",-1f)
        maxAllowedFett = sharedPref.getFloat("maxAllowedFett",-1f)

        minAllowedKcal = sharedPref.getFloat("minAllowedKcal",0f)
        minAllowedCarbs = sharedPref.getFloat("minAllowedCarbs",0f)
        minAllowedProtein = sharedPref.getFloat("minAllowedProtein",0f)
        minAllowedFett = sharedPref.getFloat("minAllowedFett",0f)

        // Premium:
        premiumStatus = sharedPref.getBoolean("premiumStatus",true)
        premiumDate = sharedPref.getString("premiumDate","")!!
        premiumEndDate = sharedPref.getString("premiumEndDate","")!!


        // MaxWerte der Nährwerte, die nicht überschritten werden sollten
        maxKcal = sharedPref.getFloat("maxKcal",2000f)
        maxCarb = sharedPref.getFloat("maxCarb",25f)
        maxProtein = sharedPref.getFloat("maxProtein",50f)
        maxFett = sharedPref.getFloat("maxFett",25f)
        initMaxValues()

        // Body Ziele festlegen...
        aim = sharedPref.getString("aim","Gewicht Halten")!!
        aimWeightLoss = sharedPref.getFloat("aimWeightLoss",0f)
        weightAim = sharedPref.getFloat("weightAim", -1f)
        bmiAim = sharedPref.getFloat("bmiAim",-1f)
        kfaAim = sharedPref.getFloat("kfaAim", -1f)
        bauchAim = sharedPref.getFloat("bauchAim", -1f)
        brustAim = sharedPref.getFloat("brustAim", -1f)
        halsAim = sharedPref.getFloat("halsAim", -1f)
        huefteAim = sharedPref.getFloat("huefteAim", -1f)


        // Userdaten speichern...
        userName = sharedPref.getString("userName","")!!
        userHeight = sharedPref.getFloat("userHeight", 180f)
        sex = sharedPref.getString("sex","")!!
        bday = sharedPref.getString("bday","")!!


        // Prüfen ob App das erste mal gestartet wird...
        appInitalStart = sharedPref.getBoolean("appInitalStart",false)

        // Rating Stuff:
        rateStatus = sharedPref.getBoolean("rateStatus",false)
        lastRequestDate = sharedPref.getString("lastRequestDate","")!!
        rateNeverStatus = sharedPref.getBoolean("rateNeverStatus",false)
        rateCountAppStart = sharedPref.getInt("rateCountAppStart",0)





    }





    fun setNewAppInitialStart(value:Boolean)
    {
        appInitalStart = value
        saveBoolean(value,"appInitalStart")
    }


    // Maximale Nährtwerte setzen
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
    private fun initMaxValues()
    {
        maxCarbValue = (((maxCarb/100f)*maxKcal)/4.1f).roundToInt().toFloat()
        maxProteinValue = (((maxProtein/100f)*maxKcal)/4.1f).roundToInt().toFloat()
        maxFettValue = (((maxFett/100f)*maxKcal)/9.2f).roundToInt().toFloat()
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
        var value = userHeight/100f
        bmiAim = weightAim/(value*value)
        /*Log.d("Smeasy","SharedAppPreferences - setNewBmiAim() - userheight = $userHeight")
        Log.d("Smeasy","SharedAppPreferences - setNewBmiAim() - userheight = $weightAim")
        Log.d("Smeasy","SharedAppPreferences - setNewBmiAim() - userheight = $bmiAim")*/

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



    // Userdaten ändern...
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


    // Premium:
    fun setNewPremiumState(value:Boolean)
    {
        premiumStatus = value
        saveBoolean(value,"premiumStatus")
    }


    // MaxAllowed & MinAllowed Werte setzen
    fun setNewMaxAllowedKcal(value:Float)
    {
        maxAllowedKcal = value
        saveFloat(value,"maxAllowedKcal")


    }
    fun setNewMaxAllowedCarbs(value:Float)
    {
        maxAllowedCarbs = value
        saveFloat(value,"maxAllowedCarbs")


    }
    fun setNewMaxAllowedProtein(value:Float)
    {
        maxAllowedProtein = value
        saveFloat(value,"maxAllowedProtein")


    }
    fun setNewMaxAllowedFett(value:Float)
    {
        maxAllowedFett = value
        saveFloat(value,"maxAllowedFett")


    }
    fun setNewMinAllowedKcal(value:Float)
    {
        minAllowedKcal = value
        saveFloat(value,"minAllowedKcal")


    }
    fun setNewMinAllowedCarbs(value:Float)
    {
        minAllowedCarbs = value
        saveFloat(value,"minAllowedCarbs")


    }
    fun setNewMinAllowedProtein(value:Float)
    {
        minAllowedProtein = value
        saveFloat(value,"minAllowedProtein")


    }
    fun setNewMinAllowedFett(value:Float)
    {
        minAllowedFett = value
        saveFloat(value,"minAllowedFett")


    }


    // Rating Methoden:
    fun setNewRateStatus(value:Boolean)
    {
        rateStatus = value
        saveBoolean(value,"rateStatus")
    }
    fun setNewLastRequestDate(value:String)
    {
        lastRequestDate = value
        saveString(value,"lastRequestDate")
    }
    fun setNewRateNeverStatus(value:Boolean)
    {
        rateNeverStatus = value
        saveBoolean(value,"rateNeverStatus")
    }
    fun setNewRateCountAppStart(value:Int)
    {
        rateCountAppStart = value
        saveInt(value,"rateCountAppStart")
    }


    fun getMaxAllowedFoodValues():ArrayList<Float>
    {
        var export:ArrayList<Float> = ArrayList()
        export.add(maxAllowedKcal)
        export.add(maxAllowedCarbs)
        export.add(maxAllowedProtein)
        export.add(maxAllowedFett)
        return export
    }
    fun getMinAllowedFoodValues():ArrayList<Float>
    {
        var export:ArrayList<Float> = ArrayList()
        export.add(minAllowedKcal)
        export.add(minAllowedCarbs)
        export.add(minAllowedProtein)
        export.add(minAllowedFett)
        return export
    }






    // Methoden:
    fun saveFloat(value:Float,key:String)
    {
        editor = sharedPref.edit()
        editor.putFloat(key,value)
        editor.commit()
    }

    fun saveInt(value:Int,key:String)
    {
        editor = sharedPref.edit()
        editor.putInt(key,value)
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