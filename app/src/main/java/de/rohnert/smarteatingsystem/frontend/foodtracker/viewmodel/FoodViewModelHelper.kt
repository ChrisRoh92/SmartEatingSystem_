package de.rohnert.smarteatingsystem.frontend.foodtracker.viewmodel

import de.rohnert.smarteatingsystem.backend.databases.daily_database.MealEntry
import de.rohnert.smarteatingsystem.backend.databases.daily_database.helper.CalcedFood
import de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database.ExtendedFood


val TAG = "Smeasy2"
val lunchNames = arrayOf("breakfast","lunch","dinner","snack")

fun getNewMealEntryId(entryList:ArrayList<MealEntry>):Int
{
    return if(entryList.isNullOrEmpty())
    {
        0
    }
    else
    {
        entryList.last().mealID + 1
    }

}


fun getCalcedFood(id:Int, food: ExtendedFood, menge:Float ): CalcedFood
{
    var values:ArrayList<Float> = ArrayList()
    values.add(food.kcal * (menge/100f))
    values.add(food.carb * (menge/100f))
    values.add(food.protein * (menge/100f))
    values.add(food.fett * (menge/100f))

    return CalcedFood(id,food,menge,values)
}

fun getCalcedFoodValues(food:ExtendedFood,menge:Float):ArrayList<Float>
{
    var values:ArrayList<Float> = ArrayList()
    values.add(food.kcal * (menge/100f))
    values.add(food.carb * (menge/100f))
    values.add(food.protein * (menge/100f))
    values.add(food.fett * (menge/100f))

    return values
}