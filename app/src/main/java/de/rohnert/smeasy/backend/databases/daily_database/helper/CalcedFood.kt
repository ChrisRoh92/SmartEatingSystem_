package com.example.roomdatabaseexample.backend.databases.daily_database.helper

import com.example.roomdatabaseexample.backend.databases.food_database.Food
import de.rohnert.smeasy.backend.databases.food_database.extend_database.ExtendedFood

data class CalcedFood(var id:Int, var f: ExtendedFood, var menge:Float, var values:ArrayList<Float> )