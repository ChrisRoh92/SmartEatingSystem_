package com.example.roomdatabaseexample.backend.repository.subrepositories.food

import android.app.Application
import com.example.roomdatabaseexample.backend.databases.daily_database.MealEntry
import com.example.roomdatabaseexample.backend.databases.daily_database.helper.CalcedFood
import com.example.roomdatabaseexample.backend.databases.food_database.Food


// Folgende Methoden müssen noch implementiert werden:
/*
- getCalcedFoodByMeal(entries:ArrayList<MealEntry>):ArrayList<CalcedFood>
- getMealValues(entries:ArrayList<MealEntry>):ArrayList<Float>
- getDailyValues(daily:Daily):ArrayList<Float>



 */
class FoodProcessor(var application: Application, var foodList:ArrayList<Food>)
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

    // Über diese Methode, wird eine neue ID, für die UserFoods generiert..
    fun getNextUserFoodList(userfoods:ArrayList<Food>):String
    {
        var ids:ArrayList<String> = ArrayList()
        if(userfoods.isNullOrEmpty())
        {
            return "u1"
        }
        else
        {
            for(i in userfoods)
            {
                ids.add(i.id)
            }

            var idNum:ArrayList<Int> = ArrayList()
            for(i in ids)
            {
                idNum.add(i.substring(1).toInt())
            }

            return "u${idNum.last()+1}"




        }



    }













    // Helper-Methoden:
    fun getCalcecFoodValues(entry:MealEntry):ArrayList<Float>
    {
        var food = getFoodById(entry.id)
        var export:ArrayList<Float> = ArrayList()
        export.add(food.kcal * (entry.menge/100f))
        export.add(food.carb * (entry.menge/100f))
        export.add(food.protein * (entry.menge/100f))
        export.add(food.fett * (entry.menge/100f))

        return export
    }

    fun getCalcedFood(entry:MealEntry):CalcedFood
    {
        var food = getFoodById(entry.id)
        var values:ArrayList<Float> = ArrayList()
        values.add(food.kcal * (entry.menge/100f))
        values.add(food.carb * (entry.menge/100f))
        values.add(food.protein * (entry.menge/100f))
        values.add(food.fett * (entry.menge/100f))

        return CalcedFood(entry.mealID,food,entry.menge,values)

    }

    private fun getFoodById(id:String):Food
    {
        var food = foodList[0]
        for(i in foodList)
        {
            if(i.id == id)
            {
                food = i
                break
            }
        }

        return food
    }









}