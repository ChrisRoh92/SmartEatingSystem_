package com.example.roomdatabaseexample.backend.databases.daily_database.helper

import com.example.roomdatabaseexample.backend.databases.food_database.Food

data class CalcedFood(var id:Int,var f: Food,var menge:Float,var values:ArrayList<Float> ) {
}