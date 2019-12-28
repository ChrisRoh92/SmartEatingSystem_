package com.example.roomdatabaseexample.backend.repository.subrepositories.daily

import android.app.Application
import android.content.Context
import com.example.roomdatabaseexample.backend.databases.daily_database.MealEntry
import com.example.roomdatabaseexample.backend.databases.daily_database.helper.CalcedFood
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import de.rohnert.smeasy.backend.databases.food_database.extend_database.ExtendedFood

class DailyProcessor(var context: Context)
{

    fun getCalcedFood(id:Int, food: ExtendedFood, menge:Float ):CalcedFood
    {
        var values:ArrayList<Float> = ArrayList()
        values.add(food.kcal * (menge/100f))
        values.add(food.carb * (menge/100f))
        values.add(food.protein * (menge/100f))
        values.add(food.fett * (menge/100f))

        return CalcedFood(id,food,menge,values)
    }


    fun getNewMealEntryId(entryList:ArrayList<MealEntry>):Int
    {
        if(entryList.isNullOrEmpty())
        {
            return 0
        }
        else
        {
            return entryList.last().mealID + 1
        }

    }

    fun getMealValues(entries:ArrayList<CalcedFood>):ArrayList<Float>
    {
        var values:ArrayList<Float> = arrayListOf(0f,0f,0f,0f)
        for(i in entries)
        {
            values[0] += i.values[0]
            values[1] += i.values[1]
            values[2] += i.values[2]
            values[3] += i.values[3]
        }

        return values

    }






}