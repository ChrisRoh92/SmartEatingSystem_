package de.rohnert.smeasy.backend.statistic

import android.content.Context
import android.util.Log
import de.rohnert.smeasy.backend.helper.Helper
import de.rohnert.smeasy.backend.databases.daily_database.Daily
import de.rohnert.smeasy.backend.databases.daily_database.MealEntry
import de.rohnert.smeasy.backend.databases.daily_database.helper.CalcedFood
import de.rohnert.smeasy.backend.databases.food_database.normal_database.Food
import de.rohnert.smeasy.backend.repository.subrepositories.food.FoodProcessor
import de.rohnert.smeasy.backend.repository.MainRepository2
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class StatisticProcessor(var context: Context, var repository: MainRepository2, var foodProcessor: FoodProcessor)
{

    private var dailyList:ArrayList<Daily> = ArrayList()
    private var prefs = SharedAppPreferences(context)
    private var maxPrefValues = arrayListOf(prefs.maxCarbValue,prefs.maxProteinValue,prefs.maxFettValue)

    // Content:
    var kcalList:ArrayList<Float> = ArrayList()
    var allKcal:Float = 0f


    suspend fun getDailyList(days:ArrayList<String>):ArrayList<Daily>
    {

         var export:ArrayList<Daily> = ArrayList()
        for(i in days)
        {
            export.add(repository.getDailyByDate(i))
        }

        dailyList = export


        return export

    }



    // Welche weiteren Funktionen werden hier noch benötigt?





    //
    fun getAlLKcalValues():ArrayList<Float>
    {
        var export:ArrayList<Float> = ArrayList()
        for(i in dailyList)
        {
            export.add(getDailyValues(i)[0].roundToInt().toFloat())
        }
       kcalList = export

        return ArrayList(kcalList.asReversed())

    }

    // Anzahl aufgenommene Lebensmittel anzeigen
    fun getNumberOfFoods():Int
    {
        var numberFoods:Int = 0
        for(i in dailyList)
        {
            numberFoods +=  i.breakfastEntry!!.size
            numberFoods +=  i.lunchEntry!!.size
            numberFoods +=  i.dinnerEntry!!.size
            numberFoods +=  i.snackEntry!!.size
        }

        return numberFoods
    }

    // Menge von Nährstoffen im Zeitraum
    fun getAllNutritionValues():ArrayList<Float>
    {
        var export:ArrayList<Float> = getAlLKcalValues()
        export.removeAt(0)
        return export
    }

    // Anzeigen wie viele Kalorien je Maahlzeit aufgenommen wurden...
    fun getKcalFromMeal():ArrayList<Float>
    {
        var export:ArrayList<Float> = ArrayList()
        export.add(0f)
        export.add(0f)
        export.add(0f)
        export.add(0f)

        for(i in dailyList)
        {
            export[0] += getValuesFromMeal(i.breakfastEntry!!)[0]
            export[1] += getValuesFromMeal(i.breakfastEntry!!)[1]
            export[2] += getValuesFromMeal(i.breakfastEntry!!)[2]
            export[3] += getValuesFromMeal(i.breakfastEntry!!)[3]
        }

        return export
    }

    fun getTop20Foods():ArrayList<CalcedFood>
    {
        var allFoods:ArrayList<CalcedFood> = ArrayList()
        for(i in dailyList)
        {
            allFoods.addAll(getCalcedFoodFromMeal(i.breakfastEntry!!))
            allFoods.addAll(getCalcedFoodFromMeal(i.lunchEntry!!))
            allFoods.addAll(getCalcedFoodFromMeal(i.dinnerEntry!!))
            allFoods.addAll(getCalcedFoodFromMeal(i.snackEntry!!))
        }

        // Nachfolgende Liste enhält alle Lebensmittel ohne Dubbels...
        var singleFoodList:ArrayList<CalcedFood> = ArrayList()

        // Check for Duplicates:
        for((index,i)in allFoods.withIndex())
        {
            // Prüfen ob die nächste Id schon vorhanden ist...
            var exist = false
            for(j in singleFoodList)
            {
                if(j.f.id == i.f.id)
                {
                    exist = true
                    break
                }
            }

            // Wenn nicht, in dieser Liste weiterer dieser IDs suchen...
            if(!exist)
            {
                var id = i.f.id
                var menge = i.menge
                // Nach allen Positionen suchen
                for((indexJ,j) in allFoods.withIndex())
                {
                    if(indexJ != index)
                    {
                        if(j.f.id == id)
                        {
                            menge += j.menge
                        }
                    }
                }
                //Log.d("Smeasy","StatisticProcessor - getTop20Foods() - FoodID: $id , menge = $menge in g")
                var calcedFood = CalcedFood(i.id,i.f,menge,foodProcessor.getCalcecFoodValues(
                    MealEntry(i.id,i.f.id,menge)
                ))
                singleFoodList.add(calcedFood)
            }
        }

        // Liste nach Menge Sortieren:
        var sortedSingleFoodList:ArrayList<CalcedFood> = ArrayList()
        sortedSingleFoodList = ArrayList(singleFoodList.sortedWith(compareBy { it.menge }))
        sortedSingleFoodList = ArrayList(sortedSingleFoodList.asReversed())
        // Top 2ß auswählen
        var exportFoodList:ArrayList<CalcedFood> = ArrayList()
        if(sortedSingleFoodList.size <= 20)
        {
            exportFoodList = sortedSingleFoodList
        }
        else
        {
            for(i in 0..19)
            {
                exportFoodList.add(sortedSingleFoodList[i])
            }
        }

        return exportFoodList
    }

    fun getCalcedNutritionValueList():ArrayList<ArrayList<Float>>
    {
        var export:ArrayList<ArrayList<Float>> = ArrayList()
        export.add(ArrayList()) // Einträge für Kohlenhydrate
        export.add(ArrayList()) // Einträge für Proteine
        export.add(ArrayList()) // Einträge für Fett
        for(i in dailyList)
        {
            var values = getNutritionCourseValueFromDaily(i)
            export[0].add(values[0])
            export[1].add(values[1])
            export[2].add(values[2])
        }
        export.add(ArrayList()) // gefüllt mit 100%
        for(i in export[0])
        {
            export[3].add(100f)
        }

        var newExport:ArrayList<ArrayList<Float>> = ArrayList()
        newExport.add(ArrayList(export[0].asReversed()))
        newExport.add(ArrayList(export[1].asReversed()))
        newExport.add(ArrayList(export[2].asReversed()))
        newExport.add(ArrayList(export[3].asReversed()))



        return newExport
    }






    // Methoden, die wohl auch woanders ausgelagert werden können:
    private fun getValuesFromMeal(values:ArrayList<MealEntry>):ArrayList<Float>
    {
        var export:ArrayList<ArrayList<Float>> = ArrayList()
        for(i in values)
        {
            export.add(foodProcessor.getCalcecFoodValues(i))
        }
        var exportValues:ArrayList<Float> = ArrayList()
        exportValues.add(0f)
        exportValues.add(0f)
        exportValues.add(0f)
        exportValues.add(0f)
        for(i in export)
        {
            exportValues[0] += i[0]
            exportValues[1] += i[1]
            exportValues[2] += i[2]
            exportValues[3] += i[3]
        }

        return exportValues




    }

    private fun getDailyValues(daily: Daily):ArrayList<Float>
    {
        var export:ArrayList<Float> = ArrayList()
        export.add(0f)
        export.add(0f)
        export.add(0f)
        export.add(0f)
        var entryList:ArrayList<ArrayList<MealEntry>> =
            arrayListOf(daily.breakfastEntry!!,daily.lunchEntry!!,daily.dinnerEntry!!,daily.snackEntry!!)

        for(i in entryList)
        {
            var values = getValuesFromMeal(i)
            export[0] += values[0]
            export[1] += values[1]
            export[2] += values[2]
            export[3] += values[3]
        }

        return export
    }

    private fun getCalcedFoodFromMeal(values:ArrayList<MealEntry>):ArrayList<CalcedFood>
    {
        var export:ArrayList<CalcedFood> = ArrayList()
        for(i in values)
        {
            export.add(foodProcessor.getCalcedFood(i))
        }

        return export

    }

    private fun getNutritionCourseValueFromDaily(daily: Daily):ArrayList<Float>
    {
        var export:ArrayList<Float> = ArrayList()
        var values = getDailyValues(daily)
        var maxValues = arrayListOf(daily.maxKcal,daily.maxCarb,daily.maxProtein,daily.maxFett)
        for(i in 1..3)
        {
            if(maxValues[i] == 0f)
            {
                export.add((values[i]/maxPrefValues[i-1])*100f)
            }
            else
            {
                export.add((values[i]/maxValues[i])*100f)
            }

        }
        Log.d("Smeasy","StatisticProcessor - getNutritionCourseValueFromDaily maxValues: $maxValues")
        Log.d("Smeasy","StatisticProcessor - getNutritionCourseValueFromDaily daily-Date: ${daily.date}")
        Log.d("Smeasy","StatisticProcessor - getNutritionCourseValueFromDaily export: $export")

        return export


    }












}