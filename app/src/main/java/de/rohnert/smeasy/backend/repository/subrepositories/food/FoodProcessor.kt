package com.example.roomdatabaseexample.backend.repository.subrepositories.food

import android.app.Application
import android.content.SharedPreferences
import com.example.roomdatabaseexample.backend.databases.daily_database.MealEntry
import com.example.roomdatabaseexample.backend.databases.daily_database.helper.CalcedFood
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences


// Folgende Methoden müssen noch implementiert werden:
/*
- getCalcedFoodByMeal(entries:ArrayList<MealEntry>):ArrayList<CalcedFood>
- getMealValues(entries:ArrayList<MealEntry>):ArrayList<Float>
- getDailyValues(daily:Daily):ArrayList<Float>



 */
class FoodProcessor(var application: Application)
{

    //var preferences = SharedAppPreferences()

    fun getFiltertedFoodList(filterItems:ArrayList<String>, foods:ArrayList<Food>):ArrayList<Food>
    {
        var export:ArrayList<Food> = ArrayList()
        for(i in foods)
        {
            if(filterItems.contains(i.category)) export.add(i)
        }

        return export
    }

    fun getAllowFoodList(foods: ArrayList<Food>):ArrayList<Food>
    {
        var export:ArrayList<Food> = ArrayList()
        for(i in foods)
        {
            if(i.kcal < 250f) export.add(i)
        }

        return export
    }




}